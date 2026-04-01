package ba.unsa.etf.employeemanagement.repository;

import ba.unsa.etf.employeemanagement.mapper.EmployeeMapper;
import ba.unsa.etf.employeemanagement.model.Employee;
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
public class EmployeeRepository {
    private final JdbcTemplate jdbcTemplate;
    private final EmployeeMapper employeeMapper;

    public List<Employee> findAll() {
        String sql = "SELECT id, user_id, gender, nationality, marital_status FROM Employee";
        return jdbcTemplate.query(sql, employeeMapper);
    }

    public Optional<Employee> findById(Long id) {
        String sql = "SELECT id, user_id, gender, nationality, marital_status FROM Employee WHERE id = ?";
        List<Employee> results = jdbcTemplate.query(sql, employeeMapper, id);
        return results.stream().findFirst();
    }

    public Long save(Employee employee) {
        String sql = "INSERT INTO Employee (id, user_id, gender, nationality, marital_status) VALUES (EMPLOYEE_SEQ.NEXTVAL, ?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, new String[]{"id"});
            ps.setLong(1, employee.getUserId());
            ps.setString(2, employee.getGender());
            ps.setString(3, employee.getNationality());
            ps.setString(4, employee.getMaritalStatus());
            return ps;
        }, keyHolder);

        return keyHolder.getKey().longValue();
    }

    public int update(Long id, Employee employee) {
        String sql = "UPDATE Employee SET user_id = ?, gender = ?, nationality = ?, marital_status = ? WHERE id = ?";
        return jdbcTemplate.update(sql,
                employee.getUserId(),
                employee.getGender(),
                employee.getNationality(),
                employee.getMaritalStatus(),
                id);
    }

    public int deleteById(Long id) {
        String sql = "DELETE FROM employee WHERE id = ?";
        return jdbcTemplate.update(sql, id);
    }
}
