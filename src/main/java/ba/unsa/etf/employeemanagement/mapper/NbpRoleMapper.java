package ba.unsa.etf.employeemanagement.mapper;

import ba.unsa.etf.employeemanagement.dto.request.NbpRoleRequest;
import ba.unsa.etf.employeemanagement.dto.response.NbpRoleResponse;
import ba.unsa.etf.employeemanagement.model.NbpRole;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class NbpRoleMapper implements RowMapper<NbpRole> {

    @Override
    public NbpRole mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new NbpRole(rs.getLong("ID"), rs.getString("NAME"));
    }

    public NbpRole mapToEntity(NbpRoleRequest request) {
        return new NbpRole(null, request.getName());
    }

    public NbpRole mapToEntity(NbpRoleResponse dto) {
        if (dto == null) {
            return null;
        }
        return new NbpRole(dto.getId(), dto.getName());
    }

    public NbpRoleResponse mapToResponse(NbpRole entity) {
        return NbpRoleResponse.builder()
                .id(entity.getId())
                .name(entity.getName())
                .build();
    }
}

