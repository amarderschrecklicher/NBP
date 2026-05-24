package ba.unsa.etf.employeemanagement.mapper;

import ba.unsa.etf.employeemanagement.dto.response.VacationRequestOverviewResponse;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class VacationRequestOverviewMapper implements RowMapper<VacationRequestOverviewResponse> {

    @Override
    public VacationRequestOverviewResponse mapRow(ResultSet rs, int rowNum) throws SQLException {
        return VacationRequestOverviewResponse.builder()
                .vacationId(rs.getLong("VACATION_ID"))
                .employeeId(rs.getLong("EMPLOYEE_ID"))
                .employeeFullName(rs.getString("EMPLOYEE_FULL_NAME"))
                .employeeEmail(rs.getString("EMPLOYEE_EMAIL"))
                .departmentName(rs.getString("DEPARTMENT_NAME"))
                .jobTitle(rs.getString("JOB_TITLE"))
                .startDate(rs.getDate("START_DATE"))
                .endDate(rs.getDate("END_DATE"))
                .totalDays(rs.getObject("TOTAL_DAYS") != null ? rs.getLong("TOTAL_DAYS") : null)
                .vacationYear(rs.getObject("VACATION_YEAR") != null ? rs.getInt("VACATION_YEAR") : null)
                .vacationMonth(rs.getObject("VACATION_MONTH") != null ? rs.getInt("VACATION_MONTH") : null)
                .vacationType(rs.getString("VACATION_TYPE"))
                .status(rs.getString("STATUS"))
                .approvedBy(rs.getObject("APPROVED_BY") != null ? rs.getLong("APPROVED_BY") : null)
                .approvedByFullName(rs.getString("APPROVED_BY_FULL_NAME"))
                .workflowBucket(rs.getString("WORKFLOW_BUCKET"))
                .requestsThisYear(rs.getObject("REQUESTS_THIS_YEAR") != null ? rs.getLong("REQUESTS_THIS_YEAR") : null)
                .requestedDaysThisYear(rs.getObject("REQUESTED_DAYS_THIS_YEAR") != null ? rs.getLong("REQUESTED_DAYS_THIS_YEAR") : null)
                .build();
    }
}

