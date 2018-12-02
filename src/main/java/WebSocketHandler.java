import com.google.gson.Gson;
import io.javalin.websocket.WsHandler;
import io.javalin.websocket.WsSession;

import java.util.function.Consumer;

public class WebSocketHandler implements Consumer<WsHandler> {
    private WebsocketSessionsMap sessionsMap;
    private Gson gson;
    private WebsocketMessageHandlerFactory messageHandlerFactory;

    public WebSocketHandler() {
        sessionsMap = new WebsocketSessionsMap();
        gson = new Gson();
        messageHandlerFactory = new WebsocketMessageHandlerFactory();
    }

    @Override
    public void accept(WsHandler wsHandler) {
        wsHandler.onMessage((session, message) -> {
            WebsocketMessage jsonMessage = gson.fromJson(message, WebsocketMessage.class);
            int userId = authenticate(jsonMessage.token);
            WebsocketMessageHandler handler = messageHandlerFactory.create(jsonMessage);
        });

        wsHandler.onClose((session, statusCode, reason) -> {

        });
    }

    private int authenticate(String token) {
        return 0;
    }
}
