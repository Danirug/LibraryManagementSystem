package com.library.db;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 * Singleton that gives every DAO the same MySQL connection.
 * Settings are read from src/main/resources/config/db.properties.
 */
public final class DBConnection {

    private static final String CONFIG_FILE = "/config/db.properties";
    private static DBConnection instance;

    private final String url;
    private final String username;
    private final String password;
    private Connection connection;

    private DBConnection() {
        Properties props = new Properties();
        try (InputStream in = DBConnection.class.getResourceAsStream(CONFIG_FILE)) {
            if (in == null) {
                throw new IllegalStateException("Database config not found: " + CONFIG_FILE);
            }
            props.load(in);
        } catch (IOException e) {
            throw new IllegalStateException("Could not read " + CONFIG_FILE, e);
        }
        url = props.getProperty("db.url");
        username = props.getProperty("db.username");
        password = props.getProperty("db.password");
    }

    public static DBConnection getInstance() {
        if (instance == null) {
            instance = new DBConnection();
        }
        return instance;
    }

    /** Opens the connection on first use, and reopens it if it was closed. */
    public Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            connection = DriverManager.getConnection(url, username, password);
        }
        return connection;
    }
}
