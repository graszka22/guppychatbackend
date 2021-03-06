package com.guppychat.backend.sockets.handlers;

import com.google.gson.Gson;
import com.guppychat.backend.Registry;
import com.guppychat.backend.datasource.finders.AccountFinder;
import com.guppychat.backend.datasource.gateways.AccountGateway;
import com.guppychat.backend.sockets.Socket;
import com.guppychat.backend.sockets.SocketMessageHandler;

import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

public class SearchFriendsSocketMessageHandler implements SocketMessageHandler {
    public static class SearchFriendsCommandData {
        public String searchPhrase;
    }

    private static class FriendData {
        String username;
        int userId;
        FriendData(int userId, String username) {
            this.userId = userId;
            this.username = username;
        }
    }

    private static class SearchFriendsCommandResponse {
        final String command = "SEARCH_FRIENDS_RESPONSE";
        List<FriendData> results;
        SearchFriendsCommandResponse(List<FriendData> results) {
            this.results = results;
        }
    }

    private SearchFriendsCommandData data;

    public static Class<SearchFriendsCommandData> getDataClass() {
        return SearchFriendsCommandData.class;
    }

    public SearchFriendsSocketMessageHandler(SearchFriendsCommandData data) {
        this.data = data;
    }

    @Override
    public void handleMessage(Socket session, AccountGateway userAccount) throws SQLException {
        AccountFinder accountFinder = Registry.getAccountFinderFactory().create();
        List<AccountGateway> listOfAccounts = accountFinder.searchByUsername(data.searchPhrase);
        List<FriendData> listOfFriends = listOfAccounts
                .stream()
                .map(account -> new FriendData(account.getId(), account.getUsername()))
                .collect(Collectors.toList());
        String response = new Gson().toJson(new SearchFriendsCommandResponse(listOfFriends));
        session.send(response);
    }
}
