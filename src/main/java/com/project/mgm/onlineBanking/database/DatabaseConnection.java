package com.project.mgm.onlineBanking.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    private static Connection connection;

    private DatabaseConnection() {

    }

    public static Connection getConnection() {
        try {
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3307/online_banking",
                    "root", "");
            return connection;
        }catch (SQLException e) {
            System.out.println("SQL Exception.");
        }

        return null;
    }
}
