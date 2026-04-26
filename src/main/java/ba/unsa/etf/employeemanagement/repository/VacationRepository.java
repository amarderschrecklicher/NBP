package ba.unsa.etf.employeemanagement.repository;

import ba.unsa.etf.employeemanagement.mapper.VacationMapper;
import ba.unsa.etf.employeemanagement.model.Vacation;
import ba.unsa.etf.employeemanagement.util.enums.VacationStatus;
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
public class VacationRepository {
    private final JdbcTemplate jdbcTemplate;
    private final VacationMapper mapper;

    public List<Vacation> findAll() {
        String sql = "SELECT id, employee_id, start_date, end_date, vacation_type, status, approved_by, reason FROM vacation";
        return jdbcTemplate.query(sql, mapper);
    }

    public Optional<Vacation> findById(Long id) {
        String sql = "SELECT id, employee_id, start_date, end_date, vacation_type, status, approved_by, reason FROM vacation WHERE id = ?";
        List<Vacation> res = jdbcTemplate.query(sql, mapper, id);
        return res.stream().findFirst();
    }

    public Long save(Vacation entity) {
        String sql = "INSERT INTO vacation (employee_id, start_date, end_date, vacation_type, status, approved_by, reason) VALUES (?, ?, ?, ?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, new String[]{"id"});
            if (entity.getEmployeeId() != null) ps.setLong(1, entity.getEmployeeId());
            else ps.setNull(1, java.sql.Types.NUMERIC);
            ps.setDate(2, entity.getStartDate() != null ? new java.sql.Date(entity.getStartDate().getTime()) : null);
            ps.setDate(3, entity.getEndDate() != null ? new java.sql.Date(entity.getEndDate().getTime()) : null);
            ps.setString(4, entity.getVacationType());
            ps.setString(5, entity.getStatus());
            if (entity.getApprovedBy() != null) ps.setLong(6, entity.getApprovedBy());
            else ps.setNull(6, java.sql.Types.NUMERIC);
            ps.setString(7, entity.getReason());
            return ps;
        }, keyHolder);
        return keyHolder.getKey().longValue();
    }

    public int update(Long id, Vacation entity) {
        String sql = "UPDATE vacation SET employee_id = ?, start_date = ?, end_date = ?, vacation_type = ?, status = ?, approved_by = ?, reason = ? WHERE id = ?";
        return jdbcTemplate.update(sql, entity.getEmployeeId(), entity.getStartDate(), entity.getEndDate(), entity.getVacationType(), entity.getStatus(), entity.getApprovedBy(), entity.getReason(), id);
    }

    public int deleteById(Long id) {
        return jdbcTemplate.update("DELETE FROM vacation WHERE id = ?", id);
    }

    public List<Vacation> findByEmployeeIdAndYear(Long employeeId, int year) {
        String sql = "SELECT id, employee_id, start_date, end_date, vacation_type, status," +
                " approved_by, reason FROM vacation WHERE employee_id = ? AND EXTRACT(YEAR FROM start_date) = ?";
        return jdbcTemplate.query(sql, mapper, employeeId, year);
    }

    public void updateStatus(Long id, VacationStatus status, Long approverId, String reason) {
        String sql = "UPDATE vacation SET status = ?, approved_by = ?, reason = ? WHERE id = ?";
        jdbcTemplate.update(sql, status.name(), approverId, reason, id);
    }
}
