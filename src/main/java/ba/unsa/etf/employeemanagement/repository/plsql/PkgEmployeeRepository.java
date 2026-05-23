package ba.unsa.etf.employeemanagement.repository.plsql;

import ba.unsa.etf.employeemanagement.dto.plsql.AddEmployeePlsqlResult;
import ba.unsa.etf.employeemanagement.util.OraclePlsqlErrors;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.SqlOutParameter;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.sql.Types;
import java.time.LocalDate;
import java.util.Map;

@Repository
@RequiredArgsConstructor
public class PkgEmployeeRepository {

    private final JdbcTemplate jdbcTemplate;

    private SimpleJdbcCall addEmployeeCall;
    private SimpleJdbcCall updateEmploymentCall;
    private SimpleJdbcCall archiveEmployeeCall;

    @PostConstruct
    void init() {
        addEmployeeCall = new SimpleJdbcCall(jdbcTemplate)
                .withCatalogName("PKG_EMPLOYEE")
                .withProcedureName("ADD_EMPLOYEE")
                .withoutProcedureColumnMetaDataAccess()
                .declareParameters(
                        new SqlParameter("P_USER_ID", Types.NUMERIC),
                        new SqlParameter("P_GENDER", Types.VARCHAR),
                        new SqlParameter("P_NATIONALITY", Types.VARCHAR),
                        new SqlParameter("P_MARITAL_STATUS", Types.VARCHAR),
                        new SqlParameter("P_MANAGER_ID", Types.NUMERIC),
                        new SqlParameter("P_EMPLOYMENT_NUMBER", Types.VARCHAR),
                        new SqlParameter("P_HIRE_DATE", Types.DATE),
                        new SqlParameter("P_JOB_TITLE", Types.VARCHAR),
                        new SqlParameter("P_EMPLOYMENT_TYPE", Types.VARCHAR),
                        new SqlParameter("P_DEPARTMENT_ID", Types.NUMERIC),
                        new SqlOutParameter("P_EMPLOYEE_ID", Types.NUMERIC),
                        new SqlOutParameter("P_EMPLOYMENT_ID", Types.NUMERIC)
                );

        updateEmploymentCall = new SimpleJdbcCall(jdbcTemplate)
                .withCatalogName("PKG_EMPLOYEE")
                .withProcedureName("UPDATE_EMPLOYMENT")
                .withoutProcedureColumnMetaDataAccess()
                .declareParameters(
                        new SqlParameter("P_EMPLOYEE_ID", Types.NUMERIC),
                        new SqlParameter("P_STATUS", Types.VARCHAR),
                        new SqlParameter("P_DEPARTMENT_ID", Types.NUMERIC)
                );

        archiveEmployeeCall = new SimpleJdbcCall(jdbcTemplate)
                .withCatalogName("PKG_EMPLOYEE")
                .withProcedureName("ARCHIVE_EMPLOYEE")
                .withoutProcedureColumnMetaDataAccess()
                .declareParameters(
                        new SqlParameter("P_EMPLOYEE_ID", Types.NUMERIC),
                        new SqlParameter("P_HARD_DELETE", Types.NUMERIC)
                );
    }

    public AddEmployeePlsqlResult addEmployee(
            Long userId,
            String gender,
            String nationality,
            String maritalStatus,
            Long managerId,
            String employmentNumber,
            LocalDate hireDate,
            String jobTitle,
            String employmentType,
            Long departmentId
    ) {
        try {
            Map<String, Object> out = addEmployeeCall.execute(
                    userId,
                    gender,
                    nationality,
                    maritalStatus,
                    managerId,
                    employmentNumber,
                    hireDate != null ? Date.valueOf(hireDate) : null,
                    jobTitle,
                    employmentType,
                    departmentId
            );
            return new AddEmployeePlsqlResult(
                    ((Number) out.get("P_EMPLOYEE_ID")).longValue(),
                    ((Number) out.get("P_EMPLOYMENT_ID")).longValue()
            );
        } catch (DataAccessException ex) {
            throw OraclePlsqlErrors.translate(ex);
        }
    }

    public void updateEmployment(Long employeeId, String status, Long departmentId) {
        try {
            updateEmploymentCall.execute(employeeId, status, departmentId);
        } catch (DataAccessException ex) {
            throw OraclePlsqlErrors.translate(ex);
        }
    }

    public void archiveEmployee(Long employeeId, boolean hardDelete) {
        try {
            archiveEmployeeCall.execute(employeeId, hardDelete ? 1 : 0);
        } catch (DataAccessException ex) {
            throw OraclePlsqlErrors.translate(ex);
        }
    }
}
