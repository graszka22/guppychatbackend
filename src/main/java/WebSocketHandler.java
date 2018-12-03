import com.google.gson.Gson;
import io.javalin.websocket.WsHandler;

import java.util.function.Consumer;

public class WebSocketHandler implements Consumer<WsHandler> {
    private WebsocketSessionsMap sessionsMap;
    private Gson gson;
    private WebsocketMessageHandlerWrapperFactory messageHandlerFactory;

    public WebSocketHandler() {
        sessionsMap = new WebsocketSessionsMap();
        gson = new Gson();
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

    private int authenticate(String token) {
        return 0;
    }
}
