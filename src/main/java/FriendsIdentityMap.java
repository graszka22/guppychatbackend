import javafx.util.Pair;

import java.util.HashMap;

public class FriendsIdentityMap {
    private static class Friends {
        int userId;
        int friendId;

        Friends(int userId, int friendId) {
            this.userId = userId;
            this.friendId = friendId;
        }

        @Override
        public boolean equals(Object o) {
            if(!(o instanceof  Friends)) return false;
            Friends friend = (Friends) o;
            return userId == friend.userId && friendId == friend.friendId;
        }

        @Override
        public int hashCode() {
            return 31*userId+friendId;
        }
    }

    private HashMap<Friends, FriendsGateway> map = new HashMap<>();

    public FriendsGateway get(int userId, int friendId) {
        return map.get(new Friends(userId, friendId));
    }

    public void add(FriendsGateway friendsGateway) {
        map.put(new Friends(friendsGateway.getUserId(), friendsGateway.getFriendId()), friendsGateway);
    }

}
