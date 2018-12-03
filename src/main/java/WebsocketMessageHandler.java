import io.javalin.websocket.WsSession;

public interface WebsocketMessageHandler {
    void handleMessage(WsSession session);
}
