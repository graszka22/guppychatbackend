import javax.xml.transform.Result;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

public class MessageGateway {
    private int id;
    private int senderId;
    private int receiverId;
    private String text;
    private Date dateSent;

    private MessageGateway(ResultSet rs) throws SQLException {
        this.id = rs.getInt("id");
        this.senderId = rs.getInt("sender_id");
        this.receiverId = rs.getInt("receiver_id");
        this.text = rs.getString("text");
        this.dateSent = rs.getDate("date_sent");
    }

    public MessageGateway(int senderId, int receiverId, String text, Date dateSent) {
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.text = text;
        this.dateSent = dateSent;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getSenderId() {
        return senderId;
    }

    public void setSenderId(int senderId) {
        this.senderId = senderId;
    }

    public int getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(int receiverId) {
        this.receiverId = receiverId;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Date getDateSent() {
        return dateSent;
    }

    public void setDateSent(Date dateSent) {
        this.dateSent = dateSent;
    }

    private static final String updateStatement = "UPDATE message SET sender_id = ?, receiver_id = ?, text = ?, date_sent = ? WHERE id = ?;";
    private static final String insertStatement = "INSERT INTO message(sender_id, receiver_id, text, date_sent) VALUES (?, ?, ?, ?);";
    private static final String findStatement = "SELECT * FROM message WHERE id = ?;";
    private static final String findByUsersStatement = "SELECT * FROM message WHERE (sender_id = ? AND receiver_id = ?) OR (sender_id = ? AND receiver_id = ?);";

    public void update() throws SQLException {
        PreparedStatement statement = Registry.getPSQLDatabase().getPreparedStatement(updateStatement);
        statement.setInt(1, senderId);
        statement.setInt(2, receiverId);
        statement.setString(3, text);
        statement.setDate(4, dateSent);
        statement.executeUpdate();
    }

    public int insert() throws SQLException {
        PreparedStatement statement = Registry.getPSQLDatabase().getPreparedStatement(insertStatement);
        statement.setInt(1, senderId);
        statement.setInt(2, receiverId);
        statement.setString(3, text);
        statement.setDate(4, dateSent);
        statement.executeUpdate();
        try(ResultSet rs = statement.getGeneratedKeys()) {
            if(rs.next())
                return rs.getInt(1);
        }
        throw new SQLException("Can't insert row into database");
    }

    public static MessageGateway load(int id) throws SQLException, NoSuchElementException {
        PreparedStatement statement = Registry.getPSQLDatabase().getPreparedStatement(findStatement);
        statement.setInt(1, id);
        ResultSet resultSet = statement.executeQuery();
        if(resultSet.next())
            return new MessageGateway(resultSet);
        throw new NoSuchElementException();
    }

    public static List<MessageGateway> findByUsers(int user1, int user2) throws SQLException {
        PreparedStatement statement = Registry.getPSQLDatabase().getPreparedStatement(findByUsersStatement);
        statement.setInt(1, user1);
        statement.setInt(2, user2);
        statement.setInt(3, user2);
        statement.setInt(4, user1);
        ResultSet resultSet = statement.executeQuery();
        ArrayList<MessageGateway> result = new ArrayList<>();
        while(resultSet.next()) {
            result.add(new MessageGateway(resultSet));
        }
        return result;
    }
}
