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
        String sql = """
                SELECT id, user_id, first_name, last_name, date_of_birth, gender, nationality, marital_status
                FROM employee
                """;
        return jdbcTemplate.query(sql, employeeMapper);
    }

    public Optional<Employee> findById(Long id) {
        String sql = """
                SELECT id, user_id, first_name, last_name, date_of_birth, gender, nationality, marital_status
                FROM employee
                WHERE id = ?
                """;
        List<Employee> result = jdbcTemplate.query(sql, employeeMapper, id);
        return result.stream().findFirst();
    }

    public Long save(Employee employee) {
        String sql = """
                INSERT INTO employee (user_id, first_name, last_name, date_of_birth, gender, nationality, marital_status)
                VALUES (?, ?, ?, ?, ?, ?, ?)
                """;
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, new String[]{"id"});
            ps.setLong(1, employee.getUserId());
            ps.setString(2, employee.getFirstName());
            ps.setString(3, employee.getLastName());
            ps.setDate(4, new java.sql.Date(employee.getDateOfBirth().getTime()));
            ps.setString(5, employee.getGender());
            ps.setString(6, employee.getNationality());
            ps.setString(7, employee.getMaritalStatus());
            return ps;
        }, keyHolder);

        return keyHolder.getKey().longValue();
    }

    public int update(Long id, Employee employee) {
        String sql = """
                UPDATE employee
                SET user_id = ?, first_name = ?, last_name = ?, date_of_birth = ?, gender = ?, nationality = ?, marital_status = ?, 
                WHERE id = ?
                """;

        return jdbcTemplate.update(
                sql,
                employee.getUserId(),
                employee.getFirstName(),
                employee.getLastName(),
                employee.getDateOfBirth(),
                employee.getGender(),
                employee.getNationality(),
                employee.getMaritalStatus(),
                id
        );
    }

    public int deleteById(Long id) {
        String sql = "DELETE FROM employee WHERE id = ?";
        return jdbcTemplate.update(sql, id);
    }
}
