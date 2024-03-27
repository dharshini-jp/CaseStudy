package com.hexaware.dao;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import com.hexaware.entity.Vehicle;
import com.hexaware.util.DatabaseContext;

public class VehicleService implements IVehicleService {

    @Override
    public Vehicle getVehicleById(int vehicleId) {
        try (Connection connection = DatabaseContext.getDBConn()) {
            String query = "SELECT * FROM Vehicle WHERE VehicleID = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, vehicleId);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return extractVehicleFromResultSet(resultSet);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null; // Vehicle not found
    }

    @Override
    public List<Vehicle> getAvailableVehicles() {
        List<Vehicle> availableVehicles = new ArrayList<>();
        try (Connection connection = DatabaseContext.getDBConn()) {
            String query = "SELECT * FROM Vehicle WHERE Availability = true";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Vehicle vehicle = extractVehicleFromResultSet(resultSet);
                availableVehicles.add(vehicle);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return availableVehicles;
    }
    
    public List<Vehicle> getAllVehicles() {
        List<Vehicle> allVehicles = new ArrayList<>();
        try (Connection connection = DatabaseContext.getDBConn()) {
            String query = "SELECT * FROM Vehicle";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Vehicle vehicle = extractVehicleFromResultSet(resultSet);
                allVehicles.add(vehicle);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return allVehicles;
    }
    @Override
    public void addVehicle(Vehicle vehicleData) {
        try (Connection connection = DatabaseContext.getDBConn()) {
            String query = "INSERT INTO Vehicle (VehicleID, Model, Make, Year, Color, RegistrationNumber, Availability, DailyRate) " +
                            "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, vehicleData.getVehicleID());
            preparedStatement.setString(2, vehicleData.getModel());
            preparedStatement.setString(3, vehicleData.getMake());
            preparedStatement.setInt(4, vehicleData.getYear());
            preparedStatement.setString(5, vehicleData.getColor());
            preparedStatement.setString(6, vehicleData.getRegistrationNumber());
            preparedStatement.setBoolean(7, vehicleData.isAvailability());
            preparedStatement.setDouble(8, vehicleData.getDailyRate());

            int rowsInserted = preparedStatement.executeUpdate();
            if (rowsInserted > 0) {
                System.out.println("Vehicle inserted successfully.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    

    @Override
    public void updateVehicle(Vehicle vehicleData) {
        try (Connection connection = DatabaseContext.getDBConn()) {
            String query = "UPDATE Vehicle SET Model = ?, Make = ?, Year = ?, Color = ?, RegistrationNumber = ?, " +
                            "Availability = ?, DailyRate = ? WHERE VehicleID = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, vehicleData.getModel());
            preparedStatement.setString(2, vehicleData.getMake());
            preparedStatement.setInt(3, vehicleData.getYear());
            preparedStatement.setString(4, vehicleData.getColor());
            preparedStatement.setString(5, vehicleData.getRegistrationNumber());
            preparedStatement.setBoolean(6, vehicleData.isAvailability());
            preparedStatement.setDouble(7, vehicleData.getDailyRate());
            preparedStatement.setInt(8, vehicleData.getVehicleID());

            int rowsUpdated = preparedStatement.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("Vehicle with ID " + vehicleData.getVehicleID() + " updated successfully.");
            } else {
                System.out.println("Failed to update vehicle with ID " + vehicleData.getVehicleID() + ".");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void removeVehicle(int vehicleId) {
        try (Connection connection = DatabaseContext.getDBConn()) {
            String query = "DELETE FROM Vehicle WHERE VehicleID = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, vehicleId);

            int rowsDeleted = preparedStatement.executeUpdate();
            if (rowsDeleted > 0) {
                System.out.println("Vehicle with ID " + vehicleId + " deleted successfully.");
            } else {
                System.out.println("Vehicle with ID " + vehicleId + " not found.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Vehicle extractVehicleFromResultSet(ResultSet resultSet) throws SQLException {
        int vehicleID = resultSet.getInt("VehicleID");
        String model = resultSet.getString("Model");
        String make = resultSet.getString("Make");
        int year = resultSet.getInt("Year");
        String color = resultSet.getString("Color");
        String registrationNumber = resultSet.getString("RegistrationNumber");
        boolean availability = resultSet.getBoolean("Availability");
        double dailyRate = resultSet.getDouble("DailyRate");

        return new Vehicle(vehicleID, model, make, year, color, registrationNumber, availability, dailyRate);
    }

}
