package com.hexaware.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import com.hexaware.entity.Reservation;
import com.hexaware.entity.Vehicle;
import com.hexaware.util.DatabaseContext;

public class ReportGenerator {

    // Method to generate a report of reservations
    public static void generateReservationReport(List<Reservation> reservations) {
        System.out.println("Reservation Report:");
        System.out.println("-------------------");
        for (Reservation reservation : reservations) {
            System.out.println("Reservation ID: " + reservation.getReservationID());
            System.out.println("Customer ID: " + reservation.getCustomerID());
            System.out.println("Vehicle ID: " + reservation.getVehicleID());
            System.out.println("Start Date: " + reservation.getStartDate());
            System.out.println("End Date: " + reservation.getEndDate());
            System.out.println("Total Cost: " + reservation.getTotalCost());
            System.out.println("Status: " + reservation.getStatus());
            System.out.println("-------------------");
        }
    }

    // Method to generate a report of vehicles along with reservation details
    public static void generateVehicleReservationReport() {
        System.out.println("Vehicle Reservation Report:");
        System.out.println("---------------------------");
        try (Connection connection = DatabaseContext.getDBConn()) {
            String query = "SELECT * FROM Vehicle";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            ResultSet resultSet = preparedStatement.executeQuery();
            VehicleService v = new VehicleService();
            while (resultSet.next()) {
            	
                Vehicle vehicle = v.extractVehicleFromResultSet(resultSet);
                System.out.println("Vehicle ID: " + vehicle.getVehicleID());
                System.out.println("Model: " + vehicle.getModel());
                System.out.println("Make: " + vehicle.getMake());
                System.out.println("Year: " + vehicle.getYear());
                System.out.println("Color: " + vehicle.getColor());
                System.out.println("Registration Number: " + vehicle.getRegistrationNumber());
                System.out.println("Availability: " + vehicle.isAvailability());
                System.out.println("Daily Rate: " + vehicle.getDailyRate());
                ReservationService res = new ReservationService();
                List<Reservation> reservations = res.getReservationsByVehicleId(vehicle.getVehicleID());
                if (reservations.isEmpty()) {
                    System.out.println("No reservations for this vehicle.");
                } else {
                    System.out.println("Reservations:");
                    for (Reservation reservation : reservations) {
                        System.out.println("  Reservation ID: " + reservation.getReservationID());
                        System.out.println("  Customer ID: " + reservation.getCustomerID());
                        System.out.println("  Start Date: " + reservation.getStartDate());
                        System.out.println("  End Date: " + reservation.getEndDate());
                        System.out.println("  Total Cost: " + reservation.getTotalCost());
                        System.out.println("  Status: " + reservation.getStatus());
                    }
                }
                System.out.println("-------------------");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
