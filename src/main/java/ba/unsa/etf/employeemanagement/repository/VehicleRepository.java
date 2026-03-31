package ba.unsa.etf.employeemanagement.repository;

import ba.unsa.etf.employeemanagement.mapper.VehicleMapper;
import ba.unsa.etf.employeemanagement.model.Vehicle;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class VehicleRepository {

    private final JdbcTemplate jdbcTemplate;
    private final VehicleMapper vehicleMapper;

    public List<Vehicle> findAll() {
        String sql = "SELECT * FROM vehicle";
        return jdbcTemplate.query(sql, vehicleMapper);
    }

    public Optional<Vehicle> findById(Long id) {
        String sql = "SELECT * FROM vehicle WHERE id = ?";
        return jdbcTemplate.query(sql, vehicleMapper, id).stream().findFirst();
    }

    public Long save(Vehicle vehicle) {
        String sql = "INSERT INTO vehicle (employee_id, vehicle_make, vehicle_model, license_plate, vin_number, fuel_type, vehicle_type, assigned_date, return_date) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(conn -> {
            PreparedStatement ps = conn.prepareStatement(sql, new String[]{"id"});
            ps.setLong(1, vehicle.getEmployeeId());
            ps.setString(2, vehicle.getVehicleMake());
            ps.setString(3, vehicle.getVehicleModel());
            ps.setString(4, vehicle.getLicensePlate());
            ps.setString(5, vehicle.getVinNumber());
            ps.setString(6, vehicle.getFuelType());
            ps.setString(7, vehicle.getVehicleType());
            ps.setDate(8, vehicle.getAssignedDate() != null ? new java.sql.Date(vehicle.getAssignedDate().getTime()) : null);
            ps.setDate(9, vehicle.getReturnDate() != null ? new java.sql.Date(vehicle.getReturnDate().getTime()) : null);
            return ps;
        }, keyHolder);

        return keyHolder.getKey().longValue();
    }

    public int update(Long id, Vehicle vehicle) {
        String sql = "UPDATE vehicle SET employee_id=?, vehicle_make=?, vehicle_model=?, license_plate=?, vin_number=?, fuel_type=?, vehicle_type=?, assigned_date=?, return_date=? WHERE id=?";
        return jdbcTemplate.update(sql,
                vehicle.getEmployeeId(),
                vehicle.getVehicleMake(),
                vehicle.getVehicleModel(),
                vehicle.getLicensePlate(),
                vehicle.getVinNumber(),
                vehicle.getFuelType(),
                vehicle.getVehicleType(),
                vehicle.getAssignedDate(),
                vehicle.getReturnDate(),
                id);
    }

    public int deleteById(Long id) {
        return jdbcTemplate.update("DELETE FROM vehicle WHERE id=?", id);
    }
}
