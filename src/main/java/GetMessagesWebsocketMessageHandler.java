import io.javalin.websocket.WsSession;

public class GetMessagesWebsocketMessageHandler implements WebsocketMessageHandler {
    private static class GetMessagesCommandData {
        public int userId;
        public int friendId;
    }

    public static Class<GetMessagesCommandData> getDataClass() {
        return GetMessagesCommandData.class;
    }

    GetMessagesWebsocketMessageHandler(GetMessagesCommandData data) {

    }

    @Override
    public void handleMessage(WsSession session) {

    }
}
