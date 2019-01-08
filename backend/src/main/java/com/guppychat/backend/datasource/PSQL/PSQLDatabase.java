package com.guppychat.backend.datasource.PSQL;

import java.sql.*;

public class PSQLDatabase {
    private static final String url = "jdbc:postgresql://localhost/guppy";
    private static final String user = "postgres";
    private static final String password = "postgres";

    private Connection connection;

    public PSQLDatabase() {
        try {
            connection = DriverManager.getConnection(url, user, password);
        } catch (SQLException e) {
            throw new RuntimeException("Cannot connect to database", e);
        }
    }

    public PreparedStatement getPreparedStatement(String SQLStatement) throws SQLException {
        return connection.prepareStatement(SQLStatement, Statement.RETURN_GENERATED_KEYS);
    }
}
