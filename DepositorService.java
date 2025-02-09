package com.bankingsystem.services;

import com.bankingsystem.database.DatabaseConnection;
import com.bankingsystem.models.Depositor;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DepositorService {
    private static final Logger LOGGER = Logger.getLogger(DepositorService.class.getName());

    public List<Depositor> getAllDepositors() {
        List<Depositor> depositors = new ArrayList<>();
        String query = "SELECT * FROM depositors";

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                depositors.add(new Depositor(
                        rs.getInt("id"),
                        rs.getString("full_name"),
                        rs.getString("contact")
                ));
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Ошибка при получении списка вкладчиков", e);
        }
        return depositors;
    }

    public void addDepositor(String fullName, String contact) {
        String query = "INSERT INTO depositors (full_name, contact) VALUES (?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, fullName);
            stmt.setString(2, contact);
            stmt.executeUpdate();

        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Ошибка при добавлении вкладчика", e);
        }
    }

    public Depositor getDepositorById(int depositorId) { // Changed to get by ID
        String query = "SELECT * FROM depositors WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, depositorId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Depositor(
                            rs.getInt("id"),
                            rs.getString("full_name"),
                            rs.getString("contact")
                    );
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Ошибка при поиске вкладчика по ID", e);
        }
        return null; // Return null if not found
    }
    // Remove findDepositorByName - getDepositorById is more useful and standard

    public void updateDepositor(int depositorId, String newFullName, String newContact) {
        String query = "UPDATE depositors SET full_name = ?, contact = ? WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, newFullName);
            stmt.setString(2, newContact);
            stmt.setInt(3, depositorId);
            stmt.executeUpdate();

        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Ошибка при обновлении данных вкладчика", e);
        }
    }

    public void deleteDepositor(int depositorId) {
        String query = "DELETE FROM depositors WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, depositorId);
            stmt.executeUpdate();

        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Ошибка при удалении вкладчика", e);
        }
    }
}