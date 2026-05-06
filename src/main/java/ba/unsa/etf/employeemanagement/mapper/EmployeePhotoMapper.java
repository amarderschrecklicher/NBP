package ba.unsa.etf.employeemanagement.mapper;

import ba.unsa.etf.employeemanagement.model.EmployeePhoto;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class EmployeePhotoMapper implements RowMapper<EmployeePhoto> {
    @Override
    public EmployeePhoto mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new EmployeePhoto(
                rs.getLong("id"),
                rs.getLong("user_id"),
                rs.getBytes("photo"),
                rs.getString("content_type"),
                rs.getTimestamp("uploaded_at") != null ? rs.getTimestamp("uploaded_at").toLocalDateTime() : null
        );
    }
}


