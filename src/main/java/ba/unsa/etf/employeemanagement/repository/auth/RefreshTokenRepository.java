package ba.unsa.etf.employeemanagement.repository.auth;

import ba.unsa.etf.employeemanagement.mapper.auth.RefreshTokenMapper;
import ba.unsa.etf.employeemanagement.model.auth.RefreshToken;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class RefreshTokenRepository {

    private final JdbcTemplate jdbcTemplate;
    private final RefreshTokenMapper mapper;

    public Optional<RefreshToken> findByToken(String token) {
        String sql = "SELECT id, token, user_id, expiry_date FROM REFRESH_TOKEN WHERE token = ?";
        return jdbcTemplate.query(sql, mapper, token).stream().findFirst();
    }

    public void save(RefreshToken token) {
        String sql = "INSERT INTO REFRESH_TOKEN (token, user_id, expiry_date) VALUES (?, ?, ?)";
        jdbcTemplate.update(sql, token.getToken(), token.getUserId(), new Timestamp(token.getExpiryDate().getTime()));
    }

    public int deleteByUserId(Long userId) {
        String sql = "DELETE FROM REFRESH_TOKEN WHERE user_id = ?";
        return jdbcTemplate.update(sql, userId);
    }
}
