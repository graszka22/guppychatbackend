package com.guppychat.backend.sockets;

import io.javalin.websocket.WsSession;

import java.util.HashMap;

public class SocketSessionsMap {
    private HashMap<Integer, Socket> sessionMap = new HashMap<>();

    public void addSession(Socket session, int userId) {
        sessionMap.put(userId, session);
    }

    public Socket getUserSession(int userId) {
        return sessionMap.get(userId);
    }

    public void removeSession(int userId) {
        sessionMap.remove(userId);
    }
}
