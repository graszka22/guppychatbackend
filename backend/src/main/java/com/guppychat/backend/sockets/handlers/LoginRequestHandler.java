package com.guppychat.backend.sockets.handlers;

import com.google.gson.Gson;
import com.guppychat.backend.Registry;
import com.guppychat.backend.datasource.finders.AccountFinder;
import com.guppychat.backend.datasource.gateways.AccountGateway;
import io.javalin.Context;
import io.javalin.Handler;
import org.jetbrains.annotations.NotNull;
import org.mindrot.jbcrypt.BCrypt;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.NoSuchElementException;

public class LoginRequestHandler implements Handler {
    private static class LoginResponse {
        String token;
        int userId;
        LoginResponse(String token, int userId) {
            this.token = token;
            this.userId = userId;
        }
    }

    @Override
    public void handle(@NotNull Context context) throws Exception {
        String username = context.formParam("username");
        String password = context.formParam("password");
        AccountFinder accountFinder = Registry.getAccountFinderFactory().create();
        AccountGateway account;
        try {
            account = accountFinder.findByUsername(username);
        } catch(NoSuchElementException e) {
            context.status(403);
            return;
        }
        if(BCrypt.checkpw(password, account.getPassword())) {
            String token = generateToken();
            account.setSessionToken(token);
            account.update();
            context.result(new Gson().toJson(new LoginResponse(token, account.getId())));
            context.status(200);
            return;
        }
        context.status(403);
    }

    private String generateToken() {
        byte[] bytes = new byte[16];
        try {
            SecureRandom.getInstanceStrong().nextBytes(bytes);
            StringBuilder builder = new StringBuilder();
            for(byte b : bytes) {
                builder.append(String.format("%02x", b));
            }
            return builder.toString();
        } catch(NoSuchAlgorithmException e) {
            throw new RuntimeException("Can't generate session token", e);
        }
    }
}
