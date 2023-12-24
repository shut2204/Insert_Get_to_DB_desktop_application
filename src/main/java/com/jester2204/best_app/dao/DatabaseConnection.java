package com.jester2204.best_app.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;

public class DatabaseConnection {

    public static Connection getConnection() throws Exception {
        Properties properties = new Properties();
        properties.load(DatabaseConnection.class.getClassLoader().getResourceAsStream("application.properties"));

        String url = properties.getProperty("database.url");
        String user = properties.getProperty("database.user");
        String password = properties.getProperty("database.password");

        return DriverManager.getConnection(url, user, password);
    }
}
