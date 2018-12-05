public class Registry {
    private static Registry instance = new Registry();

    private PSQLDatabase database;
    private AccountIdentityMap accountIdentityMap;
    private MessageIdentityMap messageIdentityMap;

    private Registry() {
        database = new PSQLDatabase();
        accountIdentityMap = new AccountIdentityMap();
        messageIdentityMap = new MessageIdentityMap();
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
}
