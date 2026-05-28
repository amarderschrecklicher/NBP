package ba.unsa.etf.employeemanagement.mapper;

import ba.unsa.etf.employeemanagement.dto.response.PayrollCostOverviewResponse;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class PayrollCostOverviewMapper implements RowMapper<PayrollCostOverviewResponse> {

    @Override
    public PayrollCostOverviewResponse mapRow(ResultSet rs, int rowNum) throws SQLException {
        return PayrollCostOverviewResponse.builder()
                .financeId(rs.getLong("FINANCE_ID"))
                .employeeId(rs.getLong("EMPLOYEE_ID"))
                .username(rs.getString("USERNAME"))
                .employeeFullName(rs.getString("EMPLOYEE_FULL_NAME"))
                .jobTitle(rs.getString("JOB_TITLE"))
                .departmentName(rs.getString("DEPARTMENT_NAME"))
                .bankName(rs.getString("BANK_NAME"))
                .bankAccountNumber(rs.getString("BANK_ACCOUNT_NUMBER"))
                .iban(rs.getString("IBAN"))
                .taxNumber(rs.getString("TAX_NUMBER"))
                .salary(rs.getObject("SALARY") != null ? rs.getDouble("SALARY") : null)
                .currency(rs.getString("CURRENCY"))
                .paymentFrequency(rs.getString("PAYMENT_FREQUENCY"))
                .annualSalaryEquivalent(rs.getObject("ANNUAL_SALARY_EQUIVALENT") != null ? rs.getDouble("ANNUAL_SALARY_EQUIVALENT") : null)
                .bonusEligibleFlag(rs.getString("BONUS_ELIGIBLE_FLAG"))
                .compensationBand(rs.getString("COMPENSATION_BAND"))
                .employmentStatus(rs.getString("EMPLOYMENT_STATUS"))
                .build();
    }
}

