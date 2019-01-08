package com.guppychat.backend.datasource.PSQL.finders;

import com.guppychat.backend.Registry;
import com.guppychat.backend.datasource.finders.FriendsFinder;
import com.guppychat.backend.datasource.gateways.AccountGateway;
import com.guppychat.backend.datasource.identitymaps.AccountIdentityMap;

import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

public class FriendsFinderPSQL implements FriendsFinder {
    @Override
    public List<AccountGateway> findUsersFriends(int userId) throws SQLException {
        AccountIdentityMap accountIdentityMap = Registry.getAccountIdentityMap();
        List<AccountGateway> friends = Registry.getAccountFinderFactory().create().findUsersFriends(userId);
        return friends
                .stream()
                .map(friend -> {
                    AccountGateway friendFromIdentityMap = accountIdentityMap.getAccount(friend.getId());
                    if(friendFromIdentityMap == null) {
                        accountIdentityMap.addAccount(friend);
                        return friend;
                    }
                    return friendFromIdentityMap;
                })
                .collect(Collectors.toList());
    }
}
