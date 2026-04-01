package ba.unsa.etf.employeemanagement.mapper;

import ba.unsa.etf.employeemanagement.dto.request.EmployeeRequest;
import ba.unsa.etf.employeemanagement.dto.response.EmployeeResponse;
import ba.unsa.etf.employeemanagement.model.Employee;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class EmployeeMapper implements RowMapper<Employee> {
    @Override
    public Employee mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new Employee(
                rs.getLong("id"),
                rs.getLong("user_id"),
                rs.getString("gender"),
                rs.getString("nationality"),
                rs.getString("marital_status")
        );
    }

    public Employee mapToEntity(EmployeeRequest request) {
        return new Employee(
                null,
                null,
                request.getGender(),
                request.getNationality(),
                request.getMaritalStatus()
        );
    }

    public EmployeeResponse mapToResponse(Employee entity) {
        return EmployeeResponse.builder()
                .id(entity.getId())
                .userId(entity.getUserId())
                .gender(entity.getGender())
                .nationality(entity.getNationality())
                .maritalStatus(entity.getMaritalStatus())
                .build();
    }
}
