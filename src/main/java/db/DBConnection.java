package db;

import lombok.Getter;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
    private static DBConnection instance;

    @Getter
    private Connection connection;

    private DBConnection() throws SQLException {
        connect();
    }

    public static synchronized DBConnection getInstance() throws SQLException {
        if (instance == null || instance.connection == null || instance.connection.isClosed()) {
            instance = new DBConnection();
        }
        return instance;
    }

    private void connect() throws SQLException {
        String URL = "jdbc:mysql://localhost:3306/clothify_shop";
        String userName = "root";
        String password = "Data1234";
        connection = DriverManager.getConnection(URL, userName, password);
        System.out.println("✅ Connected to database.");
    }

    public Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed() || !connection.isValid(2)) {
            System.out.println("🔄 Reconnecting to database...");
            connect();
        }
        return connection;
    }
}