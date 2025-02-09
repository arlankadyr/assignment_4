package com.bankingsystem.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    private static final String URL = "jdbc:mysql://localhost:3306/banking_system";
    private static final String USER = "user";
    private static final String PASSWORD = "12345";

    public static Connection getConnection() throws SQLException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            return DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (ClassNotFoundException e) {
            throw new SQLException("MySQL JDBC Driver not found", e);
        }
    }
    // Test method (optional, but very useful!)
    public static void main(String[] args) {
        try {
            Connection connection = getConnection();
            System.out.println("Connection successful!");
            connection.close(); // Close the connection when you're done
        } catch (SQLException e) {
            System.err.println("Connection failed: " + e.getMessage());
            e.printStackTrace(); // Print the full stack trace for debugging
        }
    }
}