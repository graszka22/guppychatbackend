import com.google.gson.Gson;
import io.javalin.websocket.WsSession;

import java.sql.Timestamp;
import java.sql.SQLException;

public class SendMessageWebsocketMessageHandler implements WebsocketMessageHandler {
    public static class SendMessageCommandData {
        public int userId;
        public int to;
        public String message;
    }

    private static class ReceiveMessageWebsocketMessage {
        MessageGateway data;
        String command = "RECEIVE_MESSAGE";
        ReceiveMessageWebsocketMessage(MessageGateway data) {
            this.data = data;
        }
    }

    private SendMessageCommandData data;

    SendMessageWebsocketMessageHandler(SendMessageCommandData data) {
        this.data = data;
    }

    @Override
    public void handleMessage(WsSession session) throws SQLException {
        MessageGateway message = new MessageGateway(data.userId, data.to, data.message, new Timestamp(System.currentTimeMillis()));
        message.insert();
        MessageIdentityMap map = Registry.getMessageIdentityMap();
        map.addMessage(message);
        WebsocketSessionsMap sessionsMap = Registry.getWebsocketSessionsMap();
        WsSession receiverSession = sessionsMap.getUserSession(data.to);
        if(receiverSession != null) {
            receiverSession.send(new Gson().toJson(new ReceiveMessageWebsocketMessage(message)));
        }
    }

    public static Class<SendMessageCommandData> getDataClass() {
        return SendMessageCommandData.class;
    }
}
