package com.guppychat.backend.datasource.identitymaps;

import java.util.HashMap;
import com.guppychat.backend.datasource.gateways.MessageGateway;

public class MessageIdentityMap {
    private HashMap<Integer, com.guppychat.backend.datasource.gateways.MessageGateway> hashMap = new HashMap<>();

    public void addMessage(MessageGateway message) {
        hashMap.put(message.getId(), message);
    }

    public MessageGateway getMessage(int id) {
        return hashMap.get(id);
    }
}
