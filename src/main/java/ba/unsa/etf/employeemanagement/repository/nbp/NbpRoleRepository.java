package ba.unsa.etf.employeemanagement.repository.nbp;

import ba.unsa.etf.employeemanagement.mapper.nbp.NbpRoleMapper;
import ba.unsa.etf.employeemanagement.model.nbp.NbpRole;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Types;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class NbpRoleRepository {

    private final JdbcTemplate jdbcTemplate;
    private final NbpRoleMapper mapper;

    private static final String SCHEMA = "NBP";
    private static final String TABLE = SCHEMA + ".NBP_ROLE";

    public List<NbpRole> findAll() {
        String sql = "SELECT ID, NAME FROM " + TABLE + " ORDER BY ID";
        return jdbcTemplate.query(sql, mapper);
    }

    public Optional<NbpRole> findById(Long id) {
        String sql = "SELECT ID, NAME FROM " + TABLE + " WHERE ID = ?";
        return jdbcTemplate.query(sql, mapper, id).stream().findFirst();
    }

    public Optional<NbpRole> findByName(String name) {
        String sql = "SELECT ID, NAME FROM " + TABLE + " WHERE NAME = ?";
        return jdbcTemplate.query(sql, mapper, name).stream().findFirst();
    }

    public Long save(NbpRole entity) {
        String sql = "INSERT INTO " + TABLE + " (NAME) VALUES (?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, new String[]{"ID"});
            if (entity.getName() != null) {
                ps.setString(1, entity.getName());
            } else {
                ps.setNull(1, Types.VARCHAR);
            }
            return ps;
        }, keyHolder);

        return keyHolder.getKey().longValue();
    }

    public int update(Long id, NbpRole entity) {
        String sql = "UPDATE " + TABLE + " SET NAME = ? WHERE ID = ?";
        return jdbcTemplate.update(sql, entity.getName(), id);
    }

    public int deleteById(Long id) {
        String sql = "DELETE FROM " + TABLE + " WHERE ID = ?";
        return jdbcTemplate.update(sql, id);
    }

    public long count() {
        String sql = "SELECT COUNT(*) FROM " + TABLE;
        Long count = jdbcTemplate.queryForObject(sql, Long.class);
        return count != null ? count : 0;
    }
}

