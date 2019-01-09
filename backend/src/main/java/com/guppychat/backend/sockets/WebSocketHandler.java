package com.guppychat.backend.sockets;

import io.javalin.websocket.WsHandler;

import java.util.function.Consumer;

public class WebSocketHandler implements Consumer<WsHandler> {
    private SocketMessageHandlerWrapperFactory messageHandlerFactory;

    public WebSocketHandler() {
        messageHandlerFactory = new SocketMessageHandlerWrapperFactory();
    }

    @Override
    public void accept(WsHandler wsHandler) {
        wsHandler.onMessage((session, message) -> {
            SocketMessageHandlerWrapper handler = messageHandlerFactory.create(message);
            handler.runHandler(session);
        });

        wsHandler.onClose((session, statusCode, reason) -> {

        });
    }
}
