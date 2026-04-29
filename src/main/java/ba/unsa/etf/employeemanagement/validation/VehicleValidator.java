package ba.unsa.etf.employeemanagement.validation;

import ba.unsa.etf.employeemanagement.model.Vehicle;
import ba.unsa.etf.employeemanagement.util.enums.FuelType;
import ba.unsa.etf.employeemanagement.util.enums.VehicleType;

import java.util.Arrays;
import java.util.stream.Collectors;

public class VehicleValidator {

    public static void validate(Vehicle vehicle) {
        if (vehicle == null) {
            throw new IllegalArgumentException("Vehicle cannot be null");
        }
        if (vehicle.getEmployeeId() == null) {
            throw new IllegalArgumentException("Employee ID is required");
        }
        if (vehicle.getVehicleMake() == null || vehicle.getVehicleMake().trim().isEmpty()) {
            throw new IllegalArgumentException("Vehicle make is required");
        }
        if (vehicle.getVehicleModel() == null || vehicle.getVehicleModel().trim().isEmpty()) {
            throw new IllegalArgumentException("Vehicle model is required");
        }
        if (vehicle.getLicensePlate() == null || vehicle.getLicensePlate().trim().isEmpty()) {
            throw new IllegalArgumentException("License plate is required");
        }
        if (vehicle.getVinNumber() == null || vehicle.getVinNumber().trim().isEmpty()) {
            throw new IllegalArgumentException("VIN number is required");
        }
        if (vehicle.getFuelType() == null || !isValidFuelType(vehicle.getFuelType())) {
            throw new IllegalArgumentException("Invalid fuel type. Must be one of: " + 
                Arrays.stream(FuelType.values()).map(Enum::name).collect(Collectors.joining(", ")));
        }
        if (vehicle.getVehicleType() == null || !isValidVehicleType(vehicle.getVehicleType())) {
            throw new IllegalArgumentException("Invalid vehicle type. Must be one of: " + 
                Arrays.stream(VehicleType.values()).map(Enum::name).collect(Collectors.joining(", ")));
        }
        if (vehicle.getAssignedDate() == null) {
            throw new IllegalArgumentException("Assigned date is required");
        }
        if (vehicle.getReturnDate() != null && vehicle.getReturnDate().before(vehicle.getAssignedDate())) {
            throw new IllegalArgumentException("Return date must be after or equal to assigned date");
        }
    }

    private static boolean isValidFuelType(String fuelType) {
        try {
            FuelType.valueOf(fuelType);
            return true;
        } catch (IllegalArgumentException | NullPointerException e) {
            return false;
        }
    }

    private static boolean isValidVehicleType(String vehicleType) {
        try {
            VehicleType.valueOf(vehicleType);
            return true;
        } catch (IllegalArgumentException | NullPointerException e) {
            return false;
        }
    }
}
