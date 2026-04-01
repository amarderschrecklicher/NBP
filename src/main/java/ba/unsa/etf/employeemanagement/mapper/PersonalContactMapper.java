package ba.unsa.etf.employeemanagement.mapper;

import ba.unsa.etf.employeemanagement.dto.request.PersonalContactRequest;
import ba.unsa.etf.employeemanagement.dto.response.PersonalContactResponse;
import ba.unsa.etf.employeemanagement.model.PersonalContact;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class PersonalContactMapper implements RowMapper<PersonalContact> {

    @Override
    public PersonalContact mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new PersonalContact(
                rs.getLong("id"),
                rs.getLong("employee_id"),
                rs.getString("phone_number"),
                rs.getString("personal_email")
        );
    }

    public PersonalContact mapToEntity(PersonalContactRequest request) {
        return new PersonalContact(
                null,
                request.getEmployeeId(),
                request.getPhoneNumber(),
                request.getPersonalEmail()
        );
    }

    public PersonalContactResponse mapToResponse(PersonalContact entity) {
        return PersonalContactResponse.builder()
                .id(entity.getId())
                .employeeId(entity.getEmployeeId())
                .phoneNumber(entity.getPhoneNumber())
                .personalEmail(entity.getPersonalEmail())
                .build();
    }
}