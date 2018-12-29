import com.google.gson.Gson;
import io.javalin.websocket.WsSession;

import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

public class SearchFriendsWebsocketMessageHandler implements WebsocketMessageHandler {
    public static class SearchFriendsCommandData {
        public String searchPhrase;
    }

    private static class SearchFriendsCommandResponse {
        final String command = "SEARCH_FRIENDS_RESPONSE";
        List<String> results;
        SearchFriendsCommandResponse(List<String> results) {
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
    public void handleMessage(WsSession session) throws SQLException {
        AccountFinder accountFinder = new AccountFinder();
        List<AccountGateway> listOfAccounts = accountFinder.searchByUsername(data.searchPhrase);
        List<String> listOfUsernames = listOfAccounts.stream().map(AccountGateway::getUsername).collect(Collectors.toList());
        String response = new Gson().toJson(new SearchFriendsCommandResponse(listOfUsernames));
        session.send(response);
    }
}
