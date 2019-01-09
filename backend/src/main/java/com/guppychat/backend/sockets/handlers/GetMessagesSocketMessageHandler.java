package com.guppychat.backend.sockets.handlers;

import com.google.gson.Gson;
import com.guppychat.backend.Registry;
import com.guppychat.backend.datasource.finders.MessageFinder;
import com.guppychat.backend.sockets.Socket;
import com.guppychat.backend.sockets.SocketMessageHandler;
import com.guppychat.backend.datasource.gateways.AccountGateway;
import com.guppychat.backend.datasource.gateways.MessageGateway;

import java.sql.SQLException;
import java.util.List;

public class GetMessagesSocketMessageHandler implements SocketMessageHandler {
    public static class GetMessagesCommandData {
        public int userId;
        public int friendId;
        public int minMessageId;
    }

    private static class GetMessagesCommandResponse {
        final String command = "GET_MESSAGES_RESPONSE";
        List<MessageGateway> messages;
        int friendId;
        GetMessagesCommandResponse(List<MessageGateway> messages, int friendId) {
            this.messages = messages;
            this.friendId = friendId;
        }
    }

    private GetMessagesCommandData data;

    public static Class<GetMessagesCommandData> getDataClass() {
        return GetMessagesCommandData.class;
    }

    public GetMessagesSocketMessageHandler(GetMessagesCommandData data) {
        this.data = data;
    }

    @Override
    public void handleMessage(Socket session, AccountGateway account) throws SQLException {
        MessageFinder messageFinder = Registry.getMessageFinderFactory().create();
        List<MessageGateway> listOfMessages = messageFinder.findByUsers(data.userId, data.friendId, data.minMessageId);
        String response = new Gson().toJson(new GetMessagesCommandResponse(listOfMessages, data.friendId));
        session.send(response);
    }
}
