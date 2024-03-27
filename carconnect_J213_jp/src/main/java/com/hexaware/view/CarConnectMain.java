package com.hexaware.view;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

import com.hexaware.dao.AdminService;
import com.hexaware.dao.AuthenticationService;
import com.hexaware.dao.CustomerService;
import com.hexaware.dao.ReservationService;
import com.hexaware.dao.VehicleService;
import com.hexaware.entity.Admin;
import com.hexaware.entity.Customer;
import com.hexaware.entity.Reservation;
import com.hexaware.entity.Vehicle;
import com.hexaware.util.DatabaseContext;

public class CarConnectMain {

    static CustomerService cServ = new CustomerService();
    static Customer C;
    static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {

        try {
            System.out.println("Welcome\nAre you a user or admin? : \n1.Enter 1 if user\n2.Enter 2 if admin\n3.Exit Application");
            switch (scanner.nextInt()) {
                case 1:
                    System.out.println("Welcome\nNew User? : \n1.Enter 1 if Yes\n2.Enter 2 if No\n3.Exit");
                    switch (scanner.nextInt()) {
                        case 1:
                            System.out.println("Enter the below details:");
                            try (Connection connection = DatabaseContext.getDBConn()) {
                            String maxQuery = "SELECT MAX(customerID) AS MaxID FROM Customer";
                            PreparedStatement maxStatement = connection.prepareStatement(maxQuery);
                            ResultSet resultSet = maxStatement.executeQuery();
                            int maxCustomerID = 0;
                            if (resultSet.next()) {
                            	maxCustomerID = resultSet.getInt("MaxID");
                            }
                            int customerId  = maxCustomerID + 1;
                            System.out.print("First Name: ");
                            String firstName = scanner.next();
                            System.out.print("Last Name: ");
                            String lastName = scanner.next();
                            System.out.print("Email: ");
                            String email = scanner.next();
                            System.out.print("Phone Number: ");
                            String phoneNumber = scanner.next();
                            System.out.print("Address: ");
                            String address = scanner.next();
                            System.out.print("Username: ");
                            String username = scanner.next();
                            Date registrationDate = new Date(); // Current date as registration date
                            System.out.print("Password: ");
                            String password = scanner.next();
                            if (AuthenticationService.isValidPassword(password)) {
                                C = new Customer(customerId, firstName, lastName, email, phoneNumber, address,
                                        username, password, registrationDate);
                                cServ.registerCustomer(C);
                           
                            }
                            }catch (SQLException e) {
                                e.printStackTrace();
                            }
                        case 2:
                            System.out.println("Enter the below details to login:");
                            System.out.print("Username: ");
                            String loginusername = scanner.next();
                            System.out.print("Password: ");
                            String loginpassword = scanner.next();
                            CustomerService cus1 = new CustomerService();
                            Customer c1 = cus1.getCustomerByUsername(loginusername);
                            if (c1 != null && c1.getPassword().equals(loginpassword)) {
                            	System.out.print("Welcome to Car Connect Application");
                            	while (true) {
	                                System.out.print(
	                                        "What action do you want to perform? : \n0.Enter 0 to view user detail\n1.Enter 1 to update user details\n2.Enter 2 to view available vehicles\n3.Enter 3 to view all vehicles\n4.Enter 4 to make reservation\n5.Enter 5 to update reservation\n6.Enter 6 to cancel reservation\n7.Enter 7 to end");
	                                System.out.println();
	                                switch (scanner.nextInt()) {
	                                	case 0:
	                                		System.out.println(c1.toString()); // Display admin details
                                            break;
	                                    case 1:
	                                    	System.out.print("First Name: ");
	                                        c1.setFirstName(scanner.next());
	                                        System.out.print("Last Name: ");
	                                        c1.setLastName(scanner.next());
	                                        System.out.print("Email: ");
	                                        c1.setEmail(scanner.next());
	                                        System.out.print("Phone Number: ");
	                                        c1.setPhoneNumber(scanner.next());
	                                        System.out.print("Address: ");
	                                        c1.setAddress(scanner.next());
	                                        cus1.updateCustomer(c1);
	                                        break;
	                                    case 2:
	                                        System.out.print("Find below the list of all available vehicles:");
	                                        VehicleService vserv = new VehicleService();
	                                        for (Vehicle v : vserv.getAvailableVehicles()) {
	                                            System.out.println(v.getVehicleID());
	                                            System.out.println(v.getRegistrationNumber());
	                                        }
	                                        break;
	                                    case 3:
	                                        System.out.print("Find below the list of all vehicles:");
	                                        VehicleService vserv1 = new VehicleService();
	                                        for (Vehicle v : vserv1.getAllVehicles()) {
	                                            System.out.println(v.getVehicleID());
	                                            System.out.println(v.getRegistrationNumber());
	                                        }
	                                        break;
	                                        
	                                        
	                                    case 4:
	                                    	System.out.println("Fill the below details to create a reservation:");
	                                    	ReservationService reserv = new ReservationService();
	                                    	Reservation newReservation = new Reservation();
	                                    	System.out.print("Customer ID: ");
	                                    	newReservation.setCustomerID(scanner.nextInt());
	                                    	System.out.print("Vehicle ID: ");
	                                    	int vehicleID = scanner.nextInt();
	                                    	newReservation.setVehicleID(vehicleID);
	                                    	System.out.print("Start Date (YYYY-MM-DD): ");
	                                    	String startDateStr = scanner.next();
	                                    	System.out.print("End Date (YYYY-MM-DD): ");
	                                    	String endDateStr = scanner.next();
	                                    	try {
	                                    	    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
	                                    	    Date startDate = dateFormat.parse(startDateStr);
	                                    	    Date endDate = dateFormat.parse(endDateStr);
	                                    	    newReservation.setStartDate(startDate);
	                                    	    newReservation.setEndDate(endDate);
	                                    	    
	                                    	    // Calculate the number of days
	                                    	    long diffInMillies = Math.abs(endDate.getTime() - startDate.getTime());
	                                    	    long diffInDays = diffInMillies / (1000 * 60 * 60 * 24);
	                                    	    
	                                    	    // Retrieve the daily rate of the selected vehicle from the database
	                                    	    VehicleService vehicleService = new VehicleService();
	                                    	    Vehicle vehicle = vehicleService.getVehicleById(vehicleID);
	                                    	    if (vehicle != null) {
	                                    	        // Calculate reservation cost based on the daily rate of the vehicle
	                                    	        double totalCost = diffInDays * vehicle.getDailyRate();
	                                    	        newReservation.setTotalCost(totalCost);
	                                    	        System.out.println("The Vehicle with vehicle id "+ vehicleID + " has been successfully reserved.\nTotal Cost = "+totalCost);
	                                    	    } else {
	                                    	        System.out.println("Vehicle with ID " + vehicleID + " not found.");
	                                    	        return; // Exit the reservation creation process
	                                    	    }
	                                    	    
	                                    	    reserv.CreateReservation(newReservation);
	                                    	} catch (ParseException e) {
	                                    	    System.out.println("Invalid date format. Please enter dates in the format YYYY-MM-DD.");
	                                    	}
	                                    	break;
	                                    	
	                                    	
	                                    	
	                                    case 5:
	                                        System.out.println("Enter the reservation ID you want to update: ");
	                                        ReservationService reserv1 = new ReservationService();
	                                        int updateReservationId = scanner.nextInt();
	                                        Reservation existingReservation = reserv1.GetReservationById(updateReservationId);
	                                        if (existingReservation != null) {
	                                            System.out.println("Enter the updated details:");
	                                            // Prompt user for updated details and set them in existingReservation object
	                                            reserv1.updateReservation(existingReservation);
	                                        } else {
	                                            System.out.println("Reservation with ID " + updateReservationId + " not found.");
	                                        }
	                                        break;
	                                        
	                                        
	                                        
	                                    case 6:
	                                        System.out.println("Enter the reservation ID you want to cancel: ");
	                                        int cancelReservationId = scanner.nextInt();
	                                        ReservationService reserv2 = new ReservationService();
	                                        Reservation cancelReservation = reserv2.GetReservationById(cancelReservationId);
	                                        if (cancelReservation != null) {
	                                        	reserv2.cancelReservation(cancelReservationId);
	                                        } else {
	                                            System.out.println("Reservation with ID " + cancelReservationId + " not found.");
	                                        }
	                                        break;
	                                        
	                                        
	                                    case 7:
	                                        System.out.println("Exiting Car Connect Application");
	                                        System.exit(0);
	                                    default:
	                                        System.out.println("Invalid choice.");
	                                }
                                }
                            } else {
                                System.out.println("Invalid username or password.");
                            }
                    }
                    break;
                    
                    
                case 2:
                	System.out.println("Welcome\nNew Admin? : \n1.Enter 1 if Yes\n2.Enter 2 if No\n3.Exit");
                    switch (scanner.nextInt()) {
                        case 1:
                            System.out.println("Enter the below details:");
                            System.out.print("Admin ID: ");
                            int adminId = scanner.nextInt();
                            scanner.nextLine(); // Consume newline
                            System.out.print("First Name: ");
                            String firstName = scanner.nextLine();
                            System.out.print("Last Name: ");
                            String lastName = scanner.nextLine();
                            System.out.print("Email: ");
                            String email = scanner.nextLine();
                            System.out.print("Phone Number: ");
                            String phoneNumber = scanner.nextLine();
                            System.out.print("Username: ");
                            String username = scanner.nextLine();
                            Date joinDate = new Date(); // Current date as join date
                            System.out.print("Password: ");
                            String password = scanner.nextLine();
                            if (AuthenticationService.isValidPassword(password)) {
                                Admin admin = new Admin(adminId, firstName, lastName, email, phoneNumber, username, password, "Admin", joinDate);
                                AdminService adminService = new AdminService();
								adminService.registerAdmin(admin);
                               
                            } else {
                                System.out.println("Invalid password format.");
                                break;
                            }
                        case 2:
                            System.out.println("Enter the below details to login:");
                            System.out.print("Username: ");
                            String loginUsername = scanner.next();
                            System.out.print("Password: ");
                            String loginPassword = scanner.next();
                            AdminService adminService1 = new AdminService();
                            Admin admin = adminService1.getAdminByUsername(loginUsername);
                            if (admin != null && admin.getPassword().equals(loginPassword)) {
                                System.out.println("Welcome to Car Connect Application");
                                while (true) {
                                    System.out.print(
                                            "What action do you want to perform? : \n1.Enter 1 to view admin detail\n2.Enter 2 to update admin details\n3.Enter 3 to delete admin\n4.Enter 4 to add vehicle\n5.Enter 5 to update vehicle\n6.Enter 6 to remove vehicle \n7.Enter 7 to end");
                                    switch (scanner.nextInt()) {
                                        case 1:
                                        	System.out.println(admin.toString()); // Display admin details
                                            break;
                                        case 2:
                                        	System.out.println("Enter the updated details:");
                                            System.out.print("First Name: ");
                                            String updatedFirstName = scanner.next();
                                            System.out.print("Last Name: ");
                                            String updatedLastName = scanner.next();
                                            System.out.print("Email: ");
                                            String updatedEmail = scanner.next();
                                            System.out.print("Phone Number: ");
                                            String updatedPhoneNumber = scanner.next();
                                            System.out.print("Username: ");
                                            String updatedUsername = scanner.next();
                                            System.out.print("Password: ");
                                            String updatedPassword = scanner.next();
                                            admin.setFirstName(updatedFirstName);
                                            admin.setLastName(updatedLastName);
                                            admin.setEmail(updatedEmail);
                                            admin.setPhoneNumber(updatedPhoneNumber);
                                            admin.setUsername(updatedUsername);
                                            admin.setPassword(updatedPassword);
                                            adminService1.updateAdmin(admin);
                                            break;
                                        case 3:
                                        	 System.out.print("Enter Admin ID to delete: ");
                                             int deleteAdminId = scanner.nextInt();
                                             adminService1.deleteAdmin(deleteAdminId);
                                             break;
                                        case 4:
                                            // Add Vehicle
                                            System.out.println("Fill the below details to add a new vehicle:");
                                            VehicleService vehicleService = new VehicleService();
                                            try (Connection connection = DatabaseContext.getDBConn()) {
                                            	String maxQuery = "SELECT MAX(VehicleID) AS MaxID FROM Vehicle";
                                                PreparedStatement maxStatement = connection.prepareStatement(maxQuery);
                                                ResultSet resultSet = maxStatement.executeQuery();
                                                int maxVehicleID = 0;
                                                if (resultSet.next()) {
                                                	maxVehicleID = resultSet.getInt("MaxID");
                                                }
                                                int VehicleID = maxVehicleID + 1;
                                                System.out.print("Model: ");
                                                String Model = (scanner.next());
                                                System.out.print("Make: ");
                                                String Make = (scanner.next());
                                                System.out.print("Year: ");
                                                int Year = (scanner.nextInt());
                                                scanner.nextLine(); // Consume newline
                                                System.out.print("Color: ");
                                                String Color = (scanner.next());
                                                System.out.print("Registration Number: ");
                                                String RegistrationNumber = (scanner.next());
                                                System.out.print("Availability (true/false): ");
                                                Boolean Availability = (scanner.nextBoolean());
                                                System.out.print("Daily Rate: ");
                                                double DailyRate = (scanner.nextDouble());
                                                Vehicle newVehicle = new Vehicle(VehicleID,Model,Make,Year,Color,RegistrationNumber,Availability,DailyRate);
                                                vehicleService.addVehicle(newVehicle);
                                                System.out.println("Vehicle with ID " + VehicleID + " Created.");
                                                break;
                                            }
                                            catch (SQLException e) {
                                                e.printStackTrace();
                                            }
                                            
                                        case 5:
                                            // Update Vehicle
                                            System.out.println("Enter the vehicle ID you want to update: ");
                                            int updateVehicleId = scanner.nextInt();
                                            VehicleService vehicleService2 = new VehicleService();
                                            Vehicle existingVehicle = vehicleService2.getVehicleById(updateVehicleId);
                                            if (existingVehicle != null) {
                                                System.out.println("Enter the updated details:");
                                                System.out.print("Model: ");
                                                String updatedModel = scanner.next();
                                                System.out.print("Make: ");
                                                String updatedMake = scanner.next();
                                                System.out.print("Year: ");
                                                int updatedYear = scanner.nextInt();
                                                System.out.print("Color: ");
                                                String updatedColor = scanner.next();
                                                System.out.print("Registration Number: ");
                                                String updatedRegistrationNumber = scanner.next();
                                                System.out.print("Availability (true/false): ");
                                                boolean updatedAvailability = scanner.nextBoolean();
                                                System.out.print("Daily Rate: ");
                                                double updatedDailyRate = scanner.nextDouble();
                                                existingVehicle.setModel(updatedModel);
                                                existingVehicle.setMake(updatedMake);
                                                existingVehicle.setYear(updatedYear);
                                                existingVehicle.setColor(updatedColor);
                                                existingVehicle.setRegistrationNumber(updatedRegistrationNumber);
                                                existingVehicle.setAvailability(updatedAvailability);
                                                existingVehicle.setDailyRate(updatedDailyRate);
                                                vehicleService2.updateVehicle(existingVehicle);
                                            } else {
                                                System.out.println("Vehicle with ID " + updateVehicleId + " not found.");
                                            }
                                            break;
                                        case 6:
                                            // Remove Vehicle
                                            System.out.println("Enter the vehicle ID you want to remove: ");
                                            int removeVehicleId = scanner.nextInt();
                                            VehicleService vehicleService3 = new VehicleService();
                                            vehicleService3.removeVehicle(removeVehicleId);
                                            break;

                                        case 7:
                                            System.out.println("Exiting Car Connect Application");
                                            System.exit(0);
                                        default:
                                            System.out.println("Invalid choice.");
                                    }
                                }
                            } else {
                                System.out.println("Invalid username or password.");
                            }
                            break;
                case 3:
                	System.out.println("Thanks for using Car Connect Application");
                    System.exit(0);
                    break;
                default:
                    System.out.println("Invalid choice.");
            }
        }
            } catch (NullPointerException n) {
            System.out.println("NullPointerException exists");
            n.printStackTrace();
        }
    }
}
