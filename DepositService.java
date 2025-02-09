package com.bankingsystem.services;

import com.bankingsystem.database.DatabaseConnection;
import com.bankingsystem.models.Deposit;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DepositService {
    private static final Logger LOGGER = Logger.getLogger(DepositService.class.getName());

    public List<Deposit> getAllDeposits() {
        List<Deposit> deposits = new ArrayList<>();
        String query = "SELECT * FROM deposits";

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                deposits.add(new Deposit(
                        rs.getInt("id"),
                        rs.getDouble("amount"),
                        rs.getInt("branch_id"),
                        rs.getInt("depositor_id")
                ));
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Ошибка при получении списка депозитов", e);
        }
        return deposits;
    }

    public Deposit getDepositById(int depositId) {
        String query = "SELECT * FROM deposits WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, depositId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Deposit(
                            rs.getInt("id"),
                            rs.getDouble("amount"),
                            rs.getInt("branch_id"),
                            rs.getInt("depositor_id")
                    );
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Ошибка при получении депозита по ID", e);
        }
        return null; // Or throw an exception, depending on your needs
    }

    public void addDeposit(double amount, int branchId, int depositorId) {
        String query = "INSERT INTO deposits (amount, branch_id, depositor_id) VALUES (?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setDouble(1, amount);
            stmt.setInt(2, branchId);
            stmt.setInt(3, depositorId);
            stmt.executeUpdate();

        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Ошибка при добавлении депозита", e);
        }
    }

    public void updateDepositAmount(int depositId, double newAmount) {
        String query = "UPDATE deposits SET amount = ? WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setDouble(1, newAmount);
            stmt.setInt(2, depositId);
            stmt.executeUpdate();

        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Ошибка при обновлении суммы депозита", e);
        }
    }

    public List<Deposit> findDepositByDepositorId(int depositorId) {
        List<Deposit> deposits = new ArrayList<>(); // Return a list, not an Optional
        String query = "SELECT * FROM deposits WHERE depositor_id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, depositorId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) { // Use while to get all matching deposits
                    deposits.add(new Deposit(
                            rs.getInt("id"),
                            rs.getDouble("amount"),
                            rs.getInt("branch_id"),
                            rs.getInt("depositor_id")
                    ));
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Ошибка при поиске депозита по вкладчику", e);
        }
        return deposits; // Return the list (can be empty)
    }


    public double getTotalDepositsAmount() {
        String query = "SELECT SUM(amount) AS total FROM deposits";

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            if (rs.next()) {
                return rs.getDouble("total");
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Ошибка при получении общей суммы депозитов", e);
        }
        return 0.0;
    }

    public void deleteDeposit(int depositId) {
        String query = "DELETE FROM deposits WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, depositId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Ошибка при удалении депозита", e);
        }
    }

}