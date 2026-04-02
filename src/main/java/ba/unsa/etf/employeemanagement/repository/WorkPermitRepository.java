package ba.unsa.etf.employeemanagement.repository;

import ba.unsa.etf.employeemanagement.mapper.WorkPermitMapper;
import ba.unsa.etf.employeemanagement.model.WorkPermit;
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
public class WorkPermitRepository {

    private final JdbcTemplate jdbcTemplate;
    private final WorkPermitMapper workPermitMapper;

    public List<WorkPermit> findAll() {
        String sql = "SELECT * FROM work_permit";
        return jdbcTemplate.query(sql, workPermitMapper);
    }

    public Optional<WorkPermit> findById(Long id) {
        String sql = "SELECT * FROM work_permit WHERE id = ?";
        return jdbcTemplate.query(sql, workPermitMapper, id).stream().findFirst();
    }

    public Long save(WorkPermit workPermit) {
        String sql = "INSERT INTO work_permit (employee_id, permit_number, permit_type, issuing_country, issue_date, expiry_date, status) VALUES (?, ?, ?, ?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(conn -> {
            PreparedStatement ps = conn.prepareStatement(sql, new String[]{"id"});
            ps.setLong(1, workPermit.getEmployeeId());
            ps.setString(2, workPermit.getPermitNumber());
            ps.setString(3, workPermit.getPermitType());
            ps.setString(4, workPermit.getIssuingCountry());
            ps.setDate(5, workPermit.getIssueDate() != null ? new java.sql.Date(workPermit.getIssueDate().getTime()) : null);
            ps.setDate(6, workPermit.getExpiryDate() != null ? new java.sql.Date(workPermit.getExpiryDate().getTime()) : null);
            ps.setString(7, workPermit.getStatus());
            return ps;
        }, keyHolder);

        return keyHolder.getKey().longValue();
    }

    public int update(Long id, WorkPermit workPermit) {
        String sql = "UPDATE work_permit SET employee_id=?, permit_number=?, permit_type=?, issuing_country=?, issue_date=?, expiry_date=?, status=? WHERE id=?";
        return jdbcTemplate.update(sql,
                workPermit.getEmployeeId(),
                workPermit.getPermitNumber(),
                workPermit.getPermitType(),
                workPermit.getIssuingCountry(),
                workPermit.getIssueDate(),
                workPermit.getExpiryDate(),
                workPermit.getStatus(),
                id);
    }

    public int deleteById(Long id) {
        return jdbcTemplate.update("DELETE FROM work_permit WHERE id=?", id);
    }
}
