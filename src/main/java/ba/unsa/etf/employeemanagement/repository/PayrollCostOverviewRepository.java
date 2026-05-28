package ba.unsa.etf.employeemanagement.repository;

import ba.unsa.etf.employeemanagement.dto.response.PayrollCostOverviewResponse;
import ba.unsa.etf.employeemanagement.mapper.PayrollCostOverviewMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class PayrollCostOverviewRepository {

    private final JdbcTemplate jdbcTemplate;
    private final PayrollCostOverviewMapper mapper;

    /**
     * Retrieves all payroll records from the VW_PAYROLL_COST_OVERVIEW view.
     *
     * Returns: Complete payroll profiles including salary, compensation band,
     * annual salary equivalent (normalized from various payment frequencies),
     * bonus eligibility, and employment status.
     */
    public List<PayrollCostOverviewResponse> findAll() {
        String sql = "SELECT " +
                "FINANCE_ID, EMPLOYEE_ID, USERNAME, EMPLOYEE_FULL_NAME, JOB_TITLE, " +
                "DEPARTMENT_NAME, BANK_NAME, BANK_ACCOUNT_NUMBER, IBAN, TAX_NUMBER, " +
                "SALARY, CURRENCY, PAYMENT_FREQUENCY, ANNUAL_SALARY_EQUIVALENT, " +
                "BONUS_ELIGIBLE_FLAG, COMPENSATION_BAND, EMPLOYMENT_STATUS " +
                "FROM VW_PAYROLL_COST_OVERVIEW ORDER BY EMPLOYEE_FULL_NAME";
        return jdbcTemplate.query(sql, mapper);
    }

    /**
     * Retrieves payroll details for a single employee.
     *
     * Returns: Complete compensation profile including salary, payment frequency,
     * derived annual equivalent, compensation band (ENTRY_LEVEL/MID_MARKET/SENIOR_COST),
     * and bonus eligibility.
     */
    public Optional<PayrollCostOverviewResponse> findByEmployeeId(Long employeeId) {
        String sql = "SELECT " +
                "FINANCE_ID, EMPLOYEE_ID, USERNAME, EMPLOYEE_FULL_NAME, JOB_TITLE, " +
                "DEPARTMENT_NAME, BANK_NAME, BANK_ACCOUNT_NUMBER, IBAN, TAX_NUMBER, " +
                "SALARY, CURRENCY, PAYMENT_FREQUENCY, ANNUAL_SALARY_EQUIVALENT, " +
                "BONUS_ELIGIBLE_FLAG, COMPENSATION_BAND, EMPLOYMENT_STATUS " +
                "FROM VW_PAYROLL_COST_OVERVIEW WHERE EMPLOYEE_ID = ?";
        List<PayrollCostOverviewResponse> results = jdbcTemplate.query(sql, mapper, employeeId);
        return results.stream().findFirst();
    }

    /**
     * Retrieves all payroll records for a specific department.
     *
     * Returns: Aggregated payroll/cost-center data for budget and capacity planning.
     */
    public List<PayrollCostOverviewResponse> findByDepartmentId(Long departmentId) {
        String sql = "SELECT " +
                "FINANCE_ID, EMPLOYEE_ID, USERNAME, EMPLOYEE_FULL_NAME, JOB_TITLE, " +
                "DEPARTMENT_NAME, BANK_NAME, BANK_ACCOUNT_NUMBER, IBAN, TAX_NUMBER, " +
                "SALARY, CURRENCY, PAYMENT_FREQUENCY, ANNUAL_SALARY_EQUIVALENT, " +
                "BONUS_ELIGIBLE_FLAG, COMPENSATION_BAND, EMPLOYMENT_STATUS " +
                "FROM VW_PAYROLL_COST_OVERVIEW WHERE DEPARTMENT_NAME = (SELECT NAME FROM DEPARTMENT WHERE ID = ?) ORDER BY EMPLOYEE_FULL_NAME";
        return jdbcTemplate.query(sql, mapper, departmentId);
    }

    /**
     * Retrieves all employees eligible for bonus.
     *
     * Returns: Filtered payroll profiles where bonus_eligible_flag = 'YES'.
     */
    public List<PayrollCostOverviewResponse> findBonusEligible() {
        String sql = "SELECT " +
                "FINANCE_ID, EMPLOYEE_ID, USERNAME, EMPLOYEE_FULL_NAME, JOB_TITLE, " +
                "DEPARTMENT_NAME, BANK_NAME, BANK_ACCOUNT_NUMBER, IBAN, TAX_NUMBER, " +
                "SALARY, CURRENCY, PAYMENT_FREQUENCY, ANNUAL_SALARY_EQUIVALENT, " +
                "BONUS_ELIGIBLE_FLAG, COMPENSATION_BAND, EMPLOYMENT_STATUS " +
                "FROM VW_PAYROLL_COST_OVERVIEW WHERE BONUS_ELIGIBLE_FLAG = 'YES' ORDER BY EMPLOYEE_FULL_NAME";
        return jdbcTemplate.query(sql, mapper);
    }

    /**
     * Retrieves all employees in a specific compensation band.
     *
     * Returns: Filtered payroll profiles for compensation equity analysis.
     * Bands: ENTRY_LEVEL (< 2500), MID_MARKET (2500-4999), SENIOR_COST (>= 5000)
     */
    public List<PayrollCostOverviewResponse> findByCompensationBand(String compensationBand) {
        String sql = "SELECT " +
                "FINANCE_ID, EMPLOYEE_ID, USERNAME, EMPLOYEE_FULL_NAME, JOB_TITLE, " +
                "DEPARTMENT_NAME, BANK_NAME, BANK_ACCOUNT_NUMBER, IBAN, TAX_NUMBER, " +
                "SALARY, CURRENCY, PAYMENT_FREQUENCY, ANNUAL_SALARY_EQUIVALENT, " +
                "BONUS_ELIGIBLE_FLAG, COMPENSATION_BAND, EMPLOYMENT_STATUS " +
                "FROM VW_PAYROLL_COST_OVERVIEW WHERE COMPENSATION_BAND = ? ORDER BY SALARY DESC";
        return jdbcTemplate.query(sql, mapper, compensationBand);
    }

    /**
     * Retrieves employees with unspecified salary.
     *
     * Returns: Payroll records with NULL salary for data quality audits.
     */
    public List<PayrollCostOverviewResponse> findWithUnspecifiedSalary() {
        String sql = "SELECT " +
                "FINANCE_ID, EMPLOYEE_ID, USERNAME, EMPLOYEE_FULL_NAME, JOB_TITLE, " +
                "DEPARTMENT_NAME, BANK_NAME, BANK_ACCOUNT_NUMBER, IBAN, TAX_NUMBER, " +
                "SALARY, CURRENCY, PAYMENT_FREQUENCY, ANNUAL_SALARY_EQUIVALENT, " +
                "BONUS_ELIGIBLE_FLAG, COMPENSATION_BAND, EMPLOYMENT_STATUS " +
                "FROM VW_PAYROLL_COST_OVERVIEW WHERE COMPENSATION_BAND = 'UNSPECIFIED' ORDER BY EMPLOYEE_FULL_NAME";
        return jdbcTemplate.query(sql, mapper);
    }
}

