import java.util.HashMap;

public class AccountIdentityMap {
    private HashMap<Integer, AccountGateway> hashMap = new HashMap<>();

    public void addAccount(AccountGateway account) {
        hashMap.put(account.getUserId(), account);
    }

    public AccountGateway getAccount(int id) {
        return hashMap.get(id);
    }
}
