package ba.unsa.etf.employeemanagement.repository;

import ba.unsa.etf.employeemanagement.mapper.DepartmentMapper;
import ba.unsa.etf.employeemanagement.model.Department;
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
public class DepartmentRepository {
    private final JdbcTemplate jdbcTemplate;
    private final DepartmentMapper mapper;

    public List<Department> findAll() {
        String sql = "SELECT id, name, description FROM department";
        return jdbcTemplate.query(sql, mapper);
    }

    public Optional<Department> findById(Long id) {
        String sql = "SELECT id, name, description FROM department WHERE id = ?";
        List<Department> res = jdbcTemplate.query(sql, mapper, id);
        return res.stream().findFirst();
    }

    public Long save(Department entity) {
        String sql = "INSERT INTO department (name, description) VALUES (?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, new String[]{"id"});
            ps.setString(1, entity.getName());
            ps.setString(2, entity.getDescription());
            return ps;
        }, keyHolder);
        return keyHolder.getKey().longValue();
    }

    public int update(Long id, Department entity) {
        String sql = "UPDATE department SET name = ?, description = ? WHERE id = ?";
        return jdbcTemplate.update(sql, entity.getName(), entity.getDescription(), id);
    }

    public int deleteById(Long id) {
        return jdbcTemplate.update("DELETE FROM department WHERE id = ?", id);
    }
}

