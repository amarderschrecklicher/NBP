package ba.unsa.etf.employeemanagement.repository;

import ba.unsa.etf.employeemanagement.mapper.FamilyMemberMapper;
import ba.unsa.etf.employeemanagement.model.FamilyMember;
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
public class FamilyMemberRepository {

    private final JdbcTemplate jdbcTemplate;
    private final FamilyMemberMapper familyMemberMapper;

    public List<FamilyMember> findAll() {
        String sql = "SELECT id, employee_id, first_name, last_name, relation, date_of_birth, dependent, occupation FROM family_member";
        return jdbcTemplate.query(sql, familyMemberMapper);
    }

    public Optional<FamilyMember> findById(Long id) {
        String sql = "SELECT id, employee_id, first_name, last_name, relation, date_of_birth, dependent, occupation FROM family_member WHERE id = ?";
        List<FamilyMember> results = jdbcTemplate.query(sql, familyMemberMapper, id);
        return results.stream().findFirst();
    }

    public Long save(FamilyMember familyMember) {
        String sql = "INSERT INTO family_member (employee_id, first_name, last_name, relation, date_of_birth, dependent, occupation) VALUES (?, ?, ?, ?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, new String[]{"id"});
            ps.setLong(1, familyMember.getEmployeeId());
            ps.setString(2, familyMember.getFirstName());
            ps.setString(3, familyMember.getLastName());
            ps.setString(4, familyMember.getRelation());
            ps.setDate(5, familyMember.getDateOfBirth() != null ? new java.sql.Date(familyMember.getDateOfBirth().getTime()) : null);
            if (familyMember.getDependent() != null) {
                ps.setInt(6, familyMember.getDependent());
            } else {
                ps.setNull(6, java.sql.Types.NUMERIC);
            }
            ps.setString(7, familyMember.getOccupation());
            return ps;
        }, keyHolder);

        return keyHolder.getKey().longValue();
    }

    public int update(Long id, FamilyMember familyMember) {
        String sql = "UPDATE family_member SET employee_id = ?, first_name = ?, last_name = ?, relation = ?, date_of_birth = ?, dependent = ?, occupation = ? WHERE id = ?";
        return jdbcTemplate.update(sql,
                familyMember.getEmployeeId(),
                familyMember.getFirstName(),
                familyMember.getLastName(),
                familyMember.getRelation(),
                familyMember.getDateOfBirth() != null ? new java.sql.Date(familyMember.getDateOfBirth().getTime()) : null,
                familyMember.getDependent(),
                familyMember.getOccupation(),
                id);
    }

    public int deleteById(Long id) {
        String sql = "DELETE FROM family_member WHERE id = ?";
        return jdbcTemplate.update(sql, id);
    }
}