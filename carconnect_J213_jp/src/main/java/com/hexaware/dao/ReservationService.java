package com.hexaware.dao;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import com.hexaware.entity.Reservation;
import com.hexaware.util.DatabaseContext;

public class ReservationService implements IReservationService {

    @Override
    public Reservation GetReservationById(int reservationId) {
        try (Connection connection = DatabaseContext.getDBConn()) {
            String query = "SELECT * FROM Reservation WHERE ReservationID = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, reservationId);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return extractReservationFromResultSet(resultSet);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null; // Reservation not found
    }

    @Override
    public List<Reservation> GetReservationsByCustomerId(int customerId) {
        List<Reservation> customerReservations = new ArrayList<>();
        try (Connection connection = DatabaseContext.getDBConn()) {
            String query = "SELECT * FROM Reservation WHERE CustomerID = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, customerId);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Reservation reservation = extractReservationFromResultSet(resultSet);
                customerReservations.add(reservation);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return customerReservations;
    }

    public void CreateReservation(Reservation reservationData) {
        try (Connection connection = DatabaseContext.getDBConn()) {
            String maxQuery = "SELECT MAX(ReservationID) AS MaxID FROM Reservation";
            PreparedStatement maxStatement = connection.prepareStatement(maxQuery);
            ResultSet resultSet = maxStatement.executeQuery();
            int maxReservationID = 0;
            if (resultSet.next()) {
                maxReservationID = resultSet.getInt("MaxID");
            }

            // Increment the maximum reservation ID by 1 for the new reservation
            int newReservationID = maxReservationID + 1;
            String query = "INSERT INTO Reservation (ReservationID, CustomerID, VehicleID, StartDate, EndDate, TotalCost, Status) " +
                            "VALUES (?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, newReservationID);
            preparedStatement.setInt(2, reservationData.getCustomerID());
            preparedStatement.setInt(3, reservationData.getVehicleID());
            preparedStatement.setDate(4, new java.sql.Date(reservationData.getStartDate().getTime()));
            preparedStatement.setDate(5, new java.sql.Date(reservationData.getEndDate().getTime()));
            preparedStatement.setDouble(6, reservationData.getTotalCost());
            preparedStatement.setString(7, "Confirmed");

            int rowsInserted = preparedStatement.executeUpdate();
            if (rowsInserted > 0) {
                System.out.println("Reservation created successfully.");

                // Update vehicle availability
                String updateQuery = "UPDATE Vehicle SET Availability = 0 WHERE VehicleID = ?";
                PreparedStatement updateStatement = connection.prepareStatement(updateQuery);
                updateStatement.setInt(1, reservationData.getVehicleID());
                int availabilityUpdated = updateStatement.executeUpdate();
                if (availabilityUpdated > 0) {
                    System.out.println("Vehicle availability updated successfully.");
                } else {
                    System.out.println("Failed to update vehicle availability.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void updateReservation(Reservation reservationData) {
        try (Connection connection = DatabaseContext.getDBConn()) {
            String query = "UPDATE Reservation SET CustomerID = ?, VehicleID = ?, StartDate = ?, EndDate = ?, " +
                            "TotalCost = ?, Status = ? WHERE ReservationID = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, reservationData.getCustomerID());
            preparedStatement.setInt(2, reservationData.getVehicleID());
            preparedStatement.setDate(3, new java.sql.Date(reservationData.getStartDate().getTime()));
            preparedStatement.setDate(4, new java.sql.Date(reservationData.getEndDate().getTime()));
            preparedStatement.setDouble(5, reservationData.getTotalCost());
            preparedStatement.setString(6, reservationData.getStatus());
            preparedStatement.setInt(7, reservationData.getReservationID());

            int rowsUpdated = preparedStatement.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("Reservation with ID " + reservationData.getReservationID() + " updated successfully.");
            } else {
                System.out.println("Failed to update reservation with ID " + reservationData.getReservationID() + ".");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void cancelReservation(int reservationId) {
        try (Connection connection = DatabaseContext.getDBConn()) {
            String query = "DELETE FROM Reservation WHERE ReservationID = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, reservationId);

            int rowsDeleted = preparedStatement.executeUpdate();
            if (rowsDeleted > 0) {
                System.out.println("Reservation with ID " + reservationId + " cancelled successfully.");
            } else {
                System.out.println("Reservation with ID " + reservationId + " not found.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private Reservation extractReservationFromResultSet(ResultSet resultSet) throws SQLException {
        int reservationID = resultSet.getInt("ReservationID");
        int customerID = resultSet.getInt("CustomerID");
        int vehicleID = resultSet.getInt("VehicleID");
        java.util.Date startDate = resultSet.getDate("StartDate");
        java.util.Date endDate = resultSet.getDate("EndDate");
        double totalCost = resultSet.getDouble("TotalCost");
        String status = resultSet.getString("Status");

        return new Reservation(reservationID, customerID, vehicleID, startDate, endDate, totalCost, status);
    }
    
    
    public List<Reservation> getReservationsByVehicleId(int vehicleId) {
        List<Reservation> vehicleReservations = new ArrayList<>();
        try (Connection connection = DatabaseContext.getDBConn()) {
            String query = "SELECT * FROM Reservation WHERE VehicleID = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, vehicleId);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Reservation reservation = extractReservationFromResultSet(resultSet);
                vehicleReservations.add(reservation);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return vehicleReservations;
    }

}
