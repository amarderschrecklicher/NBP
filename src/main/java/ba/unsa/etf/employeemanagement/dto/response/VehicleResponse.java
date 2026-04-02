package ba.unsa.etf.employeemanagement.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VehicleResponse {
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
