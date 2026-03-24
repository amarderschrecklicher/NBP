package ba.unsa.etf.employeemanagement.repository;
import ba.unsa.etf.employeemanagement.model.NbpApps;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
@Slf4j
public class NbpAppsRepository {

    private final JdbcTemplate jdbcTemplate;

    private static final String SCHEMA = "NBP";
    private static final String TABLE = SCHEMA + ".NBP_APPS";

    private final RowMapper<NbpApps> rowMapper = (rs, rowNum) -> NbpApps.builder()
            .id(rs.getLong("ID"))
            .appId(rs.getString("APP_ID"))
            .managerId(rs.getLong("MANAGER_ID"))
            .expiryDate(rs.getDate("EXPIRY_DATE") != null ?
                    rs.getDate("EXPIRY_DATE").toLocalDate() : null)
            .build();

    public List<NbpApps> findAll() {
        String sql = "SELECT * FROM " + TABLE + " ORDER BY ID";
        log.debug("Executing query: {}", sql);
        return jdbcTemplate.query(sql, rowMapper);
    }

    public Optional<NbpApps> findById(Long id) {
        String sql = "SELECT * FROM " + TABLE + " WHERE ID = ?";
        log.debug("Executing query: {} with id: {}", sql, id);
        return jdbcTemplate.query(sql, rowMapper, id)
                .stream()
                .findFirst();
    }

    public List<NbpApps> findByManagerId(Long managerId) {
        String sql = "SELECT * FROM " + TABLE + " WHERE MANAGER_ID = ? ORDER BY ID";
        log.debug("Finding apps by manager id: {}", managerId);
        return jdbcTemplate.query(sql, rowMapper, managerId);
    }

    public List<NbpApps> findExpiredApps() {
        String sql = "SELECT * FROM " + TABLE +
                " WHERE EXPIRY_DATE < SYSDATE ORDER BY EXPIRY_DATE DESC";
        log.debug("Finding expired apps");
        return jdbcTemplate.query(sql, rowMapper);
    }

    public NbpApps save(NbpApps apps) {
        String sql = "UPDATE " + TABLE +
                " SET APP_ID = ?, MANAGER_ID = ?, EXPIRY_DATE = ? " +
                "WHERE ID = ?";

        log.debug("Updating NBP_APPS: {}", apps);

        int rowsAffected = jdbcTemplate.update(sql,
                apps.getAppId(),
                apps.getManagerId(),
                apps.getExpiryDate() != null ? Date.valueOf(apps.getExpiryDate()) : null,
                apps.getId());

        if (rowsAffected == 0) {
            throw new RuntimeException("NBP_APPS not found with id: " + apps.getId());
        }

        log.debug("NBP_APPS updated successfully");
        return apps;
    }

    public long count() {
        String sql = "SELECT COUNT(*) FROM " + TABLE;
        Long count = jdbcTemplate.queryForObject(sql, Long.class);
        return count != null ? count : 0;
    }
}

