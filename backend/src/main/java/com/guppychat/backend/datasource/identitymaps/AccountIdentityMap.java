package com.guppychat.backend.datasource.identitymaps;

import java.util.HashMap;
import com.guppychat.backend.datasource.gateways.AccountGateway;

public class AccountIdentityMap {
    private HashMap<Integer, AccountGateway> hashMap = new HashMap<>();

    public void addAccount(AccountGateway account) {
        hashMap.put(account.getId(), account);
    }

    public AccountGateway getAccount(int id) {
        return hashMap.get(id);
    }
}
