package ba.unsa.etf.employeemanagement.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VehicleRequest {
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
