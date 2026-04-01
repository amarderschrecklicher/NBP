package ba.unsa.etf.employeemanagement.service.impl;

import ba.unsa.etf.employeemanagement.dto.request.EmployeeRequest;
import ba.unsa.etf.employeemanagement.dto.response.EmployeeResponse;
import ba.unsa.etf.employeemanagement.dto.response.EmploymentResponse;
import ba.unsa.etf.employeemanagement.dto.response.NbpUserResponse;
import ba.unsa.etf.employeemanagement.exceptions.ResourceNotFoundException;
import ba.unsa.etf.employeemanagement.mapper.EmployeeMapper;
import ba.unsa.etf.employeemanagement.mapper.EmploymentMapper;
import ba.unsa.etf.employeemanagement.mapper.NbpUserMapper;
import ba.unsa.etf.employeemanagement.model.*;
import ba.unsa.etf.employeemanagement.repository.*;
import ba.unsa.etf.employeemanagement.service.api.IEmployeeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EmployeeService implements IEmployeeService {

    private final EmployeeRepository employeeRepository;
    private final EmployeeMapper employeeMapper;
    private final AddressRepository addressRepository;
    private final NbpUserRepository nbpUserRepository;
    private final NbpUserMapper nbpUserMapper;
    private final DepartmentRepository departmentRepository;
    private final EmploymentRepository employmentRepository;
    private final EmploymentMapper employmentMapper;

    @Override
    public List<EmployeeResponse> findAll() {
        return employeeRepository.findAll()
                .stream()
                .map(employeeMapper::mapToResponse)
                .toList();
    }

    @Override
    public EmployeeResponse findById(Long id) {
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found with id: " + id));
        return employeeMapper.mapToResponse(employee);
    }

    @Override
    @Transactional
    public EmployeeResponse save(EmployeeRequest request) {
        // 1. Create Address
        Address address = Address.builder()
                .street(request.getStreet())
                .city(request.getCity())
                .postalCode(request.getPostalCode())
                .country(request.getCountry())
                .build();
        Address savedAddress = addressRepository.save(address);

        // 2. Create NbpUser
        NbpUser nbpUser = new NbpUser(
                null,
                request.getFirstName(),
                request.getLastName(),
                request.getEmail(),
                request.getPassword(),
                request.getUsername(),
                request.getPhoneNumber(),
                request.getBirthDate(),
                savedAddress.getId(),
                request.getRoleId()
        );
        Long userId = nbpUserRepository.save(nbpUser);
        NbpUser savedUser = nbpUserRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("NbpUser not found after save"));

        // 3. Create Employee
        Employee employee = employeeMapper.mapToEntity(request);
        employee.setUserId(userId);
        Long employeeId = employeeRepository.save(employee);
        Employee savedEmployee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found after save"));

        // 4. Resolve or create Department
        Long departmentId = request.getDepartmentId();
        if (departmentId == null && request.getDepartmentName() != null) {
            Department department = new Department(null, request.getDepartmentName(), request.getDepartmentDescription());
            departmentId = departmentRepository.save(department);
        }

        // 5. Create Employment
        EmploymentResponse employmentResponse = null;
        if (departmentId != null) {
            Employment employment = new Employment(
                    null,
                    employeeId,
                    request.getEmploymentNumber(),
                    request.getHireDate(),
                    request.getTerminationDate(),
                    request.getJobTitle(),
                    request.getEmploymentType(),
                    request.getEmploymentStatus(),
                    departmentId
            );
            Long employmentId = employmentRepository.save(employment);
            Employment savedEmployment = employmentRepository.findById(employmentId)
                    .orElseThrow(() -> new ResourceNotFoundException("Employment not found after save"));
            employmentResponse = employmentMapper.mapToResponse(savedEmployment);
        }

        // Build response
        NbpUserResponse userResponse = nbpUserMapper.mapToResponse(savedUser);
        EmployeeResponse response = employeeMapper.mapToResponse(savedEmployee);
        response.setUser(userResponse);
        response.setEmployment(employmentResponse);
        return response;
    }

    @Override
    public EmployeeResponse update(Long id, EmployeeRequest request) {
        Employee existing = employeeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found with id: " + id));

        Employee employee = employeeMapper.mapToEntity(request);
        employee.setId(existing.getId());
        employee.setUserId(existing.getUserId());

        employeeRepository.update(id, employee);

        Employee updated = employeeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found after update"));

        return employeeMapper.mapToResponse(updated);
    }

    @Override
    public void delete(Long id) {
        employeeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found with id: " + id));

        employeeRepository.deleteById(id);
    }

}
