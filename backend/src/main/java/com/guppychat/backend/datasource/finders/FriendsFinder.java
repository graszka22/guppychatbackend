package com.guppychat.backend.datasource.finders;

import java.sql.SQLException;
import java.util.List;

import com.guppychat.backend.datasource.gateways.AccountGateway;

public interface FriendsFinder {
    List<AccountGateway> findUsersFriends(int userId) throws SQLException;
}
