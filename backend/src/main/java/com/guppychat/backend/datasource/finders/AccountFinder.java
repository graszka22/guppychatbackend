package com.guppychat.backend.datasource.finders;

import java.sql.SQLException;
import java.util.List;
import java.util.NoSuchElementException;

import com.guppychat.backend.datasource.gateways.AccountGateway;

public interface AccountFinder {
    AccountGateway find(int id) throws SQLException, NoSuchElementException;
    AccountGateway findByUsername(String username) throws SQLException, NoSuchElementException;
    AccountGateway findByToken(String token) throws SQLException, NoSuchElementException;
    List<AccountGateway> findUsersFriends(int userId) throws SQLException;
    List<AccountGateway> searchByUsername(String searchPhrase) throws SQLException;
}
