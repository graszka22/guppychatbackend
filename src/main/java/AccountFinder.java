import java.sql.SQLException;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

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

    public List<AccountGateway> searchByUsername(String searchPhrase) throws SQLException {
        AccountIdentityMap accountIdentityMap = Registry.getAccountIdentityMap();
        List<AccountGateway> accounts = AccountGateway.searchByUsername(searchPhrase);
        return accounts.stream().map(
                account -> {
                    AccountGateway accountFromIdentityMap = accountIdentityMap.getAccount(account.getUserId());
                    if(accountFromIdentityMap == null) {
                        accountIdentityMap.addAccount(account);
                        return account;
                    }
                    return accountFromIdentityMap;
                }
        ).collect(Collectors.toList());
    }
}
