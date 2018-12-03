import io.javalin.websocket.WsSession;

public class SendMessageWebsocketMessageHandler implements WebsocketMessageHandler {
    private static class SendMessageCommandData {
        public int userId;
        public int to;
        public String message;
    }

    SendMessageWebsocketMessageHandler(SendMessageCommandData data) {}

    @Override
    public void handleMessage(WsSession session) {

    }

    public static Class<SendMessageCommandData> getDataClass() {
        return SendMessageCommandData.class;
    }
}
