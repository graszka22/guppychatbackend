package com.guppychat.backend.datasource.finders;

import java.sql.SQLException;
import java.util.List;
import java.util.NoSuchElementException;

import com.guppychat.backend.datasource.gateways.MessageGateway;

public interface MessageFinder {
    MessageGateway find(int id) throws SQLException, NoSuchElementException;
    List<MessageGateway> findByUsers(int user1, int user2, int minMessageId) throws SQLException;
}
