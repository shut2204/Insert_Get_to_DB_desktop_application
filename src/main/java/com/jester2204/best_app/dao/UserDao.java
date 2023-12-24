package com.jester2204.best_app.dao;

import java.sql.*;
import java.util.Optional;

public class UserDao {
    private final Connection connection;

    public UserDao(Connection connection) {
        this.connection = connection;
    }

    public void saveUser(User user) throws SQLException {
        String sql = "INSERT INTO users (id, name, password) VALUES (?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, user.getId());
            statement.setString(2, user.getName());
            statement.setString(3, user.getPassword());
            statement.executeUpdate();
        }
    }

    public Optional<User> getRandomUser() throws SQLException {
        String sql = "SELECT * FROM users ORDER BY RAND() LIMIT 1";
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {
            if (resultSet.next()) {
                User user = new User(
                        resultSet.getInt("id"),
                        resultSet.getString("name"),
                        resultSet.getString("password")
                );
                return Optional.of(user);
            }
            return Optional.empty();
        }
    }
}

