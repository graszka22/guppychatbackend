import com.google.gson.Gson;
import io.javalin.websocket.WsHandler;

import java.util.function.Consumer;

public class WebSocketHandler implements Consumer<WsHandler> {
    private WebsocketMessageHandlerWrapperFactory messageHandlerFactory;

    public WebSocketHandler() {
        messageHandlerFactory = new WebsocketMessageHandlerWrapperFactory();
    }

    @Override
    public void accept(WsHandler wsHandler) {
        wsHandler.onMessage((session, message) -> {
            WebsocketMessageHandlerWrapper handler = messageHandlerFactory.create(message);
            handler.runHandler(session);
        });

        wsHandler.onClose((session, statusCode, reason) -> {

        });
    }
}
