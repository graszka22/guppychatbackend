import io.javalin.websocket.WsSession;

import java.sql.SQLException;

public interface WebsocketMessageHandler {
    void handleMessage(WsSession session, AccountGateway account) throws SQLException;
}
