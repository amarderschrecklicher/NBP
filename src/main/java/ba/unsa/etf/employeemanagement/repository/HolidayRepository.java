package ba.unsa.etf.employeemanagement.repository;

import ba.unsa.etf.employeemanagement.mapper.HolidayMapper;
import ba.unsa.etf.employeemanagement.model.Holiday;
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
public class HolidayRepository {
    private final JdbcTemplate jdbcTemplate;
    private final HolidayMapper mapper;

    public List<Holiday> findAll() {
        String sql = "SELECT id, holiday_name, holiday_date, country, description FROM holiday";
        return jdbcTemplate.query(sql, mapper);
    }

    public Optional<Holiday> findById(Long id) {
        String sql = "SELECT id, holiday_name, holiday_date, country, description FROM holiday WHERE id = ?";
        List<Holiday> res = jdbcTemplate.query(sql, mapper, id);
        return res.stream().findFirst();
    }

    public Long save(Holiday entity) {
        String sql = "INSERT INTO holiday (holiday_name, holiday_date, country, description) VALUES (?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, new String[]{"id"});
            ps.setString(1, entity.getHolidayName());
            ps.setDate(2, entity.getHolidayDate() != null ? new java.sql.Date(entity.getHolidayDate().getTime()) : null);
            ps.setString(3, entity.getCountry());
            ps.setString(4, entity.getDescription());
            return ps;
        }, keyHolder);
        return keyHolder.getKey().longValue();
    }

    public int update(Long id, Holiday entity) {
        String sql = "UPDATE holiday SET holiday_name = ?, holiday_date = ?, country = ?, description = ? WHERE id = ?";
        return jdbcTemplate.update(sql, entity.getHolidayName(), entity.getHolidayDate(), entity.getCountry(), entity.getDescription(), id);
    }

    public int deleteById(Long id) {
        return jdbcTemplate.update("DELETE FROM holiday WHERE id = ?", id);
    }
}

