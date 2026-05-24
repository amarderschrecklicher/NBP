package ba.unsa.etf.employeemanagement.mapper;

import ba.unsa.etf.employeemanagement.dto.response.EmployeeWorkProfileResponse;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class EmployeeWorkProfileMapper implements RowMapper<EmployeeWorkProfileResponse> {

    @Override
    public EmployeeWorkProfileResponse mapRow(ResultSet rs, int rowNum) throws SQLException {
        return EmployeeWorkProfileResponse.builder()
                .employeeId(rs.getLong("EMPLOYEE_ID"))
                .userId(rs.getLong("USER_ID"))
                .username(rs.getString("USERNAME"))
                .employeeFullName(rs.getString("EMPLOYEE_FULL_NAME"))
                .email(rs.getString("EMAIL"))
                .phoneNumber(rs.getString("PHONE_NUMBER"))
                .birthDate(rs.getDate("BIRTH_DATE"))
                .gender(rs.getString("GENDER"))
                .nationality(rs.getString("NATIONALITY"))
                .maritalStatus(rs.getString("MARITAL_STATUS"))
                .managerId(rs.getObject("MANAGER_ID") != null ? rs.getLong("MANAGER_ID") : null)
                .managerFullName(rs.getString("MANAGER_FULL_NAME"))
                .employmentId(rs.getObject("EMPLOYMENT_ID") != null ? rs.getLong("EMPLOYMENT_ID") : null)
                .employmentNumber(rs.getString("EMPLOYMENT_NUMBER"))
                .hireDate(rs.getDate("HIRE_DATE"))
                .terminationDate(rs.getDate("TERMINATION_DATE"))
                .daysSinceHire(rs.getObject("DAYS_SINCE_HIRE") != null ? rs.getLong("DAYS_SINCE_HIRE") : null)
                .jobTitle(rs.getString("JOB_TITLE"))
                .employmentType(rs.getString("EMPLOYMENT_TYPE"))
                .employmentStatus(rs.getString("EMPLOYMENT_STATUS"))
                .departmentId(rs.getObject("DEPARTMENT_ID") != null ? rs.getLong("DEPARTMENT_ID") : null)
                .departmentName(rs.getString("DEPARTMENT_NAME"))
                .lifecycleState(rs.getString("LIFECYCLE_STATE"))
                .build();
    }
}

