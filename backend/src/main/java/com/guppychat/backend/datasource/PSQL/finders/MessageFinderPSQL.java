package com.guppychat.backend.datasource.PSQL.finders;

import com.guppychat.backend.Registry;
import com.guppychat.backend.datasource.PSQL.gateways.MessageGatewayPSQL;
import com.guppychat.backend.datasource.finders.MessageFinder;
import com.guppychat.backend.datasource.gateways.MessageGateway;
import com.guppychat.backend.datasource.identitymaps.MessageIdentityMap;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

public class MessageFinderPSQL implements MessageFinder {
    private static final String findStatement = "SELECT * FROM message WHERE message_id = ?;";
    private static final String findByUsersStatementWithBoundary = "SELECT * FROM message WHERE ((sender_id = ? AND receiver_id = ?) OR (sender_id = ? AND receiver_id = ?)) AND message_id < ? ORDER BY message_id DESC LIMIT 10;";
    private static final String findByUsersStatement = "SELECT * FROM message WHERE (sender_id = ? AND receiver_id = ?) OR (sender_id = ? AND receiver_id = ?) ORDER BY message_id DESC LIMIT 30;";

    @Override
    public MessageGateway find(int id) throws SQLException, NoSuchElementException {
        MessageIdentityMap messageIdentityMap = Registry.getMessageIdentityMap();
        MessageGateway message = messageIdentityMap.getMessage(id);
        if(message != null) return message;
        PreparedStatement statement = Registry.getPSQLDatabase().getPreparedStatement(findStatement);
        statement.setInt(1, id);
        ResultSet resultSet = statement.executeQuery();
        if(resultSet.next())
            message = new MessageGatewayPSQL(resultSet);
        else
            throw new NoSuchElementException();
        messageIdentityMap.addMessage(message);
        return message;
    }

    @Override
    public List<MessageGateway> findByUsers(int user1, int user2, int minMessageId) throws SQLException {
        MessageIdentityMap messageIdentityMap = Registry.getMessageIdentityMap();
        PreparedStatement statement = Registry.getPSQLDatabase().getPreparedStatement(minMessageId == -1 ? findByUsersStatement : findByUsersStatementWithBoundary);
        statement.setInt(1, user1);
        statement.setInt(2, user2);
        statement.setInt(3, user2);
        statement.setInt(4, user1);
        if(minMessageId != -1)
            statement.setInt(5, minMessageId);
        ResultSet resultSet = statement.executeQuery();
        ArrayList<MessageGateway> messages = new ArrayList<>();
        while(resultSet.next()) {
            messages.add(new MessageGatewayPSQL(resultSet));
        }
        return messages
                .stream()
                .map(message -> {
                    MessageGateway messageFromIdentityMap = messageIdentityMap.getMessage(message.getId());
                    if(messageFromIdentityMap == null)  {
                        messageIdentityMap.addMessage(message);
                        return message;
                    }
                    return messageFromIdentityMap;
                })
                .collect(Collectors.toList());
    }
}
