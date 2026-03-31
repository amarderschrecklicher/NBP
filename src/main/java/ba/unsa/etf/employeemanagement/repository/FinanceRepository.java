package ba.unsa.etf.employeemanagement.repository;

import ba.unsa.etf.employeemanagement.mapper.FinanceMapper;
import ba.unsa.etf.employeemanagement.model.Finance;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class FinanceRepository {

    private final JdbcTemplate jdbcTemplate;
    private final FinanceMapper financeMapper;

    public List<Finance> findAll() {
        String sql = "SELECT id, employee_id, bank_name, bank_account_number, iban, tax_number, salary, currency, payment_frequency, bonus_eligible FROM finance";
        return jdbcTemplate.query(sql, financeMapper);
    }

    public Optional<Finance> findById(Long id) {
        String sql = "SELECT id, employee_id, bank_name, bank_account_number, iban, tax_number, salary, currency, payment_frequency, bonus_eligible FROM finance WHERE id = ?";
        List<Finance> results = jdbcTemplate.query(sql, financeMapper, id);
        return results.stream().findFirst();
    }

    public Long save(Finance finance) {
        String sql = "INSERT INTO finance (employee_id, bank_name, bank_account_number, iban, tax_number, salary, currency, payment_frequency, bonus_eligible) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, new String[]{"id"});
            ps.setLong(1, finance.getEmployeeId());
            ps.setString(2, finance.getBankName());
            ps.setString(3, finance.getBankAccountNumber());
            ps.setString(4, finance.getIban());
            ps.setString(5, finance.getTaxNumber());

            if (finance.getSalary() != null) {
                ps.setDouble(6, finance.getSalary());
            } else {
                ps.setNull(6, java.sql.Types.NUMERIC);
            }

            ps.setString(7, finance.getCurrency());
            ps.setString(8, finance.getPaymentFrequency());

            if (finance.getBonusEligible() != null) {
                ps.setInt(9, finance.getBonusEligible());
            } else {
                ps.setNull(9, java.sql.Types.NUMERIC);
            }

            return ps;
        }, keyHolder);

        return keyHolder.getKey().longValue();
    }

    public int update(Long id, Finance finance) {
        String sql = "UPDATE finance SET employee_id = ?, bank_name = ?, bank_account_number = ?, iban = ?, tax_number = ?, salary = ?, currency = ?, payment_frequency = ?, bonus_eligible = ? WHERE id = ?";
        return jdbcTemplate.update(sql,
                finance.getEmployeeId(),
                finance.getBankName(),
                finance.getBankAccountNumber(),
                finance.getIban(),
                finance.getTaxNumber(),
                finance.getSalary(),
                finance.getCurrency(),
                finance.getPaymentFrequency(),
                finance.getBonusEligible(),
                id);
    }

    public int deleteById(Long id) {
        String sql = "DELETE FROM finance WHERE id = ?";
        return jdbcTemplate.update(sql, id);
    }
}
