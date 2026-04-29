package ba.unsa.etf.employeemanagement.dto.request;

import ba.unsa.etf.employeemanagement.util.enums.FuelType;
import ba.unsa.etf.employeemanagement.util.enums.VehicleType;
import ba.unsa.etf.employeemanagement.util.validation.ValueOfEnum;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VehicleRequest {

    @NotNull(message = "Employee ID is required")
    @Positive(message = "Employee ID must be a positive number")
    private Long employeeId;

    @NotBlank(message = "Vehicle make is required")
    @Size(max = 100, message = "Vehicle make must not exceed 100 characters")
    private String vehicleMake;

    @NotBlank(message = "Vehicle model is required")
    @Size(max = 100, message = "Vehicle model must not exceed 100 characters")
    private String vehicleModel;

    @NotBlank(message = "License plate is required")
    @Size(max = 50, message = "License plate must not exceed 50 characters")
    @Pattern(regexp = "^[A-Z0-9 -]+$", message = "License plate must contain only uppercase letters, digits, spaces, or dashes")
    private String licensePlate;

    @NotBlank(message = "VIN number is required")
    @Size(max = 100, message = "VIN number must not exceed 100 characters")
    @Pattern(regexp = "^[A-HJ-NPR-Z0-9]{17}$", message = "VIN number must be a valid 17-character alphanumeric string (excluding I, O, Q)")
    private String vinNumber;

    @NotBlank(message = "Fuel type is required")
    @ValueOfEnum(enumClass = FuelType.class, message = "Fuel type must be one of: PETROL, DIESEL, ELECTRIC, HYBRID, LPG, CNG")
    private String fuelType;

    @NotBlank(message = "Vehicle type is required")
    @ValueOfEnum(enumClass = VehicleType.class, message = "Vehicle type must be one of: SEDAN, HATCHBACK, SUV, COUPE, CONVERTIBLE, WAGON, PICKUP, VAN, MOTORCYCLE")
    private String vehicleType;

    @NotNull(message = "Assigned date is required")
    @PastOrPresent(message = "Assigned date cannot be in the future")
    private Date assignedDate;

    private Date returnDate;
}
