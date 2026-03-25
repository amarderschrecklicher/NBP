package ba.unsa.etf.employeemanagement.mapper;

import ba.unsa.etf.employeemanagement.dto.request.EmploymentRequest;
import ba.unsa.etf.employeemanagement.dto.response.EmploymentResponse;
import ba.unsa.etf.employeemanagement.model.Employment;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class EmploymentMapper implements RowMapper<Employment> {
    @Override
    public Employment mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new Employment(
                rs.getLong("id"),
                rs.getLong("employee_id"),
                rs.getString("employment_number"),
                rs.getDate("hire_date"),
                rs.getDate("termination_date"),
                rs.getString("job_title"),
                rs.getString("employment_type"),
                rs.getString("status"),
                rs.getLong("department_id")
        );
    }

    public Employment mapToEntity(EmploymentRequest request) {
        return new Employment(
                null,
                request.getEmployeeId(),
                request.getNumber(),
                request.getHireDate(),
                request.getTerminationDate(),
                request.getJobTitle(),
                request.getEmploymentType(),
                request.getStatus(),
                request.getDepartmentId()
        );
    }
    public EmploymentResponse mapToResponse(Employment employment) {
        return EmploymentResponse.builder()
                .id(employment.getId())
                .employeeId(employment.getEmployeeId())
                .number(employment.getNumber())
                .hireDate(employment.getHireDate())
                .terminationDate(employment.getTerminationDate())
                .jobTitle(employment.getJobTitle())
                .employmentType(employment.getEmploymentType())
                .status(employment.getStatus())
                .departmentId(employment.getDepartmentId())
                .build();
    }
}
