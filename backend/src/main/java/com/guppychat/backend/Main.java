package com.guppychat.backend;

import com.guppychat.backend.sockets.WebSocketHandler;
import com.guppychat.backend.sockets.handlers.LoginRequestHandler;
import com.guppychat.backend.sockets.handlers.RegisterRequestHandler;
import io.javalin.Javalin;

public class Main {
    public static void main(String[] args) {
        Javalin app = Javalin.create();
        app.enableCorsForOrigin("http://localhost:300");
        app.start(7000);

        app.post("/register", new RegisterRequestHandler());
        app.post("/login", new LoginRequestHandler());
        Javalin.create().ws("/chat", new WebSocketHandler()).start(7070);
    }
}