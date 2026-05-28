package ba.unsa.etf.employeemanagement.service.impl.plsql;

import ba.unsa.etf.employeemanagement.dto.plsql.AddEmployeePlsqlRequest;
import ba.unsa.etf.employeemanagement.dto.response.EmployeeResponse;
import ba.unsa.etf.employeemanagement.exceptions.BadRequestException;
import ba.unsa.etf.employeemanagement.exceptions.ResourceNotFoundException;
import ba.unsa.etf.employeemanagement.mapper.EmployeeMapper;
import ba.unsa.etf.employeemanagement.mapper.EmploymentMapper;
import ba.unsa.etf.employeemanagement.mapper.nbp.NbpUserMapper;
import ba.unsa.etf.employeemanagement.model.Employee;
import ba.unsa.etf.employeemanagement.repository.EmployeeRepository;
import ba.unsa.etf.employeemanagement.repository.EmploymentRepository;
import ba.unsa.etf.employeemanagement.repository.nbp.NbpUserRepository;
import ba.unsa.etf.employeemanagement.repository.plsql.PkgEmployeeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.ZoneId;

@Service
@RequiredArgsConstructor
public class PlsqlEmployeeService {

    private final PkgEmployeeRepository pkgEmployeeRepository;
    private final EmployeeRepository employeeRepository;
    private final EmploymentRepository employmentRepository;
    private final NbpUserRepository nbpUserRepository;
    private final EmployeeMapper employeeMapper;
    private final EmploymentMapper employmentMapper;
    private final NbpUserMapper nbpUserMapper;

    public EmployeeResponse addEmployee(AddEmployeePlsqlRequest request) {
        var result = pkgEmployeeRepository.addEmployee(
                request.getUserId(),
                request.getGender(),
                request.getNationality(),
                request.getMaritalStatus(),
                request.getManagerId(),
                request.getEmploymentNumber(),
                request.getHireDate() != null
                        ? request.getHireDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate()
                        : null,
                request.getJobTitle(),
                request.getEmploymentType(),
                request.getDepartmentId()
        );
        return buildFullResponse(result.employeeId());
    }

    public EmployeeResponse updateEmployment(Long employeeId, String status, Long departmentId) {
        if (status == null && departmentId == null) {
            throw new BadRequestException("Provide at least status or departmentId to update.");
        }
        pkgEmployeeRepository.updateEmployment(employeeId, status, departmentId);
        return buildFullResponse(employeeId);
    }

    public EmployeeResponse archiveEmployee(Long employeeId, boolean hardDelete) {
        pkgEmployeeRepository.archiveEmployee(employeeId, hardDelete);
        if (hardDelete) {
            return null;
        }
        return buildFullResponse(employeeId);
    }

    private EmployeeResponse buildFullResponse(Long employeeId) {
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found with id: " + employeeId));

        EmployeeResponse response = employeeMapper.mapToResponse(employee);

        employmentRepository.findByEmployeeId(employeeId)
                .map(employmentMapper::mapToResponse)
                .ifPresent(response::setEmployment);

        nbpUserRepository.findById(employee.getUserId())
                .map(nbpUserMapper::mapToResponse)
                .ifPresent(response::setUser);

        return response;
    }
}
