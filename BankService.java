package com.bankingsystem.services;

import com.bankingsystem.database.DatabaseConnection;
import com.bankingsystem.models.Bank;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class BankService {
    private static final Logger LOGGER = Logger.getLogger(BankService.class.getName());

    public List<Bank> getAllBanks() {
        List<Bank> banks = new ArrayList<>();
        String query = "SELECT * FROM banks";

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                banks.add(new Bank(
                        rs.getInt("id"),
                        rs.getString("name")
                ));
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Ошибка при получении списка банков", e);
        }
        return banks;
    }

    public void addBank(String name) {
        String query = "INSERT INTO banks (name) VALUES (?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, name);
            stmt.executeUpdate();

        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Ошибка при добавлении банка", e);
        }
    }

    public Bank findBankById(int id) { // Changed to return Bank, not Optional<Bank>
        String query = "SELECT * FROM banks WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Bank( // Return the Bank object directly
                            rs.getInt("id"),
                            rs.getString("name")
                    );
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Ошибка при поиске банка по ID", e);
        }
        return null; // Return null if not found
    }

    public void updateBank(int bankId, String newName) {
        String query = "UPDATE banks SET name = ? WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, newName);
            stmt.setInt(2, bankId);
            int rowsUpdated = stmt.executeUpdate();
            if (rowsUpdated == 0) {
                LOGGER.log(Level.WARNING, "Попытка обновления несуществующего банка (ID: " + bankId + ")");
            }

        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Ошибка при обновлении названия банка", e);
        }
    }

    public void deleteBank(int id) {
        String query = "DELETE FROM banks WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, id);
            int rowsDeleted = stmt.executeUpdate();
            if (rowsDeleted == 0) {
                LOGGER.log(Level.WARNING, "Попытка удаления несуществующего банка (ID: " + id + ")");
            }

        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Ошибка при удалении банка", e);
        }
    }
}