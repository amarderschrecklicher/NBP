package ba.unsa.etf.employeemanagement.repository.nbp;
import ba.unsa.etf.employeemanagement.model.nbp.NbpLogs;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
@Slf4j
public class NbpLogsRepository {

    private final JdbcTemplate jdbcTemplate;

    private static final String SCHEMA = "NBP";
    private static final String TABLE = SCHEMA + ".NBP_LOG";

    private final RowMapper<NbpLogs> rowMapper = (rs, rowNum) -> NbpLogs.builder()
            .id(rs.getLong("ID"))
            .actionName(rs.getString("ACTION_NAME"))
            .tableName(rs.getString("TABLE_NAME"))
            .dateTime(rs.getTimestamp("DATE_TIME") != null ?
                    rs.getTimestamp("DATE_TIME").toLocalDateTime() : null)
            .dbUser(rs.getString("DB_USER"))
            .build();

    public List<NbpLogs> findAll() {
        String sql = "SELECT * FROM " + TABLE + " ORDER BY DATE_TIME DESC";
        log.debug("Executing query: {}", sql);
        return jdbcTemplate.query(sql, rowMapper);
    }

    public Optional<NbpLogs> findById(Long id) {
        String sql = "SELECT * FROM " + TABLE + " WHERE ID = ?";
        log.debug("Executing query: {} with id: {}", sql, id);
        return jdbcTemplate.query(sql, rowMapper, id)
                .stream()
                .findFirst();
    }

    public List<NbpLogs> findByTableName(String tableName) {
        String sql = "SELECT * FROM " + TABLE +
                " WHERE TABLE_NAME = ? ORDER BY DATE_TIME DESC";
        log.debug("Finding logs by table name: {}", tableName);
        return jdbcTemplate.query(sql, rowMapper, tableName);
    }

    public List<NbpLogs> findByActionName(String actionName) {
        String sql = "SELECT * FROM " + TABLE +
                " WHERE ACTION_NAME = ? ORDER BY DATE_TIME DESC";
        log.debug("Finding logs by action name: {}", actionName);
        return jdbcTemplate.query(sql, rowMapper, actionName);
    }

    public List<NbpLogs> findByDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        String sql = "SELECT * FROM " + TABLE +
                " WHERE DATE_TIME BETWEEN ? AND ? ORDER BY DATE_TIME DESC";
        log.debug("Finding logs between {} and {}", startDate, endDate);
        return jdbcTemplate.query(sql, rowMapper,
                Timestamp.valueOf(startDate),
                Timestamp.valueOf(endDate));
    }

    public List<NbpLogs> findByDbUser(String dbUser) {
        String sql = "SELECT * FROM " + TABLE +
                " WHERE DB_USER = ? ORDER BY DATE_TIME DESC";
        log.debug("Finding logs by db user: {}", dbUser);
        return jdbcTemplate.query(sql, rowMapper, dbUser);
    }

    public List<NbpLogs> findRecentLogs(int limit) {
        String sql = "SELECT * FROM " + TABLE +
                " ORDER BY DATE_TIME DESC FETCH FIRST ? ROWS ONLY";
        log.debug("Finding {} most recent logs", limit);
        return jdbcTemplate.query(sql, rowMapper, limit);
    }

    public long count() {
        String sql = "SELECT COUNT(*) FROM " + TABLE;
        Long count = jdbcTemplate.queryForObject(sql, Long.class);
        return count != null ? count : 0;
    }

    public long countByTableName(String tableName) {
        String sql = "SELECT COUNT(*) FROM " + TABLE + " WHERE TABLE_NAME = ?";
        Long count = jdbcTemplate.queryForObject(sql, Long.class, tableName);
        return count != null ? count : 0;
    }
}

