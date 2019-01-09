package com.guppychat.backend.sockets.handlers;

import com.google.gson.Gson;
import com.guppychat.backend.Registry;
import com.guppychat.backend.datasource.identitymaps.MessageIdentityMap;
import com.guppychat.backend.sockets.Socket;
import com.guppychat.backend.sockets.SocketMessageHandler;
import com.guppychat.backend.sockets.SocketSessionsMap;
import com.guppychat.backend.datasource.gateways.MessageGateway;
import com.guppychat.backend.datasource.gateways.AccountGateway;
import io.javalin.websocket.WsSession;

import java.sql.Timestamp;
import java.sql.SQLException;

public class SendMessageSocketMessageHandler implements SocketMessageHandler {
    public static class SendMessageCommandData {
        public int userId;
        public int to;
        public String message;
    }

    private static class ReceiveMessageWebsocketMessage {
        MessageGateway data;
        String command = "RECEIVE_MESSAGE";
        ReceiveMessageWebsocketMessage(MessageGateway data) {
            this.data = data;
        }
    }

    private SendMessageCommandData data;

    public SendMessageSocketMessageHandler(SendMessageCommandData data) {
        this.data = data;
    }

    @Override
    public void handleMessage(Socket session, AccountGateway account) throws SQLException {
        MessageGateway message = Registry.getMessageGatewayFactory().create();
        message.setSenderId(data.userId);
        message.setReceiverId(data.to);
        message.setText(data.message);
        message.setDateSent(new Timestamp(System.currentTimeMillis()));
        message.insert();
        MessageIdentityMap map = Registry.getMessageIdentityMap();
        map.addMessage(message);
        SocketSessionsMap sessionsMap = Registry.getWebsocketSessionsMap();
        Socket receiverSession = sessionsMap.getUserSession(data.to);
        if(receiverSession != null) {
            receiverSession.send(new Gson().toJson(new ReceiveMessageWebsocketMessage(message)));
        }
    }

    public static Class<SendMessageCommandData> getDataClass() {
        return SendMessageCommandData.class;
    }
}
