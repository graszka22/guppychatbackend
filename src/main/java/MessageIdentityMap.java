import java.util.HashMap;

public class MessageIdentityMap {
    private HashMap<Integer, MessageGateway> hashMap = new HashMap<>();

    public void addMessage(MessageGateway message) {
        hashMap.put(message.getMessageId(), message);
    }

    public MessageGateway getMessage(int id) {
        return hashMap.get(id);
    }
}
