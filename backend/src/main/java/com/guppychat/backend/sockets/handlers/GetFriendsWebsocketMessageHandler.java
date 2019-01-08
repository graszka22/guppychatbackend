package com.guppychat.backend.sockets.handlers;

import com.google.gson.Gson;
import com.guppychat.backend.Registry;
import com.guppychat.backend.datasource.finders.FriendsFinder;
import com.guppychat.backend.datasource.gateways.AccountGateway;
import com.guppychat.backend.sockets.WebsocketMessageHandler;
import io.javalin.websocket.WsSession;

import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

public class GetFriendsWebsocketMessageHandler implements WebsocketMessageHandler {
    public static class GetFriendsCommandData {
        int userId;
    }

    private static class Friend {
        int userId;
        String username;
        Friend(int userId, String username) {
            this.userId = userId;
            this.username = username;
        }
    }

    private static class GetFriendsCommandResponse {
        final String command = "GET_FRIENDS_RESPONSE";
        List<Friend> friends;

        GetFriendsCommandResponse(List<Friend> friends) {
            this.friends = friends;
        }
    }

    private GetFriendsCommandData data;

    public static Class<GetFriendsCommandData> getDataClass() {
        return GetFriendsCommandData.class;
    }

    public GetFriendsWebsocketMessageHandler(GetFriendsCommandData data) {
        this.data = data;
    }

    @Override
    public void handleMessage(WsSession session, AccountGateway userAccount) throws SQLException {
        FriendsFinder friendsFinder = Registry.getFriendsFinderFactory().create();
        List<AccountGateway> friendsAccounts = friendsFinder.findUsersFriends(data.userId);
        List<Friend> friends = friendsAccounts
                .stream()
                .map(account -> new Friend(account.getId(), account.getUsername()))
                .collect(Collectors.toList());
        String response = new Gson().toJson(new GetFriendsCommandResponse(friends));
        session.send(response);
    }
}
