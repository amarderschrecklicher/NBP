package ba.unsa.etf.employeemanagement.mapper;

import ba.unsa.etf.employeemanagement.model.RefreshToken;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class RefreshTokenMapper implements RowMapper<RefreshToken> {
    @Override
    public RefreshToken mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new RefreshToken(
                rs.getLong("id"),
                rs.getString("token"),
                rs.getLong("user_id"),
                rs.getTimestamp("expiry_date")
        );
    }
}
