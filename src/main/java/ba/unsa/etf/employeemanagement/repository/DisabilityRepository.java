package ba.unsa.etf.employeemanagement.repository;

import ba.unsa.etf.employeemanagement.mapper.DisabilityMapper;
import ba.unsa.etf.employeemanagement.model.Disability;
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
public class DisabilityRepository {

    private final JdbcTemplate jdbcTemplate;
    private final DisabilityMapper disabilityMapper;

    public List<Disability> findAll() {
        String sql = "SELECT * FROM disability";
        return jdbcTemplate.query(sql, disabilityMapper);
    }

    public Optional<Disability> findById(Long id) {
        String sql = "SELECT * FROM disability WHERE id = ?";
        return jdbcTemplate.query(sql, disabilityMapper, id).stream().findFirst();
    }

    public Long save(Disability disability) {
        String sql = "INSERT INTO disability (employee_id, disability_type, disability_level, description, registered_date) VALUES (?, ?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(conn -> {
            PreparedStatement ps = conn.prepareStatement(sql, new String[]{"id"});
            ps.setLong(1, disability.getEmployeeId());
            ps.setString(2, disability.getDisabilityType());
            ps.setString(3, disability.getDisabilityLevel());
            ps.setString(4, disability.getDescription());
            ps.setDate(5, disability.getRegisteredDate() != null ? new java.sql.Date(disability.getRegisteredDate().getTime()) : null);
            return ps;
        }, keyHolder);

        return keyHolder.getKey().longValue();
    }

    public int update(Long id, Disability disability) {
        String sql = "UPDATE disability SET employee_id=?, disability_type=?, disability_level=?, description=?, registered_date=? WHERE id=?";
        return jdbcTemplate.update(sql,
                disability.getEmployeeId(),
                disability.getDisabilityType(),
                disability.getDisabilityLevel(),
                disability.getDescription(),
                disability.getRegisteredDate(),
                id);
    }

    public int deleteById(Long id) {
        return jdbcTemplate.update("DELETE FROM disability WHERE id=?", id);
    }
}
