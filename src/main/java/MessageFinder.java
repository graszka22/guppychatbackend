import java.sql.SQLException;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

public class MessageFinder {
    public MessageGateway find(int id) throws SQLException, NoSuchElementException {
        MessageIdentityMap messageIdentityMap = Registry.getMessageIdentityMap();
        MessageGateway message = messageIdentityMap.getMessage(id);
        if(message != null) return message;
        message = MessageGateway.load(id);
        messageIdentityMap.addMessage(message);
        return message;
    }

    public List<MessageGateway> findByUsers(int user1, int user2) throws SQLException {
        MessageIdentityMap messageIdentityMap = Registry.getMessageIdentityMap();
        List<MessageGateway> messages = MessageGateway.findByUsers(user1, user2);
        return messages
                .stream()
                .map(message -> {
                    MessageGateway messageFromIdentityMap = messageIdentityMap.getMessage(message.getMessageId());
                    if(messageFromIdentityMap == null)  {
                        messageIdentityMap.addMessage(message);
                        return message;
                    }
                    return messageFromIdentityMap;
                })
                .collect(Collectors.toList());
    }
}
