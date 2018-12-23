import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

public class FriendsGateway {
    private int userId;
    private int friendId;

    private FriendsGateway(ResultSet rs) throws SQLException {
        this.userId = rs.getInt("user_id");
        this.friendId = rs.getInt("friend_id");
    }

    public FriendsGateway() {}

    public FriendsGateway(int userId, int friendId) {
        this.userId = userId;
        this.friendId = friendId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getFriendId() {
        return friendId;
    }

    public void setFriendId(int friendId) {
        this.friendId = friendId;
    }

    private static final String insertStatement = "INSERT INTO friends(user_id, friend_id) VALUES (?, ?);";
    private static final String findStatement = "SELECT * FROM friends WHERE user_id = ? AND friend_id = ?;";

    public void insert() throws SQLException {
        PSQLDatabase database = Registry.getPSQLDatabase();
        PreparedStatement preparedStatement = database.getPreparedStatement(insertStatement);
        preparedStatement.setInt(1, userId);
        preparedStatement.setInt(2, friendId);
        preparedStatement.executeQuery();
    }

    public static FriendsGateway load(int userId, int friendId) throws SQLException {
        PSQLDatabase database = Registry.getPSQLDatabase();
        PreparedStatement preparedStatement = database.getPreparedStatement(findStatement);
        preparedStatement.setInt(1, userId);
        preparedStatement.setInt(2, friendId);
        ResultSet resultSet = preparedStatement.executeQuery();
        if (resultSet.next())
            return new FriendsGateway(resultSet);
        throw new NoSuchElementException();
    }
}
