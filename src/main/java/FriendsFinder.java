import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

public class FriendsFinder {
    public List<AccountGateway> findUsersFriends(int userId) throws SQLException {
        AccountIdentityMap accountIdentityMap = Registry.getAccountIdentityMap();
        List<AccountGateway> friends = AccountGateway.findUsersFriends(userId);
        return friends
                .stream()
                .map(friend -> {
                    AccountGateway friendFromIdentityMap = accountIdentityMap.getAccount(friend.getUserId());
                    if(friendFromIdentityMap == null) {
                        accountIdentityMap.addAccount(friend);
                        return friend;
                    }
                    return friendFromIdentityMap;
                })
                .collect(Collectors.toList());
    }
}
