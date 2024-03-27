package carconnect_J213_jp;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import java.util.List;
import com.hexaware.entity.*;
import com.hexaware.dao.*;


public class CarConnectTest {
    private CustomerService customerService;
    private VehicleService vehicleService;

    @Before
    public void setUp() {
        customerService = new CustomerService();
        vehicleService = new VehicleService();
    }

    @Test
    public void testCustomerAuthenticationWithInvalidCredentials() {
        // Arrange
        String invalidUsername = "invalid_username";
        String invalidPassword = "j23";
        // Act
        boolean isAuthenticated = AuthenticationService.isValidPassword(invalidUsername);
        boolean isAuthenticated1 = AuthenticationService.isValidPassword(invalidPassword);
        // Assert
        assertFalse(isAuthenticated);
        assertFalse(isAuthenticated1);
       
    }
    @Test
    public void testUpdatingCustomerInformation() {
        // Arrange
        int customerId = 1; // Assuming customer with ID 123 exists
        String updatedAddress = "Updated Address";

        // Act
        Customer updatedCustomer = customerService.UpdateCustomerAddress(customerId, updatedAddress);

        // Assert
        assertNotNull(updatedCustomer);
        assertEquals(updatedAddress, updatedCustomer.getAddress());
    }
    
    @Test
    public void testAddingNewVehicle() {
        // Arrange
        Vehicle newVehicle = new Vehicle(1001, "Toyota", "Corolla", 2022, "Red", "ABC123", true, 50.0);

        // Act
        boolean isAdded = VehicleService.addVehicle(newVehicle);

        // Assert
        assertTrue(isAdded);
    }

    @Test
    public void testUpdatingVehicleDetails() {
        // Arrange
        int vehicleId = 1001; // Assuming vehicle with ID 1001 exists
        String updatedColor = "Blue";

        // Act
        boolean isUpdated = vehicleService.updateVehicleColor(vehicleId, updatedColor);

        // Assert
        assertTrue(isUpdated);
    }

    @Test
    public void testGettingListOfAvailableVehicles() {
        // Act
        List<Vehicle> availableVehicles = vehicleService.GetAvailableVehicles();

        // Assert
        assertNotNull(availableVehicles);
        assertFalse(availableVehicles.isEmpty());
    }

    @Test
    public void testGettingListOfAllVehicles() {
        // Act
        List<Vehicle> allVehicles = vehicleService.GetAllVehicles();

        // Assert
        assertNotNull(allVehicles);
        assertFalse(allVehicles.isEmpty());
    }
}
