package ba.unsa.etf.employeemanagement.mapper;

import ba.unsa.etf.employeemanagement.dto.request.WorkPermitRequest;
import ba.unsa.etf.employeemanagement.dto.response.WorkPermitResponse;
import ba.unsa.etf.employeemanagement.model.WorkPermit;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class WorkPermitMapper implements RowMapper<WorkPermit> {

    @Override
    public WorkPermit mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new WorkPermit(
                rs.getLong("id"),
                rs.getLong("employee_id"),
                rs.getString("permit_number"),
                rs.getString("permit_type"),
                rs.getString("issuing_country"),
                rs.getDate("issue_date"),
                rs.getDate("expiry_date"),
                rs.getString("status")
        );
    }

    public WorkPermit mapToEntity(WorkPermitRequest request) {
        return new WorkPermit(
                null,
                request.getEmployeeId(),
                request.getPermitNumber(),
                request.getPermitType(),
                request.getIssuingCountry(),
                request.getIssueDate(),
                request.getExpiryDate(),
                request.getStatus()
        );
    }

    public WorkPermitResponse mapToResponse(WorkPermit entity) {
        return WorkPermitResponse.builder()
                .id(entity.getId())
                .employeeId(entity.getEmployeeId())
                .permitNumber(entity.getPermitNumber())
                .permitType(entity.getPermitType())
                .issuingCountry(entity.getIssuingCountry())
                .issueDate(entity.getIssueDate())
                .expiryDate(entity.getExpiryDate())
                .status(entity.getStatus())
                .build();
    }
}
