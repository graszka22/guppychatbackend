package com.guppychat.backend.sockets.handlers;

import com.google.gson.Gson;
import com.guppychat.backend.Registry;
import com.guppychat.backend.datasource.gateways.AccountGateway;
import com.guppychat.backend.sockets.Socket;
import com.guppychat.backend.sockets.SocketMessageHandler;

import java.sql.SQLException;

public class LogoutSocketMessageHandler implements SocketMessageHandler {
    private static class LogoutCommandResponse {
        final String command = "LOGOUT_RESPONSE";
    }
    @Override
    public void handleMessage(Socket session, AccountGateway account) throws SQLException {
        account.setSessionToken(null);
        account.update();
        session.send(new Gson().toJson(new LogoutCommandResponse()));
        Registry.getWebsocketSessionsMap().removeSession(account.getId());
    }
}
