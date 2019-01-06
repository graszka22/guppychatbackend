import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

public class MessageGateway {
    private int messageId;
    private int senderId;
    private int receiverId;
    private String text;
    private Timestamp dateSent;

    private MessageGateway(ResultSet rs) throws SQLException {
        this.messageId = rs.getInt("message_id");
        this.senderId = rs.getInt("sender_id");
        this.receiverId = rs.getInt("receiver_id");
        this.text = rs.getString("text");
        this.dateSent = rs.getTimestamp("date_sent");
    }

    public MessageGateway(int senderId, int receiverId, String text, Timestamp dateSent) {
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.text = text;
        this.dateSent = dateSent;
    }

    public int getMessageId() {
        return messageId;
    }

    public void setMessageId(int messageId) {
        this.messageId = messageId;
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

    public Timestamp getDateSent() {
        return dateSent;
    }

    public void setDateSent(Timestamp dateSent) {
        this.dateSent = dateSent;
    }

    private static final String updateStatement = "UPDATE message SET sender_id = ?, receiver_id = ?, text = ?, date_sent = ? WHERE message_id = ?;";
    private static final String insertStatement = "INSERT INTO message(sender_id, receiver_id, text, date_sent) VALUES (?, ?, ?, ?);";
    private static final String findStatement = "SELECT * FROM message WHERE message_id = ?;";
    private static final String findByUsersStatementWithBoundary = "SELECT * FROM message WHERE ((sender_id = ? AND receiver_id = ?) OR (sender_id = ? AND receiver_id = ?)) AND message_id < ? ORDER BY message_id DESC LIMIT 10;";
    private static final String findByUsersStatement = "SELECT * FROM message WHERE (sender_id = ? AND receiver_id = ?) OR (sender_id = ? AND receiver_id = ?) ORDER BY message_id DESC LIMIT 30;";

    public void update() throws SQLException {
        PreparedStatement statement = Registry.getPSQLDatabase().getPreparedStatement(updateStatement);
        statement.setInt(1, senderId);
        statement.setInt(2, receiverId);
        statement.setString(3, text);
        statement.setTimestamp(4, dateSent);
        statement.executeUpdate();
    }

    public int insert() throws SQLException {
        PreparedStatement statement = Registry.getPSQLDatabase().getPreparedStatement(insertStatement);
        statement.setInt(1, senderId);
        statement.setInt(2, receiverId);
        statement.setString(3, text);
        statement.setTimestamp(4, dateSent);
        statement.executeUpdate();
        try(ResultSet rs = statement.getGeneratedKeys()) {
            if(rs.next()) {
                this.messageId = rs.getInt(1);
                return this.messageId;
            }
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

    public static List<MessageGateway> findByUsers(int user1, int user2, int minMessageId) throws SQLException {
        PreparedStatement statement = Registry.getPSQLDatabase().getPreparedStatement(minMessageId == -1 ? findByUsersStatement : findByUsersStatementWithBoundary);
        statement.setInt(1, user1);
        statement.setInt(2, user2);
        statement.setInt(3, user2);
        statement.setInt(4, user1);
        if(minMessageId != -1)
            statement.setInt(5, minMessageId);
        ResultSet resultSet = statement.executeQuery();
        ArrayList<MessageGateway> result = new ArrayList<>();
        while(resultSet.next()) {
            result.add(new MessageGateway(resultSet));
        }
        return result;
    }
}
