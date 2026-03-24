package ba.unsa.etf.employeemanagement.mapper;

import ba.unsa.etf.employeemanagement.dto.request.HolidayRequest;
import ba.unsa.etf.employeemanagement.dto.response.HolidayResponse;
import ba.unsa.etf.employeemanagement.model.Holiday;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class HolidayMapper implements RowMapper<Holiday> {
    @Override
    public Holiday mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new Holiday(rs.getLong("id"), rs.getString("holiday_name"), rs.getDate("holiday_date"), rs.getString("country"), rs.getString("description"));
    }

    public Holiday mapToEntity(HolidayRequest request) {
        return new Holiday(null, request.getHolidayName(), request.getHolidayDate(), request.getCountry(), request.getDescription());
    }

    public HolidayResponse mapToResponse(Holiday entity) {
        return HolidayResponse.builder().id(entity.getId()).holidayName(entity.getHolidayName()).holidayDate(entity.getHolidayDate()).country(entity.getCountry()).description(entity.getDescription()).build();
    }
}

