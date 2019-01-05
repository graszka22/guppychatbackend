import com.google.gson.Gson;
import io.javalin.websocket.WsSession;

import java.sql.SQLException;

public class AddFriendWebsocketMessageHandler implements WebsocketMessageHandler {
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

    public AddFriendWebsocketMessageHandler(AddFriendCommandData data) {
        this.data = data;
    }

    @Override
    public void handleMessage(WsSession session, AccountGateway account) throws SQLException {
        AccountFinder accountFinder = new AccountFinder();
        AccountGateway accountGateway = accountFinder.find(data.friendId);
        FriendsGateway friendsGateway = new FriendsGateway(account.getUserId(), data.friendId);
        friendsGateway.insert();
        Registry.getFriendsIdentityMap().add(friendsGateway);
        String response = new Gson().toJson(new AddFriendCommandResponse(data.friendId, accountGateway.getUsername()));
        System.out.println(response);
        session.send(response);
    }
}
