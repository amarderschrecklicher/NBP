package ba.unsa.etf.employeemanagement.mapper;

import ba.unsa.etf.employeemanagement.dto.request.DepartmentRequest;
import ba.unsa.etf.employeemanagement.dto.response.DepartmentResponse;
import ba.unsa.etf.employeemanagement.model.Department;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class DepartmentMapper implements RowMapper<Department> {
    @Override
    public Department mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new Department(rs.getLong("id"), rs.getString("name"), rs.getString("description"));
    }

    public Department mapToEntity(DepartmentRequest request) {
        return new Department(null, request.getName(), request.getDescription());
    }

    public DepartmentResponse mapToResponse(Department entity) {
        return DepartmentResponse.builder().id(entity.getId()).name(entity.getName()).description(entity.getDescription()).build();
    }
}

