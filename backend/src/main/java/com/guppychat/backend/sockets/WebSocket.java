package com.guppychat.backend.sockets;

import io.javalin.websocket.WsSession;

public class WebSocket implements Socket {
    private WsSession session;

    public WebSocket(WsSession session) {
        this.session = session;
    }

    @Override
    public void send(String s) {
        this.session.send(s);
    }
}
