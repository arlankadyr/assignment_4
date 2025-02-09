package com.bankingsystem.services;

import com.bankingsystem.database.DatabaseConnection;
import com.bankingsystem.models.Branch;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class BranchService {
    private static final Logger LOGGER = Logger.getLogger(BranchService.class.getName());

    public List<Branch> getAllBranches() {
        List<Branch> branches = new ArrayList<>();
        String query = "SELECT * FROM branches";

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                branches.add(new Branch(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getInt("bank_id")
                ));
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Ошибка при получении списка филиалов", e);
        }
        return branches;
    }

    public void addBranch(String name, int bankId) {
        String query = "INSERT INTO branches (name, bank_id) VALUES (?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, name);
            stmt.setInt(2, bankId);
            stmt.executeUpdate();

        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Ошибка при добавлении филиала", e);
        }
    }

    public Branch findBranchById(int id) { // Changed to return Branch, not Optional<Branch>
        String query = "SELECT * FROM branches WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Branch( // Return Branch object directly
                            rs.getInt("id"),
                            rs.getString("name"),
                            rs.getInt("bank_id")
                    );
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Ошибка при поиске филиала по ID", e);
        }
        return null; // Return null if not found
    }

    public void updateBranch(int branchId, String newName, int newBankId) { // Added newBankId
        String query = "UPDATE branches SET name = ?, bank_id = ? WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, newName);
            stmt.setInt(2, newBankId); // Set the new bank_id
            stmt.setInt(3, branchId);
            stmt.executeUpdate();

        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Ошибка при обновлении названия филиала", e);
        }
    }

    public void deleteBranch(int id) {
        String query = "DELETE FROM branches WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, id);
            stmt.executeUpdate();

        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Ошибка при удалении филиала", e);
        }
    }
}