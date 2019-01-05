import com.google.gson.Gson;
import io.javalin.websocket.WsSession;

import java.sql.SQLException;
import java.util.List;

public class GetMessagesWebsocketMessageHandler implements WebsocketMessageHandler {
    public static class GetMessagesCommandData {
        public int userId;
        public int friendId;
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

    GetMessagesWebsocketMessageHandler(GetMessagesCommandData data) {
        this.data = data;
    }

    @Override
    public void handleMessage(WsSession session, AccountGateway account) throws SQLException {
        MessageFinder messageFinder = new MessageFinder();
        List<MessageGateway> listOfMessages = messageFinder.findByUsers(data.userId, data.friendId);
        String response = new Gson().toJson(new GetMessagesCommandResponse(listOfMessages, data.friendId));
        session.send(response);
    }
}
