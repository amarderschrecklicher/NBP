package ba.unsa.etf.employeemanagement.mapper;

import ba.unsa.etf.employeemanagement.dto.request.FinanceRequest;
import ba.unsa.etf.employeemanagement.dto.response.FinanceResponse;
import ba.unsa.etf.employeemanagement.model.Finance;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class FinanceMapper implements RowMapper<Finance> {

    @Override
    public Finance mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new Finance(
                rs.getLong("id"),
                rs.getLong("employee_id"),
                rs.getString("bank_name"),
                rs.getString("bank_account_number"),
                rs.getString("iban"),
                rs.getString("tax_number"),
                rs.getDouble("salary"),
                rs.getString("currency"),
                rs.getString("payment_frequency"),
                rs.getInt("bonus_eligible")
        );
    }

    public Finance mapToEntity(FinanceRequest request) {
        return new Finance(
                null,
                request.getEmployeeId(),
                request.getBankName(),
                request.getBankAccountNumber(),
                request.getIban(),
                request.getTaxNumber(),
                request.getSalary(),
                request.getCurrency(),
                request.getPaymentFrequency(),
                request.getBonusEligible()
        );
    }

    public FinanceResponse mapToResponse(Finance entity) {
        return FinanceResponse.builder()
                .id(entity.getId())
                .employeeId(entity.getEmployeeId())
                .bankName(entity.getBankName())
                .bankAccountNumber(entity.getBankAccountNumber())
                .iban(entity.getIban())
                .taxNumber(entity.getTaxNumber())
                .salary(entity.getSalary())
                .currency(entity.getCurrency())
                .paymentFrequency(entity.getPaymentFrequency())
                .bonusEligible(entity.getBonusEligible())
                .build();
    }
}
