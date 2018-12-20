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

    private int authenticate(WsSession session) {
        String token = jsonObject.get("token").getAsString();
        AccountFinder finder = new AccountFinder();
        try {
            AccountGateway account = finder.findByToken(token);
            return account.getUserId();
        } catch(SQLException ex) {
            throw new RuntimeException(ex);
        } catch(NoSuchElementException ex) {
            session.send("Invalid auth token.");
            session.close();
        }
        return -1;
    }

    private void registerSession(WsSession session, int userId) {
        WebsocketSessionsMap map = Registry.getWebsocketSessionsMap();
        map.addSession(session, userId);
    }

    void runHandler(WsSession session) throws SQLException {
        int userId = authenticate(session);
        if(userId == -1) return;
        registerSession(session, userId);
        messageHandler.handleMessage(session);
    }
}
