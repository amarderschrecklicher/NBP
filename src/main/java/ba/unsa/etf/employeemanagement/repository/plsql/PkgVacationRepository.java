package ba.unsa.etf.employeemanagement.repository.plsql;

import ba.unsa.etf.employeemanagement.dto.plsql.RemainingVacationDays;
import ba.unsa.etf.employeemanagement.util.OraclePlsqlErrors;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.SqlOutParameter;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.sql.Types;
import java.time.LocalDate;
import java.util.Map;

@Repository
@RequiredArgsConstructor
public class PkgVacationRepository {

    private final JdbcTemplate jdbcTemplate;

    private SimpleJdbcCall submitRequestCall;
    private SimpleJdbcCall decideVacationCall;
    private SimpleJdbcCall remainingDaysCall;

    @PostConstruct
    void init() {
        submitRequestCall = new SimpleJdbcCall(jdbcTemplate)
                .withCatalogName("PKG_VACATION")
                .withProcedureName("SUBMIT_VACATION_REQUEST")
                .withoutProcedureColumnMetaDataAccess()
                .declareParameters(
                        new SqlParameter("P_EMPLOYEE_ID", Types.NUMERIC),
                        new SqlParameter("P_START_DATE", Types.DATE),
                        new SqlParameter("P_END_DATE", Types.DATE),
                        new SqlParameter("P_VACATION_TYPE", Types.VARCHAR),
                        new SqlParameter("P_REASON", Types.VARCHAR),
                        new SqlOutParameter("P_VACATION_ID", Types.NUMERIC)
                );

        decideVacationCall = new SimpleJdbcCall(jdbcTemplate)
                .withCatalogName("PKG_VACATION")
                .withProcedureName("DECIDE_VACATION")
                .withoutProcedureColumnMetaDataAccess()
                .declareParameters(
                        new SqlParameter("P_VACATION_ID", Types.NUMERIC),
                        new SqlParameter("P_APPROVER_ID", Types.NUMERIC),
                        new SqlParameter("P_APPROVE", Types.NUMERIC),
                        new SqlParameter("P_REASON", Types.VARCHAR)
                );

        remainingDaysCall = new SimpleJdbcCall(jdbcTemplate)
                .withCatalogName("PKG_VACATION")
                .withProcedureName("CALCULATE_REMAINING_DAYS")
                .withoutProcedureColumnMetaDataAccess()
                .declareParameters(
                        new SqlParameter("P_EMPLOYEE_ID", Types.NUMERIC),
                        new SqlParameter("P_YEAR", Types.NUMERIC),
                        new SqlOutParameter("P_USED_DAYS", Types.NUMERIC),
                        new SqlOutParameter("P_REMAINING_DAYS", Types.NUMERIC)
                );
    }

    public Long submitVacationRequest(
            Long employeeId,
            LocalDate startDate,
            LocalDate endDate,
            String vacationType,
            String reason
    ) {
        try {
            Map<String, Object> out = submitRequestCall.execute(
                    employeeId,
                    Date.valueOf(startDate),
                    Date.valueOf(endDate),
                    vacationType,
                    reason
            );
            return ((Number) out.get("P_VACATION_ID")).longValue();
        } catch (DataAccessException ex) {
            throw OraclePlsqlErrors.translate(ex);
        }
    }

    public void decideVacation(Long vacationId, Long approverId, boolean approve, String reason) {
        try {
            decideVacationCall.execute(vacationId, approverId, approve ? 1 : 0, reason);
        } catch (DataAccessException ex) {
            throw OraclePlsqlErrors.translate(ex);
        }
    }

    public RemainingVacationDays calculateRemainingDays(Long employeeId, Integer year) {
        try {
            Map<String, Object> out = remainingDaysCall.execute(employeeId, year);
            int used = ((Number) out.get("P_USED_DAYS")).intValue();
            int remaining = ((Number) out.get("P_REMAINING_DAYS")).intValue();
            int resolvedYear = year != null ? year : LocalDate.now().getYear();
            return new RemainingVacationDays(employeeId, resolvedYear, used, remaining);
        } catch (DataAccessException ex) {
            throw OraclePlsqlErrors.translate(ex);
        }
    }
}
