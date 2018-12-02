public class WebsocketMessageHandlerFactory {
    private enum WebSocketMessageType {
        GET_MESSAGES,
        SEND_MESSAGE
    }

    public WebsocketMessageHandler create(WebsocketMessage message) {
        WebSocketMessageType type = WebSocketMessageType.valueOf(message.command);
        switch (type) {
            case GET_MESSAGES:
                System.out.println("get message!");
                return null;
            case SEND_MESSAGE:
                System.out.println("send message!");
            default:
                return null;
        }
    }
}
