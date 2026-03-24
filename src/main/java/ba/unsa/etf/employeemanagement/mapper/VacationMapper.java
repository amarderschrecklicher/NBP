package ba.unsa.etf.employeemanagement.mapper;

import ba.unsa.etf.employeemanagement.dto.request.VacationRequest;
import ba.unsa.etf.employeemanagement.dto.response.VacationResponse;
import ba.unsa.etf.employeemanagement.model.Vacation;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class VacationMapper implements RowMapper<Vacation> {
    @Override
    public Vacation mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new Vacation(rs.getLong("id"), rs.getLong("employee_id"), rs.getDate("start_date"), rs.getDate("end_date"), rs.getString("vacation_type"), rs.getString("status"), rs.getLong("approved_by"), rs.getString("reason"));
    }

    public Vacation mapToEntity(VacationRequest request) {
        return new Vacation(null, request.getEmployeeId(), request.getStartDate(), request.getEndDate(), request.getVacationType(), request.getStatus(), request.getApprovedBy(), request.getReason());
    }

    public VacationResponse mapToResponse(Vacation entity) {
        return VacationResponse.builder().id(entity.getId()).employeeId(entity.getEmployeeId()).startDate(entity.getStartDate()).endDate(entity.getEndDate()).vacationType(entity.getVacationType()).status(entity.getStatus()).approvedBy(entity.getApprovedBy()).reason(entity.getReason()).build();
    }
}

