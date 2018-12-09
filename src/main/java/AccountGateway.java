import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.NoSuchElementException;

public class AccountGateway {
    private String username = "";
    private String password = "";
    private String email = "";
    private String sessionToken = "";
    private int userId;

    private AccountGateway(ResultSet rs) throws SQLException {
        this.userId = rs.getInt("user_id");
        this.username = rs.getString("username");
        this.password = rs.getString("password");
        this.email = rs.getString("email");
        this.sessionToken = rs.getString("session_token");
    }

    public AccountGateway() { }

    public AccountGateway(String username, String password, String email) {
        this.username = username;
        this.password = password;
        this.email = email;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSessionToken() {
        return sessionToken;
    }

    public void setSessionToken(String sessionToken) {
        this.sessionToken = sessionToken;
    }

    private static final String updateStatement = "UPDATE account SET username = ?, password = ?, email = ?, session_token = ? WHERE user_id = ?;";
    private static final String insertStatement = "INSERT INTO account(username, password, email, session_token) VALUES (?, ?, ?, ?);";
    private final static String findStatement = "SELECT * FROM account WHERE user_id = ?;";
    private final static String findByUsernameStatement = "SELECT * FROM account WHERE username = ?";

    public void update() throws SQLException {
        PreparedStatement pstmt = Registry.getPSQLDatabase().getPreparedStatement(updateStatement);
        pstmt.setString(1, username);
        pstmt.setString(2, password);
        pstmt.setString(3, email);
        pstmt.setString(4, sessionToken);
        pstmt.setInt(5, userId);
        pstmt.executeUpdate();
    }

    public int insert() throws SQLException {
        PreparedStatement pstmt = Registry.getPSQLDatabase().getPreparedStatement(insertStatement);
        pstmt.setString(1, username);
        pstmt.setString(2, password);
        pstmt.setString(3, email);
        pstmt.setString(4, sessionToken);
        pstmt.executeUpdate();
        try(ResultSet rs = pstmt.getGeneratedKeys()) {
            if (rs.next()) {
                this.userId = rs.getInt(1);
                return this.userId;
            }
        }
        throw new SQLException("Can't insert row into database");
    }

    public static AccountGateway load(int id) throws SQLException, NoSuchElementException {
        PSQLDatabase database = Registry.getPSQLDatabase();
        PreparedStatement preparedStatement = database.getPreparedStatement(findStatement);
        preparedStatement.setInt(1, id);
        ResultSet resultSet = preparedStatement.executeQuery();
        if (resultSet.next())
            return new AccountGateway(resultSet);
        throw new NoSuchElementException();
    }

    public static AccountGateway findByUsername(String username) throws  SQLException, NoSuchElementException {
        PSQLDatabase database = Registry.getPSQLDatabase();
        PreparedStatement preparedStatement = database.getPreparedStatement(findByUsernameStatement);
        preparedStatement.setString(1, username);
        ResultSet resultSet = preparedStatement.executeQuery();
        if(resultSet.next())
            return new AccountGateway(resultSet);
        throw new NoSuchElementException();
    }
}
