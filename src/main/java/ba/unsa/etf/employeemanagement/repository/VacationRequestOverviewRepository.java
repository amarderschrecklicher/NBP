package ba.unsa.etf.employeemanagement.repository;

import ba.unsa.etf.employeemanagement.dto.response.VacationRequestOverviewResponse;
import ba.unsa.etf.employeemanagement.mapper.VacationRequestOverviewMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class VacationRequestOverviewRepository {

    private final JdbcTemplate jdbcTemplate;
    private final VacationRequestOverviewMapper mapper;

    /**
     * Retrieves all vacation requests from the VW_VACATION_REQUEST_OVERVIEW view.
     *
     * Returns: Complete vacation details including employee, dates, approver, total days,
     * year-to-date aggregates (requests_this_year, requested_days_this_year), and
     * workflow status (OPEN/CLOSED).
     */
    public List<VacationRequestOverviewResponse> findAll() {
        String sql = "SELECT " +
                "VACATION_ID, EMPLOYEE_ID, EMPLOYEE_FULL_NAME, EMPLOYEE_EMAIL, DEPARTMENT_NAME, " +
                "JOB_TITLE, START_DATE, END_DATE, TOTAL_DAYS, VACATION_YEAR, VACATION_MONTH, " +
                "VACATION_TYPE, STATUS, APPROVED_BY, APPROVED_BY_FULL_NAME, WORKFLOW_BUCKET, " +
                "REQUESTS_THIS_YEAR, REQUESTED_DAYS_THIS_YEAR " +
                "FROM VW_VACATION_REQUEST_OVERVIEW ORDER BY VACATION_ID DESC";
        return jdbcTemplate.query(sql, mapper);
    }

    /**
     * Retrieves a single vacation request by ID.
     *
     * Returns: Complete vacation details with duration, approver info, and year-to-date aggregates.
     */
    public Optional<VacationRequestOverviewResponse> findByVacationId(Long vacationId) {
        String sql = "SELECT " +
                "VACATION_ID, EMPLOYEE_ID, EMPLOYEE_FULL_NAME, EMPLOYEE_EMAIL, DEPARTMENT_NAME, " +
                "JOB_TITLE, START_DATE, END_DATE, TOTAL_DAYS, VACATION_YEAR, VACATION_MONTH, " +
                "VACATION_TYPE, STATUS, APPROVED_BY, APPROVED_BY_FULL_NAME, WORKFLOW_BUCKET, " +
                "REQUESTS_THIS_YEAR, REQUESTED_DAYS_THIS_YEAR " +
                "FROM VW_VACATION_REQUEST_OVERVIEW WHERE VACATION_ID = ?";
        List<VacationRequestOverviewResponse> results = jdbcTemplate.query(sql, mapper, vacationId);
        return results.stream().findFirst();
    }

    /**
     * Retrieves all vacation requests for a specific employee.
     *
     * Returns: Vacation history with aggregates showing total requests and days requested this year.
     */
    public List<VacationRequestOverviewResponse> findByEmployeeId(Long employeeId) {
        String sql = "SELECT " +
                "VACATION_ID, EMPLOYEE_ID, EMPLOYEE_FULL_NAME, EMPLOYEE_EMAIL, DEPARTMENT_NAME, " +
                "JOB_TITLE, START_DATE, END_DATE, TOTAL_DAYS, VACATION_YEAR, VACATION_MONTH, " +
                "VACATION_TYPE, STATUS, APPROVED_BY, APPROVED_BY_FULL_NAME, WORKFLOW_BUCKET, " +
                "REQUESTS_THIS_YEAR, REQUESTED_DAYS_THIS_YEAR " +
                "FROM VW_VACATION_REQUEST_OVERVIEW WHERE EMPLOYEE_ID = ? ORDER BY START_DATE DESC";
        return jdbcTemplate.query(sql, mapper, employeeId);
    }

    /**
     * Retrieves all open (pending) vacation requests.
     *
     * Returns: Requests where workflow_bucket = 'OPEN' (not yet approved/rejected).
     */
    public List<VacationRequestOverviewResponse> findOpenRequests() {
        String sql = "SELECT " +
                "VACATION_ID, EMPLOYEE_ID, EMPLOYEE_FULL_NAME, EMPLOYEE_EMAIL, DEPARTMENT_NAME, " +
                "JOB_TITLE, START_DATE, END_DATE, TOTAL_DAYS, VACATION_YEAR, VACATION_MONTH, " +
                "VACATION_TYPE, STATUS, APPROVED_BY, APPROVED_BY_FULL_NAME, WORKFLOW_BUCKET, " +
                "REQUESTS_THIS_YEAR, REQUESTED_DAYS_THIS_YEAR " +
                "FROM VW_VACATION_REQUEST_OVERVIEW WHERE WORKFLOW_BUCKET = 'OPEN' ORDER BY START_DATE";
        return jdbcTemplate.query(sql, mapper);
    }

    /**
     * Retrieves all approved vacation requests.
     *
     * Returns: Requests where STATUS = 'APPROVED' for coverage planning.
     */
    public List<VacationRequestOverviewResponse> findApprovedRequests() {
        String sql = "SELECT " +
                "VACATION_ID, EMPLOYEE_ID, EMPLOYEE_FULL_NAME, EMPLOYEE_EMAIL, DEPARTMENT_NAME, " +
                "JOB_TITLE, START_DATE, END_DATE, TOTAL_DAYS, VACATION_YEAR, VACATION_MONTH, " +
                "VACATION_TYPE, STATUS, APPROVED_BY, APPROVED_BY_FULL_NAME, WORKFLOW_BUCKET, " +
                "REQUESTS_THIS_YEAR, REQUESTED_DAYS_THIS_YEAR " +
                "FROM VW_VACATION_REQUEST_OVERVIEW WHERE STATUS = 'APPROVED' ORDER BY START_DATE";
        return jdbcTemplate.query(sql, mapper);
    }

    /**
     * Retrieves all vacation requests for a specific year.
     *
     * Returns: Vacation records filtered by vacation_year for annual compliance audits.
     */
    public List<VacationRequestOverviewResponse> findByYear(Integer year) {
        String sql = "SELECT " +
                "VACATION_ID, EMPLOYEE_ID, EMPLOYEE_FULL_NAME, EMPLOYEE_EMAIL, DEPARTMENT_NAME, " +
                "JOB_TITLE, START_DATE, END_DATE, TOTAL_DAYS, VACATION_YEAR, VACATION_MONTH, " +
                "VACATION_TYPE, STATUS, APPROVED_BY, APPROVED_BY_FULL_NAME, WORKFLOW_BUCKET, " +
                "REQUESTS_THIS_YEAR, REQUESTED_DAYS_THIS_YEAR " +
                "FROM VW_VACATION_REQUEST_OVERVIEW WHERE VACATION_YEAR = ? ORDER BY START_DATE";
        return jdbcTemplate.query(sql, mapper, year);
    }

    /**
     * Retrieves all vacation requests for a specific department.
     *
     * Returns: Department vacation coverage view for resource planning.
     */
    public List<VacationRequestOverviewResponse> findByDepartmentName(String departmentName) {
        String sql = "SELECT " +
                "VACATION_ID, EMPLOYEE_ID, EMPLOYEE_FULL_NAME, EMPLOYEE_EMAIL, DEPARTMENT_NAME, " +
                "JOB_TITLE, START_DATE, END_DATE, TOTAL_DAYS, VACATION_YEAR, VACATION_MONTH, " +
                "VACATION_TYPE, STATUS, APPROVED_BY, APPROVED_BY_FULL_NAME, WORKFLOW_BUCKET, " +
                "REQUESTS_THIS_YEAR, REQUESTED_DAYS_THIS_YEAR " +
                "FROM VW_VACATION_REQUEST_OVERVIEW WHERE DEPARTMENT_NAME = ? ORDER BY START_DATE";
        return jdbcTemplate.query(sql, mapper, departmentName);
    }

    /**
     * Retrieves vacation requests awaiting approval by a specific approver.
     *
     * Returns: Open requests where approved_by_full_name matches (approval queue).
     */
    public List<VacationRequestOverviewResponse> findPendingApprovalByApproverId(Long approverId) {
        String sql = "SELECT " +
                "VACATION_ID, EMPLOYEE_ID, EMPLOYEE_FULL_NAME, EMPLOYEE_EMAIL, DEPARTMENT_NAME, " +
                "JOB_TITLE, START_DATE, END_DATE, TOTAL_DAYS, VACATION_YEAR, VACATION_MONTH, " +
                "VACATION_TYPE, STATUS, APPROVED_BY, APPROVED_BY_FULL_NAME, WORKFLOW_BUCKET, " +
                "REQUESTS_THIS_YEAR, REQUESTED_DAYS_THIS_YEAR " +
                "FROM VW_VACATION_REQUEST_OVERVIEW WHERE WORKFLOW_BUCKET = 'OPEN' ORDER BY START_DATE";
        return jdbcTemplate.query(sql, mapper);
    }
}

