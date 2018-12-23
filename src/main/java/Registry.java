public class Registry {
    private static Registry instance = new Registry();

    private PSQLDatabase database;
    private AccountIdentityMap accountIdentityMap;
    private MessageIdentityMap messageIdentityMap;
    private WebsocketSessionsMap websocketSessionsMap;
    private FriendsIdentityMap friendsIdentityMap;

    private Registry() {
        database = new PSQLDatabase();
        accountIdentityMap = new AccountIdentityMap();
        messageIdentityMap = new MessageIdentityMap();
        websocketSessionsMap = new WebsocketSessionsMap();
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

    public static WebsocketSessionsMap getWebsocketSessionsMap() {
        return getInstance().websocketSessionsMap;
    }

    public static FriendsIdentityMap getFriendsIdentityMap() {
        return getInstance().friendsIdentityMap;
    }
}
