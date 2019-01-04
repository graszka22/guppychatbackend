import com.google.gson.JsonObject;
import io.javalin.websocket.WsSession;

import java.sql.SQLException;
import java.util.NoSuchElementException;

public class WebsocketMessageHandlerWrapper<T extends WebsocketMessageHandler> {
    private T messageHandler;
    private JsonObject jsonObject;

    WebsocketMessageHandlerWrapper(JsonObject jsonObject, T messageHandler) {
        this.jsonObject = jsonObject;
        this.messageHandler = messageHandler;
    }

    private AccountGateway authenticate(WsSession session) {
        System.out.println(jsonObject);
        String token = jsonObject.get("token").getAsString();
        AccountFinder finder = new AccountFinder();
        try {
            return finder.findByToken(token);
        } catch(SQLException ex) {
            throw new RuntimeException(ex);
        } catch(NoSuchElementException ex) {
            session.send("Invalid auth token.");
            session.close();
        }
        return null;
    }

    private void registerSession(WsSession session, int userId) {
        WebsocketSessionsMap map = Registry.getWebsocketSessionsMap();
        map.addSession(session, userId);
    }

    void runHandler(WsSession session) throws SQLException {
        AccountGateway account = authenticate(session);
        if(account == null) return;
        registerSession(session, account.getUserId());
        messageHandler.handleMessage(session, account);
    }
}
