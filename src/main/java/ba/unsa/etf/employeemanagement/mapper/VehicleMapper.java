package ba.unsa.etf.employeemanagement.mapper;

import ba.unsa.etf.employeemanagement.dto.request.VehicleRequest;
import ba.unsa.etf.employeemanagement.dto.response.VehicleResponse;
import ba.unsa.etf.employeemanagement.model.Vehicle;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class VehicleMapper implements RowMapper<Vehicle> {

    @Override
    public Vehicle mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new Vehicle(
                rs.getLong("id"),
                rs.getLong("employee_id"),
                rs.getString("vehicle_make"),
                rs.getString("vehicle_model"),
                rs.getString("license_plate"),
                rs.getString("vin_number"),
                rs.getString("fuel_type"),
                rs.getString("vehicle_type"),
                rs.getDate("assigned_date"),
                rs.getDate("return_date")
        );
    }

    public Vehicle mapToEntity(VehicleRequest request) {
        return new Vehicle(
                null,
                request.getEmployeeId(),
                request.getVehicleMake(),
                request.getVehicleModel(),
                request.getLicensePlate(),
                request.getVinNumber(),
                request.getFuelType(),
                request.getVehicleType(),
                request.getAssignedDate(),
                request.getReturnDate()
        );
    }

    public VehicleResponse mapToResponse(Vehicle entity) {
        return VehicleResponse.builder()
                .id(entity.getId())
                .employeeId(entity.getEmployeeId())
                .vehicleMake(entity.getVehicleMake())
                .vehicleModel(entity.getVehicleModel())
                .licensePlate(entity.getLicensePlate())
                .vinNumber(entity.getVinNumber())
                .fuelType(entity.getFuelType())
                .vehicleType(entity.getVehicleType())
                .assignedDate(entity.getAssignedDate())
                .returnDate(entity.getReturnDate())
                .build();
    }
}
