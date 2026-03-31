package ba.unsa.etf.employeemanagement.mapper;

import ba.unsa.etf.employeemanagement.dto.request.DisabilityRequest;
import ba.unsa.etf.employeemanagement.dto.response.DisabilityResponse;
import ba.unsa.etf.employeemanagement.model.Disability;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class DisabilityMapper implements RowMapper<Disability> {

    @Override
    public Disability mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new Disability(
                rs.getLong("id"),
                rs.getLong("employee_id"),
                rs.getString("disability_type"),
                rs.getString("disability_level"),
                rs.getString("description"),
                rs.getDate("registered_date")
        );
    }

    public Disability mapToEntity(DisabilityRequest request) {
        return new Disability(
                null,
                request.getEmployeeId(),
                request.getDisabilityType(),
                request.getDisabilityLevel(),
                request.getDescription(),
                request.getRegisteredDate()
        );
    }

    public DisabilityResponse mapToResponse(Disability entity) {
        return DisabilityResponse.builder()
                .id(entity.getId())
                .employeeId(entity.getEmployeeId())
                .disabilityType(entity.getDisabilityType())
                .disabilityLevel(entity.getDisabilityLevel())
                .description(entity.getDescription())
                .registeredDate(entity.getRegisteredDate())
                .build();
    }
}
