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
                rs.getLong("userId"),
                rs.getString("first_name"),
                rs.getString("last_name"),
                rs.getDate("date_of_birth"),
                rs.getString("gender"),
                rs.getString("nationality"),
                rs.getString("marital_status")
        );
    }

    public Employee mapToEntity(EmployeeRequest request) {
        return new Employee(
                null,
                request.getUserId(),
                request.getFirstName(),
                request.getLastName(),
                request.getDateOfBirth(),
                request.getGender(),
                request.getNationality(),
                request.getMaritalStatus()
        );
    }
    public EmployeeResponse mapToResponse(Employee employee) {
        return EmployeeResponse.builder()
                .id(employee.getId())
                .userId(employee.getUserId())
                .firstName(employee.getFirstName())
                .lastName(employee.getLastName())
                .dateOfBirth(employee.getDateOfBirth())
                .gender(employee.getGender())
                .nationality(employee.getNationality())
                .maritalStatus(employee.getMaritalStatus())
                .build();
    }
}
