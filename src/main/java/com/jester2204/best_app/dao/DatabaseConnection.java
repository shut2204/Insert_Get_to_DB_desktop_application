package com.jester2204.best_app.dao;

import java.sql.Connection;
import java.sql.DriverManager;

public class DatabaseConnection {

    private static String url;
    private static String user;
    private static String password;

    public static void setCredentials(String dbUrl, String dbUser, String dbPassword) {
        url = dbUrl;
        user = dbUser;
        password = dbPassword;
    }

    public static Connection getConnection() throws Exception {
        if (url == null || user == null || password == null) {
            throw new IllegalStateException("Database credentials not set!");
        }
        return DriverManager.getConnection(url, user, password);
    }
}
