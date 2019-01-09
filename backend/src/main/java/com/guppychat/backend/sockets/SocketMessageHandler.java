package com.guppychat.backend.sockets;

import java.sql.SQLException;

import com.guppychat.backend.datasource.gateways.AccountGateway;

public interface SocketMessageHandler {
    void handleMessage(Socket session, AccountGateway account) throws SQLException;
}
