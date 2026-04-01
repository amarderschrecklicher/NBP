package ba.unsa.etf.employeemanagement.mapper;

import ba.unsa.etf.employeemanagement.dto.request.EmergencyContactRequest;
import ba.unsa.etf.employeemanagement.dto.response.EmergencyContactResponse;
import ba.unsa.etf.employeemanagement.model.EmergencyContact;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class EmergencyContactMapper implements RowMapper<EmergencyContact> {

    @Override
    public EmergencyContact mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new EmergencyContact(
                rs.getLong("id"),
                rs.getLong("employee_id"),
                rs.getString("first_name"),
                rs.getString("last_name"),
                rs.getString("relationship"),
                rs.getString("phone_number"),
                rs.getString("email"),
                rs.getString("address")
        );
    }

    public EmergencyContact mapToEntity(EmergencyContactRequest request) {
        return new EmergencyContact(
                null,
                request.getEmployeeId(),
                request.getFirstName(),
                request.getLastName(),
                request.getRelationship(),
                request.getPhoneNumber(),
                request.getEmail(),
                request.getAddress()
        );
    }

    public EmergencyContactResponse mapToResponse(EmergencyContact entity) {
        return EmergencyContactResponse.builder()
                .id(entity.getId())
                .employeeId(entity.getEmployeeId())
                .firstName(entity.getFirstName())
                .lastName(entity.getLastName())
                .relationship(entity.getRelationship())
                .phoneNumber(entity.getPhoneNumber())
                .email(entity.getEmail())
                .address(entity.getAddress())
                .build();
    }
}