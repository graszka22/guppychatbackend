package com.guppychat.backend.sockets.handlers;

import com.google.gson.Gson;
import com.guppychat.backend.Registry;
import com.guppychat.backend.datasource.finders.AccountFinder;
import com.guppychat.backend.sockets.Socket;
import com.guppychat.backend.sockets.SocketMessageHandler;
import com.guppychat.backend.datasource.gateways.AccountGateway;
import com.guppychat.backend.datasource.gateways.FriendsGateway;
import io.javalin.websocket.WsSession;

import java.sql.SQLException;

public class AddFriendSocketMessageHandler implements SocketMessageHandler {
    public static class AddFriendCommandData {
        public int friendId;
    }

    private static class AddFriendCommandResponse {
        final String command = "ADD_FRIEND_RESPONSE";
        public int friendId;
        String friendUsername;
        AddFriendCommandResponse(int friendId, String username) {
            this.friendId = friendId;
            this.friendUsername = username;
        }
    }

    private AddFriendCommandData data;

    public static Class<AddFriendCommandData> getDataClass() {
        return AddFriendCommandData.class;
    }

    public AddFriendSocketMessageHandler(AddFriendCommandData data) {
        this.data = data;
    }

    @Override
    public void handleMessage(Socket session, AccountGateway account) throws SQLException {
        AccountFinder accountFinder = Registry.getAccountFinderFactory().create();
        AccountGateway accountGateway = accountFinder.find(data.friendId);
        FriendsGateway friendsGateway = Registry.getFriendsGatewayFactory().create();
        friendsGateway.setUserId(account.getId());
        friendsGateway.setFriendId(data.friendId);
        friendsGateway.insert();
        Registry.getFriendsIdentityMap().add(friendsGateway);
        String response = new Gson().toJson(new AddFriendCommandResponse(data.friendId, accountGateway.getUsername()));
        System.out.println(response);
        session.send(response);
    }
}
