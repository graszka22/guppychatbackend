package com.guppychat.backend.sockets;

import io.javalin.websocket.WsSession;

import java.sql.SQLException;

import com.guppychat.backend.datasource.gateways.AccountGateway;

public interface WebsocketMessageHandler {
    void handleMessage(WsSession session, AccountGateway account) throws SQLException;
}
