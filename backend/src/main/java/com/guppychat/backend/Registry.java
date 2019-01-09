package com.guppychat.backend;

import com.guppychat.backend.datasource.PSQL.finders.AccountFinderPSQLFactory;
import com.guppychat.backend.datasource.PSQL.finders.FriendsFinderPSQLFactory;
import com.guppychat.backend.datasource.PSQL.finders.MessageFinderPSQLFactory;
import com.guppychat.backend.datasource.PSQL.gateways.AccountGatewayPSQLFactory;
import com.guppychat.backend.datasource.PSQL.gateways.FriendsGatewayPSQLFactory;
import com.guppychat.backend.datasource.PSQL.gateways.MessageGatewayPSQLFactory;
import com.guppychat.backend.datasource.finders.AccountFinderFactory;
import com.guppychat.backend.datasource.finders.FriendsFinderFactory;
import com.guppychat.backend.datasource.finders.MessageFinderFactory;
import com.guppychat.backend.datasource.identitymaps.AccountIdentityMap;
import com.guppychat.backend.datasource.identitymaps.FriendsIdentityMap;
import com.guppychat.backend.datasource.identitymaps.MessageIdentityMap;
import com.guppychat.backend.datasource.gateways.AccountGatewayFactory;
import com.guppychat.backend.datasource.gateways.MessageGatewayFactory;
import com.guppychat.backend.datasource.gateways.FriendsGatewayFactory;
import com.guppychat.backend.datasource.PSQL.PSQLDatabase;
import com.guppychat.backend.sockets.SocketSessionsMap;

public class Registry {
    private static Registry instance = new Registry();

    private PSQLDatabase database;
    private AccountIdentityMap accountIdentityMap;
    private MessageIdentityMap messageIdentityMap;
    private SocketSessionsMap socketSessionsMap;
    private FriendsIdentityMap friendsIdentityMap;

    private AccountFinderFactory accountFinderFactory = new AccountFinderPSQLFactory();
    private MessageFinderFactory messageFinderFactory = new MessageFinderPSQLFactory();
    private FriendsFinderFactory friendsFinderFactory = new FriendsFinderPSQLFactory();

    private AccountGatewayFactory accountGatewayFactory = new AccountGatewayPSQLFactory();
    private MessageGatewayFactory messageGatewayFactory = new MessageGatewayPSQLFactory();
    private FriendsGatewayFactory friendsGatewayFactory = new FriendsGatewayPSQLFactory();

    private Registry() {
        database = new PSQLDatabase();
        accountIdentityMap = new AccountIdentityMap();
        messageIdentityMap = new MessageIdentityMap();
        socketSessionsMap = new SocketSessionsMap();
        friendsIdentityMap = new FriendsIdentityMap();
    }

    private static Registry getInstance() {
        return instance;
    }

    public static PSQLDatabase getPSQLDatabase() {
        return getInstance().database;
    }

    public static AccountIdentityMap getAccountIdentityMap() {
        return getInstance().accountIdentityMap;
    }

    public static MessageIdentityMap getMessageIdentityMap() {
        return getInstance().messageIdentityMap;
    }

    public static SocketSessionsMap getWebsocketSessionsMap() {
        return getInstance().socketSessionsMap;
    }

    public static FriendsIdentityMap getFriendsIdentityMap() {
        return getInstance().friendsIdentityMap;
    }

    public static AccountFinderFactory getAccountFinderFactory() {
        return getInstance().accountFinderFactory;
    }

    public static MessageFinderFactory getMessageFinderFactory() {
        return getInstance().messageFinderFactory;
    }

    public static FriendsFinderFactory getFriendsFinderFactory() {
        return getInstance().friendsFinderFactory;
    }

    public static AccountGatewayFactory getAccountGatewayFactory() {
        return getInstance().accountGatewayFactory;
    }

    public static MessageGatewayFactory getMessageGatewayFactory() {
        return getInstance().messageGatewayFactory;
    }

    public static FriendsGatewayFactory getFriendsGatewayFactory() {
        return getInstance().friendsGatewayFactory;
    }
}
