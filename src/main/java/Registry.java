public class Registry {
    private static Registry instance = new Registry();

    private PSQLDatabase database;
    private AccountIdentityMap accountIdentityMap;

    private Registry() {
        database = new PSQLDatabase();
        accountIdentityMap = new AccountIdentityMap();
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
}
