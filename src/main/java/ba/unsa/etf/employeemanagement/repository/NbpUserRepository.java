package ba.unsa.etf.employeemanagement.repository;

import ba.unsa.etf.employeemanagement.mapper.NbpUserMapper;
import ba.unsa.etf.employeemanagement.model.NbpUser;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.Types;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class NbpUserRepository {

    private final JdbcTemplate jdbcTemplate;
    private final NbpUserMapper mapper;

    private static final String SCHEMA = "NBP";
    private static final String TABLE = SCHEMA + ".NBP_USER";

    public List<NbpUser> findAll() {
        String sql = "SELECT ID, FIRST_NAME, LAST_NAME, EMAIL, USERNAME, PHONE_NUMBER, BIRTH_DATE, ADDRESS_ID, ROLE_ID " +
                "FROM " + TABLE + " ORDER BY ID";
        return jdbcTemplate.query(sql, mapper);
    }

    public Optional<NbpUser> findById(Long id) {
        String sql = "SELECT ID, FIRST_NAME, LAST_NAME, EMAIL, USERNAME, PHONE_NUMBER, BIRTH_DATE, ADDRESS_ID, ROLE_ID " +
                "FROM " + TABLE + " WHERE ID = ?";
        List<NbpUser> res = jdbcTemplate.query(sql, mapper, id);
        return res.stream().findFirst();
    }

    public Optional<NbpUser> findByUsername(String username) {
        String sql = "SELECT ID, FIRST_NAME, LAST_NAME, EMAIL, USERNAME, PHONE_NUMBER, BIRTH_DATE, ADDRESS_ID, ROLE_ID " +
                "FROM " + TABLE + " WHERE USERNAME = ?";
        List<NbpUser> res = jdbcTemplate.query(sql, mapper, username);
        return res.stream().findFirst();
    }

    public Optional<String> findPasswordById(Long id) {
        String sql = "SELECT PASSWORD FROM " + TABLE + " WHERE ID = ?";
        List<String> res = jdbcTemplate.query(sql, (rs, rowNum) -> rs.getString("PASSWORD"), id);
        return res.stream().findFirst();
    }

    public Long save(NbpUser entity) {
        String sql = "INSERT INTO " + TABLE +
                " (FIRST_NAME, LAST_NAME, EMAIL, PASSWORD, USERNAME, PHONE_NUMBER, BIRTH_DATE, ADDRESS_ID, ROLE_ID) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, new String[]{"ID"});

            ps.setString(1, entity.getFirstName());
            ps.setString(2, entity.getLastName());
            ps.setString(3, entity.getEmail());
            ps.setString(4, entity.getPassword());
            ps.setString(5, entity.getUsername());
            if (entity.getPhoneNumber() != null) {
                ps.setString(6, entity.getPhoneNumber());
            } else {
                ps.setNull(6, Types.VARCHAR);
            }

            ps.setDate(7, entity.getBirthDate() != null ? new Date(entity.getBirthDate().getTime()) : null);

            if (entity.getAddressId() != null) {
                ps.setLong(8, entity.getAddressId());
            } else {
                ps.setNull(8, Types.NUMERIC);
            }

            if (entity.getRoleId() != null) {
                ps.setLong(9, entity.getRoleId());
            } else {
                ps.setNull(9, Types.NUMERIC);
            }

            return ps;
        }, keyHolder);

        return keyHolder.getKey().longValue();
    }

    public int update(Long id, NbpUser entity) {
        String sql = "UPDATE " + TABLE +
                " SET FIRST_NAME = ?, LAST_NAME = ?, EMAIL = ?, PASSWORD = ?, USERNAME = ?, PHONE_NUMBER = ?, BIRTH_DATE = ?, ADDRESS_ID = ?, ROLE_ID = ? " +
                "WHERE ID = ?";

        return jdbcTemplate.update(sql,
                entity.getFirstName(),
                entity.getLastName(),
                entity.getEmail(),
                entity.getPassword(),
                entity.getUsername(),
                entity.getPhoneNumber(),
                entity.getBirthDate() != null ? new Date(entity.getBirthDate().getTime()) : null,
                entity.getAddressId(),
                entity.getRoleId(),
                id
        );
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

