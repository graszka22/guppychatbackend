import com.google.gson.JsonObject;
import io.javalin.websocket.WsSession;

public class WebsocketMessageHandlerWrapper<T extends WebsocketMessageHandler> {
    private T messageHandler;

    WebsocketMessageHandlerWrapper(JsonObject jsonObject, T messageHandler) {
        this.messageHandler = messageHandler;
    }

    void runHandler(WsSession session) {
        messageHandler.handleMessage(session);
    }
}
