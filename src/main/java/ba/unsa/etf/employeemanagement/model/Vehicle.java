package ba.unsa.etf.employeemanagement.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Vehicle {
    private Long id;
    private Long employeeId;
    private String vehicleMake;
    private String vehicleModel;
    private String licensePlate;
    private String vinNumber;
    private String fuelType;
    private String vehicleType;
    private Date assignedDate;
    private Date returnDate;
}
