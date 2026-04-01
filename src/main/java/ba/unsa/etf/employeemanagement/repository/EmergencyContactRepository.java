package ba.unsa.etf.employeemanagement.repository;

import ba.unsa.etf.employeemanagement.mapper.EmergencyContactMapper;
import ba.unsa.etf.employeemanagement.model.EmergencyContact;
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
public class EmergencyContactRepository {

    private final JdbcTemplate jdbcTemplate;
    private final EmergencyContactMapper emergencyContactMapper;

    public List<EmergencyContact> findAll() {
        String sql = "SELECT id, employee_id, first_name, last_name, relationship, phone_number, email, address FROM emergency_contact";
        return jdbcTemplate.query(sql, emergencyContactMapper);
    }

    public Optional<EmergencyContact> findById(Long id) {
        String sql = "SELECT id, employee_id, first_name, last_name, relationship, phone_number, email, address FROM emergency_contact WHERE id = ?";
        List<EmergencyContact> results = jdbcTemplate.query(sql, emergencyContactMapper, id);
        return results.stream().findFirst();
    }

    public Long save(EmergencyContact emergencyContact) {
        String sql = "INSERT INTO emergency_contact (employee_id, first_name, last_name, relationship, phone_number, email, address) VALUES (?, ?, ?, ?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, new String[]{"id"});
            ps.setLong(1, emergencyContact.getEmployeeId());
            ps.setString(2, emergencyContact.getFirstName());
            ps.setString(3, emergencyContact.getLastName());
            ps.setString(4, emergencyContact.getRelationship());
            ps.setString(5, emergencyContact.getPhoneNumber());
            ps.setString(6, emergencyContact.getEmail());
            ps.setString(7, emergencyContact.getAddress());
            return ps;
        }, keyHolder);

        return keyHolder.getKey().longValue();
    }

    public int update(Long id, EmergencyContact emergencyContact) {
        String sql = "UPDATE emergency_contact SET employee_id = ?, first_name = ?, last_name = ?, relationship = ?, phone_number = ?, email = ?, address = ? WHERE id = ?";
        return jdbcTemplate.update(sql,
                emergencyContact.getEmployeeId(),
                emergencyContact.getFirstName(),
                emergencyContact.getLastName(),
                emergencyContact.getRelationship(),
                emergencyContact.getPhoneNumber(),
                emergencyContact.getEmail(),
                emergencyContact.getAddress(),
                id);
    }

    public int deleteById(Long id) {
        String sql = "DELETE FROM emergency_contact WHERE id = ?";
        return jdbcTemplate.update(sql, id);
    }
}