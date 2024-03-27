package com.hexaware.dao;

import java.sql.Connection;
import java.util.Date;
import com.hexaware.entity.Customer;
import com.hexaware.util.DatabaseContext;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


public class CustomerService implements ICustomerService {

    @Override
    public Customer getCustomerById(int customerId) {
        try (Connection connection = DatabaseContext.getDBConn()) {
            String query = "SELECT * FROM Customer WHERE customerID = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, customerId);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return extractCustomerFromResultSet(resultSet);
            } else {
                return null; // Customer not found
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null; // Error occurred
        }
    }

    @Override
    public Customer getCustomerByUsername(String username) {
        try (Connection connection = DatabaseContext.getDBConn()) {
            String query = "SELECT * FROM Customer WHERE username = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, username);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return extractCustomerFromResultSet(resultSet);
            } else {
                return null; // Username not found
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null; // Error occurred
        }
    }

    @Override
    public void registerCustomer(Customer customerData) {
        try (Connection connection = DatabaseContext.getDBConn()) {
        	
            String query = "INSERT INTO Customer (customerID, firstName, lastName, email, phoneNumber, address, username, password, registrationDate) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, customerData.getCustomerID());
            preparedStatement.setString(2, customerData.getFirstName());
            preparedStatement.setString(3, customerData.getLastName());
            preparedStatement.setString(4, customerData.getEmail());
            preparedStatement.setString(5, customerData.getPhoneNumber());
            preparedStatement.setString(6, customerData.getAddress());
            preparedStatement.setString(7, customerData.getUsername());
            preparedStatement.setString(8, customerData.getPassword());
            preparedStatement.setDate(9, new java.sql.Date(customerData.getRegistrationDate().getTime()));

            int rowsInserted = preparedStatement.executeUpdate();
            if (rowsInserted > 0) {
                System.out.println("Customer inserted successfully.");
            }

            // Close resources
            preparedStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void updateCustomer(Customer customerData) {
        try (Connection connection = DatabaseContext.getDBConn()) {
            String query = "UPDATE Customer SET firstName = ?, lastName = ?, email = ?, phoneNumber = ?, address = ?, username = ?, password = ? WHERE customerID = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, customerData.getFirstName());
            preparedStatement.setString(2, customerData.getLastName());
            preparedStatement.setString(3, customerData.getEmail());
            preparedStatement.setString(4, customerData.getPhoneNumber());
            preparedStatement.setString(5, customerData.getAddress());
            preparedStatement.setString(6, customerData.getUsername());
            preparedStatement.setString(7, customerData.getPassword());
            preparedStatement.setInt(8, customerData.getCustomerID());

            int rowsUpdated = preparedStatement.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("Customer with ID " + customerData.getCustomerID() + " updated successfully.");
            } else {
                System.out.println("Failed to update customer with ID " + customerData.getCustomerID() + ".");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void deleteCustomer(int customerId) {
        try (Connection connection = DatabaseContext.getDBConn()) {
            String query = "DELETE FROM Customer WHERE customerID = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, customerId);

            int rowsDeleted = preparedStatement.executeUpdate();
            if (rowsDeleted > 0) {
                System.out.println("Customer with ID " + customerId + " deleted successfully.");
            } else {
                System.out.println("Customer with ID " + customerId + " not found.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private Customer extractCustomerFromResultSet(ResultSet resultSet) throws SQLException {
        int customerID = resultSet.getInt("customerID");
        String firstName = resultSet.getString("firstName");
        String lastName = resultSet.getString("lastName");
        String email = resultSet.getString("email");
        String phoneNumber = resultSet.getString("phoneNumber");
        String address = resultSet.getString("address");
        String username = resultSet.getString("username");
        String password = resultSet.getString("password");
        Date registrationDate = resultSet.getDate("registrationDate");

        return new Customer(customerID, firstName, lastName, email, phoneNumber, address, username, password, registrationDate);
    }

	
}
