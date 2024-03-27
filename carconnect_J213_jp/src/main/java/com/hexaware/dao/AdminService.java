package com.hexaware.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.hexaware.entity.Admin;
import com.hexaware.util.DatabaseContext;

public class AdminService implements IAdminService {

    @Override
    public Admin getAdminById(int adminId) {
        try (Connection connection = DatabaseContext.getDBConn()) {
            String query = "SELECT * FROM Admin WHERE AdminID = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, adminId);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return extractAdminFromResultSet(resultSet);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null; // Admin not found
    }

    @Override
    public Admin getAdminByUsername(String username) {
        try (Connection connection = DatabaseContext.getDBConn()) {
            String query = "SELECT * FROM Admin WHERE Username = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, username);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return extractAdminFromResultSet(resultSet);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null; // Username not found
    }

    @Override
    public void registerAdmin(Admin adminData) {
        try (Connection connection = DatabaseContext.getDBConn()) {
            String query = "INSERT INTO Admin (AdminID, FirstName, LastName, Email, PhoneNumber, Username, Password, Role, JoinDate) "
                    + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, adminData.getAdminID());
            preparedStatement.setString(2, adminData.getFirstName());
            preparedStatement.setString(3, adminData.getLastName());
            preparedStatement.setString(4, adminData.getEmail());
            preparedStatement.setString(5, adminData.getPhoneNumber());
            preparedStatement.setString(6, adminData.getUsername());
            preparedStatement.setString(7, adminData.getPassword());
            preparedStatement.setString(8, adminData.getRole());
            preparedStatement.setDate(9, new java.sql.Date(adminData.getJoinDate().getTime()));

            int rowsInserted = preparedStatement.executeUpdate();
            if (rowsInserted > 0) {
                System.out.println("Admin registered successfully.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void updateAdmin(Admin adminData) {
        try (Connection connection = DatabaseContext.getDBConn()) {
            String query = "UPDATE Admin SET FirstName = ?, LastName = ?, Email = ?, PhoneNumber = ?, Username = ?, "
                    + "Password = ?, Role = ?, JoinDate = ? WHERE AdminID = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, adminData.getFirstName());
            preparedStatement.setString(2, adminData.getLastName());
            preparedStatement.setString(3, adminData.getEmail());
            preparedStatement.setString(4, adminData.getPhoneNumber());
            preparedStatement.setString(5, adminData.getUsername());
            preparedStatement.setString(6, adminData.getPassword());
            preparedStatement.setString(7, adminData.getRole());
            preparedStatement.setDate(8, new java.sql.Date(adminData.getJoinDate().getTime()));
            preparedStatement.setInt(9, adminData.getAdminID());

            int rowsUpdated = preparedStatement.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("Admin with ID " + adminData.getAdminID() + " updated successfully.");
            } else {
                System.out.println("Failed to update admin with ID " + adminData.getAdminID() + ".");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void deleteAdmin(int adminId) {
        try (Connection connection = DatabaseContext.getDBConn()) {
            String query = "DELETE FROM Admin WHERE AdminID = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, adminId);

            int rowsDeleted = preparedStatement.executeUpdate();
            if (rowsDeleted > 0) {
                System.out.println("Admin with ID " + adminId + " deleted successfully.");
            } else {
                System.out.println("Admin with ID " + adminId + " not found.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private Admin extractAdminFromResultSet(ResultSet resultSet) throws SQLException {
        int adminID = resultSet.getInt("AdminID");
        String firstName = resultSet.getString("FirstName");
        String lastName = resultSet.getString("LastName");
        String email = resultSet.getString("Email");
        String phoneNumber = resultSet.getString("PhoneNumber");
        String username = resultSet.getString("Username");
        String password = resultSet.getString("Password");
        String role = resultSet.getString("Role");
        java.util.Date joinDate = resultSet.getDate("JoinDate");

        return new Admin(adminID, firstName, lastName, email, phoneNumber, username, password, role, joinDate);
    }
}
