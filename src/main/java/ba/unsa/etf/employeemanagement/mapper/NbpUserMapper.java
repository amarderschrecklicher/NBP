package ba.unsa.etf.employeemanagement.mapper;

import ba.unsa.etf.employeemanagement.dto.request.NbpUserRequest;
import ba.unsa.etf.employeemanagement.dto.response.NbpUserResponse;
import ba.unsa.etf.employeemanagement.model.NbpUser;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class NbpUserMapper implements RowMapper<NbpUser> {

    @Override
    public NbpUser mapRow(ResultSet rs, int rowNum) throws SQLException {
        Long addressId = rs.getObject("ADDRESS_ID") != null ? rs.getLong("ADDRESS_ID") : null;
        return new NbpUser(
                rs.getLong("ID"),
                rs.getString("FIRST_NAME"),
                rs.getString("LAST_NAME"),
                rs.getString("EMAIL"),
                rs.getString("PASSWORD"),
                rs.getString("USERNAME"),
                rs.getString("PHONE_NUMBER"),
                rs.getDate("BIRTH_DATE"),
                addressId,
                rs.getLong("ROLE_ID")
        );
    }

    public NbpUser mapToEntity(NbpUserRequest request) {
        return new NbpUser(
                null,
                request.getFirstName(),
                request.getLastName(),
                request.getEmail(),
                request.getPassword(),
                request.getUsername(),
                request.getPhoneNumber(),
                request.getBirthDate(),
                request.getAddressId(),
                request.getRoleId()
        );
    }

    public NbpUser mapToEntity(NbpUserResponse dto) {
        if (dto == null) {
            return null;
        }
        return new NbpUser(
                dto.getId(),
                dto.getFirstName(),
                dto.getLastName(),
                dto.getEmail(),
                dto.getPassword(),
                dto.getUsername(),
                dto.getPhoneNumber(),
                dto.getBirthDate(),
                dto.getAddressId(),
                dto.getRoleId()
        );
    }

    public NbpUserResponse mapToResponse(NbpUser entity) {
        return NbpUserResponse.builder()
                .id(entity.getId())
                .firstName(entity.getFirstName())
                .lastName(entity.getLastName())
                .email(entity.getEmail())
                .password(entity.getPassword())
                .username(entity.getUsername())
                .phoneNumber(entity.getPhoneNumber())
                .birthDate(entity.getBirthDate())
                .addressId(entity.getAddressId())
                .roleId(entity.getRoleId())
                .build();
    }
}

