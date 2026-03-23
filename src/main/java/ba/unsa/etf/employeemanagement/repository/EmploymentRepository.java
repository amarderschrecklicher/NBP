package ba.unsa.etf.employeemanagement.repository;

import ba.unsa.etf.employeemanagement.mapper.EmploymentMapper;
import ba.unsa.etf.employeemanagement.model.Employee;
import ba.unsa.etf.employeemanagement.model.Employment;
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
public class EmploymentRepository {

    private final JdbcTemplate jdbcTemplate;
    private final EmploymentMapper employmentMapper;

    public List<Employment> findAll() {
        String sql = """
                SELECT id, employee_id, number, hire_date, termination_date,
                       job_title, employment_type, status, department_id
                FROM employment
                """;
        return jdbcTemplate.query(sql, employmentMapper);
    }

    public Optional<Employment> findById(Long id) {
        String sql = """
                SELECT id, employee_id, number, hire_date, termination_date,
                       job_title, employment_type, status, department_id
                FROM employment
                WHERE id = ?
                """;
        List<Employment> result = jdbcTemplate.query(sql, employmentMapper, id);
        return result.stream().findFirst();
    }

    public Long save(Employment employment) {
        String sql = """
                INSERT INTO employment (
                    employee_id, number, hire_date, termination_date,
                    job_title, employment_type, status, department_id
                )
                VALUES (?, ?, ?, ?, ?, ?, ?, ?)
                """;

        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, new String[]{"id"});
            ps.setLong(1, employment.getEmployeeId());
            ps.setString(2, employment.getNumber());
            ps.setDate(3, employment.getHireDate() != null
                    ? new java.sql.Date(employment.getHireDate().getTime())
                    : null);
            ps.setDate(4, employment.getTerminationDate() != null
                    ? new java.sql.Date(employment.getTerminationDate().getTime())
                    : null);
            ps.setString(5, employment.getJobTitle());
            ps.setString(6, employment.getEmploymentType());
            ps.setString(7, employment.getStatus());
            ps.setLong(8, employment.getDepartmentId());
            return ps;
        }, keyHolder);

        return keyHolder.getKey().longValue();
    }

    public int update(Long id, Employment employment) {
        String sql = """
                UPDATE employment
                SET employee_id = ?,
                    number = ?,
                    hire_date = ?,
                    termination_date = ?,
                    job_title = ?,
                    employment_type = ?,
                    status = ?,
                    department_id = ?
                WHERE id = ?
                """;

        return jdbcTemplate.update(
                sql,
                employment.getEmployeeId(),
                employment.getNumber(),
                employment.getHireDate() != null
                        ? new java.sql.Date(employment.getHireDate().getTime())
                        : null,
                employment.getTerminationDate() != null
                        ? new java.sql.Date(employment.getTerminationDate().getTime())
                        : null,
                employment.getJobTitle(),
                employment.getEmploymentType(),
                employment.getStatus(),
                employment.getDepartmentId(),
                id
        );
    }

    public int deleteById(Long id) {
        String sql = "DELETE FROM employment WHERE id = ?";
        return jdbcTemplate.update(sql, id);
    }
}
