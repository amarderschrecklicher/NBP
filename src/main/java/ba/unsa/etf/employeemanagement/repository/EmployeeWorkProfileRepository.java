package ba.unsa.etf.employeemanagement.repository;

import ba.unsa.etf.employeemanagement.dto.response.EmployeeWorkProfileResponse;
import ba.unsa.etf.employeemanagement.mapper.EmployeeWorkProfileMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class EmployeeWorkProfileRepository {

    private final JdbcTemplate jdbcTemplate;
    private final EmployeeWorkProfileMapper mapper;

    /**
     * Retrieves all employee work profiles from the VW_EMPLOYEE_WORK_PROFILE view.
     *
     * Returns: List of complete HR profiles including employee identity, manager,
     * employment details, department, and lifecycle state (ACTIVE/INACTIVE).
     */
    public List<EmployeeWorkProfileResponse> findAll() {
        String sql = "SELECT " +
                "EMPLOYEE_ID, USER_ID, USERNAME, EMPLOYEE_FULL_NAME, EMAIL, PHONE_NUMBER, " +
                "BIRTH_DATE, GENDER, NATIONALITY, MARITAL_STATUS, MANAGER_ID, MANAGER_FULL_NAME, " +
                "EMPLOYMENT_ID, EMPLOYMENT_NUMBER, HIRE_DATE, TERMINATION_DATE, DAYS_SINCE_HIRE, " +
                "JOB_TITLE, EMPLOYMENT_TYPE, EMPLOYMENT_STATUS, DEPARTMENT_ID, DEPARTMENT_NAME, " +
                "LIFECYCLE_STATE " +
                "FROM VW_EMPLOYEE_WORK_PROFILE ORDER BY EMPLOYEE_ID";
        return jdbcTemplate.query(sql, mapper);
    }

    /**
     * Retrieves a single employee's work profile by employee ID.
     *
     * Returns: Complete HR profile with all employment and organizational context.
     */
    public Optional<EmployeeWorkProfileResponse> findByEmployeeId(Long employeeId) {
        String sql = "SELECT " +
                "EMPLOYEE_ID, USER_ID, USERNAME, EMPLOYEE_FULL_NAME, EMAIL, PHONE_NUMBER, " +
                "BIRTH_DATE, GENDER, NATIONALITY, MARITAL_STATUS, MANAGER_ID, MANAGER_FULL_NAME, " +
                "EMPLOYMENT_ID, EMPLOYMENT_NUMBER, HIRE_DATE, TERMINATION_DATE, DAYS_SINCE_HIRE, " +
                "JOB_TITLE, EMPLOYMENT_TYPE, EMPLOYMENT_STATUS, DEPARTMENT_ID, DEPARTMENT_NAME, " +
                "LIFECYCLE_STATE " +
                "FROM VW_EMPLOYEE_WORK_PROFILE WHERE EMPLOYEE_ID = ?";
        List<EmployeeWorkProfileResponse> results = jdbcTemplate.query(sql, mapper, employeeId);
        return results.stream().findFirst();
    }

    /**
     * Retrieves all active employees (LIFECYCLE_STATE = 'ACTIVE').
     *
     * Returns: List of HR profiles for currently employed staff.
     */
    public List<EmployeeWorkProfileResponse> findActiveEmployees() {
        String sql = "SELECT " +
                "EMPLOYEE_ID, USER_ID, USERNAME, EMPLOYEE_FULL_NAME, EMAIL, PHONE_NUMBER, " +
                "BIRTH_DATE, GENDER, NATIONALITY, MARITAL_STATUS, MANAGER_ID, MANAGER_FULL_NAME, " +
                "EMPLOYMENT_ID, EMPLOYMENT_NUMBER, HIRE_DATE, TERMINATION_DATE, DAYS_SINCE_HIRE, " +
                "JOB_TITLE, EMPLOYMENT_TYPE, EMPLOYMENT_STATUS, DEPARTMENT_ID, DEPARTMENT_NAME, " +
                "LIFECYCLE_STATE " +
                "FROM VW_EMPLOYEE_WORK_PROFILE WHERE LIFECYCLE_STATE = 'ACTIVE' ORDER BY EMPLOYEE_FULL_NAME";
        return jdbcTemplate.query(sql, mapper);
    }

    /**
     * Retrieves all employees in a specific department.
     *
     * Returns: HR profiles filtered by department_id.
     */
    public List<EmployeeWorkProfileResponse> findByDepartmentId(Long departmentId) {
        String sql = "SELECT " +
                "EMPLOYEE_ID, USER_ID, USERNAME, EMPLOYEE_FULL_NAME, EMAIL, PHONE_NUMBER, " +
                "BIRTH_DATE, GENDER, NATIONALITY, MARITAL_STATUS, MANAGER_ID, MANAGER_FULL_NAME, " +
                "EMPLOYMENT_ID, EMPLOYMENT_NUMBER, HIRE_DATE, TERMINATION_DATE, DAYS_SINCE_HIRE, " +
                "JOB_TITLE, EMPLOYMENT_TYPE, EMPLOYMENT_STATUS, DEPARTMENT_ID, DEPARTMENT_NAME, " +
                "LIFECYCLE_STATE " +
                "FROM VW_EMPLOYEE_WORK_PROFILE WHERE DEPARTMENT_ID = ? ORDER BY EMPLOYEE_FULL_NAME";
        return jdbcTemplate.query(sql, mapper, departmentId);
    }

    /**
     * Retrieves all direct reports (employees) for a specific manager.
     *
     * Returns: HR profiles of employees where MANAGER_ID matches.
     */
    public List<EmployeeWorkProfileResponse> findByManagerId(Long managerId) {
        String sql = "SELECT " +
                "EMPLOYEE_ID, USER_ID, USERNAME, EMPLOYEE_FULL_NAME, EMAIL, PHONE_NUMBER, " +
                "BIRTH_DATE, GENDER, NATIONALITY, MARITAL_STATUS, MANAGER_ID, MANAGER_FULL_NAME, " +
                "EMPLOYMENT_ID, EMPLOYMENT_NUMBER, HIRE_DATE, TERMINATION_DATE, DAYS_SINCE_HIRE, " +
                "JOB_TITLE, EMPLOYMENT_TYPE, EMPLOYMENT_STATUS, DEPARTMENT_ID, DEPARTMENT_NAME, " +
                "LIFECYCLE_STATE " +
                "FROM VW_EMPLOYEE_WORK_PROFILE WHERE MANAGER_ID = ? ORDER BY EMPLOYEE_FULL_NAME";
        return jdbcTemplate.query(sql, mapper, managerId);
    }
}

