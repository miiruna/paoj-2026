package com.pao.proiect.biblioteca.util;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DatabaseConnection {

    private static DatabaseConnection instance;
    private final Properties properties;
    private Connection connection;

    private DatabaseConnection() {
        this.properties = loadProperties();
    }

    public static synchronized DatabaseConnection getInstance() {
        if (instance == null) {
            instance = new DatabaseConnection();
        }
        return instance;
    }

    public synchronized Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            connection = DriverManager.getConnection(
                    properties.getProperty("db.url"),
                    properties.getProperty("db.user"),
                    properties.getProperty("db.password"));
        }
        return connection;
    }

    private Properties loadProperties() {
        Properties loadedProperties = new Properties();
        Path localPath = Path.of("resources", "db.properties");

        try (InputStream input = Files.exists(localPath)
                ? Files.newInputStream(localPath)
                : DatabaseConnection.class.getClassLoader().getResourceAsStream("db.properties")) {
            if (input == null) {
                throw new IllegalStateException("Nu exista configurarea resources/db.properties.");
            }
            loadedProperties.load(input);
            return loadedProperties;
        } catch (IOException e) {
            throw new RuntimeException("Nu s-a putut citi configurarea bazei de date.", e);
        }
    }
}
