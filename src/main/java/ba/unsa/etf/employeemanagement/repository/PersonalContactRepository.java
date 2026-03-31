package ba.unsa.etf.employeemanagement.repository;

import ba.unsa.etf.employeemanagement.mapper.PersonalContactMapper;
import ba.unsa.etf.employeemanagement.model.PersonalContact;
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
public class PersonalContactRepository {
    private final JdbcTemplate jdbcTemplate;
    private final PersonalContactMapper personalContactMapper;

    public List<PersonalContact> findAll() {
        String sql = "SELECT id, employee_id, phone_number, personal_email FROM PERSONAL_CONTACT";
        return jdbcTemplate.query(sql, personalContactMapper);
    }

    public Optional<PersonalContact> findById(Long id) {
        String sql = "SELECT id, employee_id, phone_number, personal_email FROM PERSONAL_CONTACT WHERE id = ?";
        List<PersonalContact> results = jdbcTemplate.query(sql, personalContactMapper, id);
        return results.stream().findFirst();
    }

    public Optional<PersonalContact> findByEmployeeId(Long employeeId) {
        String sql = "SELECT id, employee_id, phone_number, personal_email FROM PERSONAL_CONTACT WHERE employee_id = ?";
        List<PersonalContact> results = jdbcTemplate.query(sql, personalContactMapper, employeeId);
        return results.stream().findFirst();
    }

    public Long save(PersonalContact personalContact) {
        String sql = "INSERT INTO PERSONAL_CONTACT (employee_id, phone_number, personal_email) VALUES (?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, new String[]{"id"});
            ps.setLong(1, personalContact.getEmployeeId());
            ps.setString(2, personalContact.getPhoneNumber());
            ps.setString(3, personalContact.getPersonalEmail());
            return ps;
        }, keyHolder);

        return keyHolder.getKey().longValue();
    }

    public int update(Long id, PersonalContact personalContact) {
        String sql = "UPDATE PERSONAL_CONTACT SET employee_id = ?, phone_number = ?, personal_email = ? WHERE id = ?";
        return jdbcTemplate.update(sql,
                personalContact.getEmployeeId(),
                personalContact.getPhoneNumber(),
                personalContact.getPersonalEmail(),
                id);
    }

    public int deleteById(Long id) {
        String sql = "DELETE FROM PERSONAL_CONTACT WHERE id = ?";
        return jdbcTemplate.update(sql, id);
    }
}