import java.sql.SQLException;
import java.util.NoSuchElementException;

public class AccountFinder {
    public AccountGateway find(int id) throws SQLException, NoSuchElementException {
        AccountIdentityMap accountIdentityMap = Registry.getAccountIdentityMap();
        AccountGateway account = accountIdentityMap.getAccount(id);
        if(account != null) return account;
        account = AccountGateway.load(id);
        accountIdentityMap.addAccount(account);
        return account;
    }

    public AccountGateway findByUsername(String username) throws SQLException, NoSuchElementException {
        AccountIdentityMap accountIdentityMap = Registry.getAccountIdentityMap();
        AccountGateway account = AccountGateway.findByUsername(username);
        AccountGateway loadedAccount = accountIdentityMap.getAccount(account.getUserId());
        if(loadedAccount == null) {
            accountIdentityMap.addAccount(account);
            return account;
        }
        return loadedAccount;
    }

    public AccountGateway findByToken(String token) throws SQLException, NoSuchElementException {
        AccountIdentityMap accountIdentityMap = Registry.getAccountIdentityMap();
        AccountGateway account = AccountGateway.findByToken(token);
        AccountGateway loadedAccount = accountIdentityMap.getAccount(account.getUserId());
        if(loadedAccount == null) {
            accountIdentityMap.addAccount(account);
            return account;
        }
        return loadedAccount;
    }
}
