package com.guppychat.backend.sockets.handlers;

import com.google.gson.Gson;
import com.guppychat.backend.Registry;
import com.guppychat.backend.datasource.identitymaps.MessageIdentityMap;
import com.guppychat.backend.sockets.WebsocketMessageHandler;
import com.guppychat.backend.sockets.WebsocketSessionsMap;
import com.guppychat.backend.datasource.gateways.MessageGateway;
import com.guppychat.backend.datasource.gateways.AccountGateway;
import io.javalin.websocket.WsSession;

import java.sql.Timestamp;
import java.sql.SQLException;

public class SendMessageWebsocketMessageHandler implements WebsocketMessageHandler {
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

    public SendMessageWebsocketMessageHandler(SendMessageCommandData data) {
        this.data = data;
    }

    @Override
    public void handleMessage(WsSession session, AccountGateway account) throws SQLException {
        MessageGateway message = Registry.getMessageGatewayFactory().create();
        message.setSenderId(data.userId);
        message.setReceiverId(data.to);
        message.setText(data.message);
        message.setDateSent(new Timestamp(System.currentTimeMillis()));
        message.insert();
        MessageIdentityMap map = Registry.getMessageIdentityMap();
        map.addMessage(message);
        WebsocketSessionsMap sessionsMap = Registry.getWebsocketSessionsMap();
        WsSession receiverSession = sessionsMap.getUserSession(data.to);
        if(receiverSession != null) {
            receiverSession.send(new Gson().toJson(new ReceiveMessageWebsocketMessage(message)));
        }
    }

    public static Class<SendMessageCommandData> getDataClass() {
        return SendMessageCommandData.class;
    }
}
