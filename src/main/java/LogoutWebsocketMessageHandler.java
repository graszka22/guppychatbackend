import com.google.gson.Gson;
import io.javalin.websocket.WsSession;

import java.sql.SQLException;

public class LogoutWebsocketMessageHandler implements WebsocketMessageHandler {
    private static class LogoutCommandResponse {
        final String command = "LOGOUT_RESPONSE";
    }
    @Override
    public void handleMessage(WsSession session, AccountGateway account) throws SQLException {
        account.setSessionToken(null);
        account.update();
        session.send(new Gson().toJson(new LogoutCommandResponse()));
        Registry.getWebsocketSessionsMap().removeSession(account.getUserId());
    }
}
