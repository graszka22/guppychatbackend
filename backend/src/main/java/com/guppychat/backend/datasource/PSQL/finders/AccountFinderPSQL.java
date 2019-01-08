package com.guppychat.backend.datasource.PSQL.finders;

import com.guppychat.backend.Registry;
import com.guppychat.backend.datasource.finders.AccountFinder;
import com.guppychat.backend.datasource.gateways.AccountGateway;
import com.guppychat.backend.datasource.PSQL.gateways.AccountGatewayPSQL;
import com.guppychat.backend.datasource.identitymaps.AccountIdentityMap;
import com.guppychat.backend.datasource.PSQL.PSQLDatabase;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

public class AccountFinderPSQL implements AccountFinder {
    private final static String findStatement = "SELECT * FROM account WHERE id = ?;";
    private final static String findByUsernameStatement = "SELECT * FROM account WHERE username = ?";
    private final static String findByTokenStatement = "SELECT * FROM account WHERE session_token = ?";
    private static final String findUsersFriendsStatement = "SELECT * FROM account INNER JOIN friends ON account.id = friends.friend_id WHERE friends.user_id = ?;";
    private static final String searchUsersByUsername = "SELECT * FROM account WHERE username LIKE ?";

    @Override
    public AccountGateway find(int id) throws SQLException, NoSuchElementException {
        AccountIdentityMap accountIdentityMap = Registry.getAccountIdentityMap();
        AccountGateway account = accountIdentityMap.getAccount(id);
        if(account != null) return account;
        PSQLDatabase database = Registry.getPSQLDatabase();
        PreparedStatement preparedStatement = database.getPreparedStatement(findStatement);
        preparedStatement.setInt(1, id);
        ResultSet resultSet = preparedStatement.executeQuery();
        if (resultSet.next())
            account = new AccountGatewayPSQL(resultSet);
        else
            throw new NoSuchElementException();
        accountIdentityMap.addAccount(account);
        return account;
    }

    @Override
    public AccountGateway findByUsername(String username) throws SQLException, NoSuchElementException {
        AccountIdentityMap accountIdentityMap = Registry.getAccountIdentityMap();
        PSQLDatabase database = Registry.getPSQLDatabase();
        PreparedStatement preparedStatement = database.getPreparedStatement(findByUsernameStatement);
        preparedStatement.setString(1, username);
        ResultSet resultSet = preparedStatement.executeQuery();
        AccountGateway account;
        if(resultSet.next())
            account = new AccountGatewayPSQL(resultSet);
        else
            throw new NoSuchElementException();
        AccountGateway loadedAccount = accountIdentityMap.getAccount(account.getId());
        if(loadedAccount == null) {
            accountIdentityMap.addAccount(account);
            return account;
        }
        return loadedAccount;
    }

    @Override
    public AccountGateway findByToken(String token) throws SQLException, NoSuchElementException {
        AccountIdentityMap accountIdentityMap = Registry.getAccountIdentityMap();
        AccountGateway account;
        PSQLDatabase database = Registry.getPSQLDatabase();
        PreparedStatement preparedStatement = database.getPreparedStatement(findByTokenStatement);
        preparedStatement.setString(1, token);
        ResultSet resultSet = preparedStatement.executeQuery();
        if(resultSet.next())
            account = new AccountGatewayPSQL(resultSet);
        else
            throw new NoSuchElementException();
        AccountGateway loadedAccount = accountIdentityMap.getAccount(account.getId());
        if(loadedAccount == null) {
            accountIdentityMap.addAccount(account);
            return account;
        }
        return loadedAccount;
    }

    @Override
    public List<AccountGateway> findUsersFriends(int userId) throws SQLException {
        PreparedStatement preparedStatement = Registry.getPSQLDatabase().getPreparedStatement(findUsersFriendsStatement);
        preparedStatement.setInt(1, userId);
        ResultSet resultSet = preparedStatement.executeQuery();
        ArrayList<AccountGateway> result = new ArrayList<>();
        while (resultSet.next()) {
            result.add(new AccountGatewayPSQL(resultSet));
        }
        return result;
    }

    @Override
    public List<AccountGateway> searchByUsername(String searchPhrase) throws SQLException {
        AccountIdentityMap accountIdentityMap = Registry.getAccountIdentityMap();
        PreparedStatement preparedStatement = Registry.getPSQLDatabase().getPreparedStatement(searchUsersByUsername);
        searchPhrase += '%';
        preparedStatement.setString(1, searchPhrase);
        ResultSet resultSet = preparedStatement.executeQuery();
        ArrayList<AccountGateway> accounts = new ArrayList<>();
        while (resultSet.next()) {
            accounts.add(new AccountGatewayPSQL(resultSet));
        }
        return accounts.stream().map(
                account -> {
                    AccountGateway accountFromIdentityMap = accountIdentityMap.getAccount(account.getId());
                    if(accountFromIdentityMap == null) {
                        accountIdentityMap.addAccount(account);
                        return account;
                    }
                    return accountFromIdentityMap;
                }
        ).collect(Collectors.toList());
    }
}
