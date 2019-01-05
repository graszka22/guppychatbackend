import com.google.gson.Gson;
import io.javalin.websocket.WsSession;

import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

public class SearchFriendsWebsocketMessageHandler implements WebsocketMessageHandler {
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

    public SearchFriendsWebsocketMessageHandler(SearchFriendsCommandData data) {
        this.data = data;
    }

    @Override
    public void handleMessage(WsSession session, AccountGateway userAccount) throws SQLException {
        AccountFinder accountFinder = new AccountFinder();
        List<AccountGateway> listOfAccounts = accountFinder.searchByUsername(data.searchPhrase);
        List<FriendData> listOfFriends = listOfAccounts
                .stream()
                .map(account -> new FriendData(account.getUserId(), account.getUsername()))
                .collect(Collectors.toList());
        String response = new Gson().toJson(new SearchFriendsCommandResponse(listOfFriends));
        session.send(response);
    }
}
