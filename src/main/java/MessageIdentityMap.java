import java.util.HashMap;

public class MessageIdentityMap {
    private HashMap<Integer, MessageGateway> hashMap = new HashMap<>();

    public void addMessage(MessageGateway message) {
        hashMap.put(message.getId(), message);
    }

    public MessageGateway getMessage(int id) {
        return hashMap.get(id);
    }
}
