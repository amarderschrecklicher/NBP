package ba.unsa.etf.employeemanagement.mapper;

import ba.unsa.etf.employeemanagement.dto.request.FamilyMemberRequest;
import ba.unsa.etf.employeemanagement.dto.response.FamilyMemberResponse;
import ba.unsa.etf.employeemanagement.model.FamilyMember;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class FamilyMemberMapper implements RowMapper<FamilyMember> {

    @Override
    public FamilyMember mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new FamilyMember(
                rs.getLong("id"),
                rs.getLong("employee_id"),
                rs.getString("first_name"),
                rs.getString("last_name"),
                rs.getString("relation"),
                rs.getDate("date_of_birth"),
                rs.getInt("dependent"),
                rs.getString("occupation")
        );
    }

    public FamilyMember mapToEntity(FamilyMemberRequest request) {
        return new FamilyMember(
                null,
                request.getEmployeeId(),
                request.getFirstName(),
                request.getLastName(),
                request.getRelation(),
                request.getDateOfBirth(),
                request.getDependent(),
                request.getOccupation()
        );
    }

    public FamilyMemberResponse mapToResponse(FamilyMember entity) {
        return FamilyMemberResponse.builder()
                .id(entity.getId())
                .employeeId(entity.getEmployeeId())
                .firstName(entity.getFirstName())
                .lastName(entity.getLastName())
                .relation(entity.getRelation())
                .dateOfBirth(entity.getDateOfBirth())
                .dependent(entity.getDependent())
                .occupation(entity.getOccupation())
                .build();
    }
}