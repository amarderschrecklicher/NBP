package ba.unsa.etf.employeemanagement.repository;

import ba.unsa.etf.employeemanagement.mapper.EmployeePhotoMapper;
import ba.unsa.etf.employeemanagement.model.EmployeePhoto;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class EmployeePhotoRepository {
    private final JdbcTemplate jdbcTemplate;
    private final EmployeePhotoMapper employeePhotoMapper;

    public Optional<EmployeePhoto> findByUserId(Long userId) {
        String sql = "SELECT id, user_id, photo, content_type, uploaded_at FROM EMPLOYEE_PHOTO WHERE user_id = ?";
        List<EmployeePhoto> results = jdbcTemplate.query(sql, employeePhotoMapper, userId);
        return results.stream().findFirst();
    }

    public Long save(Long userId, byte[] photo, String contentType) {
        String sql = "INSERT INTO EMPLOYEE_PHOTO (id, user_id, photo, content_type, uploaded_at) VALUES (EMPLOYEE_PHOTO_SEQ.NEXTVAL, ?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, new String[]{"id"});
            ps.setLong(1, userId);
            ps.setBytes(2, photo);
            ps.setString(3, contentType);
            ps.setTimestamp(4, Timestamp.valueOf(LocalDateTime.now()));
            return ps;
        }, keyHolder);

        return keyHolder.getKey().longValue();
    }

    public int update(Long userId, byte[] photo, String contentType) {
        String sql = "UPDATE EMPLOYEE_PHOTO SET photo = ?, content_type = ?, uploaded_at = ? WHERE user_id = ?";
        return jdbcTemplate.update(sql, photo, contentType, Timestamp.valueOf(LocalDateTime.now()), userId);
    }

    public void deleteByUserId(Long userId) {
        String sql = "DELETE FROM EMPLOYEE_PHOTO WHERE user_id = ?";
        jdbcTemplate.update(sql, userId);
    }
}


