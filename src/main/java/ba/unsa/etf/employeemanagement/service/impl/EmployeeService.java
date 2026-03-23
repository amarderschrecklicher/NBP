package ba.unsa.etf.employeemanagement.service.impl;

import ba.unsa.etf.employeemanagement.dto.request.EmployeeRequest;
import ba.unsa.etf.employeemanagement.dto.response.EmployeeResponse;
import ba.unsa.etf.employeemanagement.exceptions.ResourceNotFoundException;
import ba.unsa.etf.employeemanagement.mapper.EmployeeMapper;
import ba.unsa.etf.employeemanagement.model.Employee;
import ba.unsa.etf.employeemanagement.repository.EmployeeRepository;
import ba.unsa.etf.employeemanagement.service.api.IEmployeeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EmployeeService implements IEmployeeService {

    private final EmployeeRepository employeeRepository;
    private  final EmployeeMapper employeeMapper;

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
    public EmployeeResponse save(EmployeeRequest request) {
        Employee employee = employeeMapper.mapToEntity(request);
        Long generatedId = employeeRepository.save(employee);
        Employee saved = employeeRepository.findById(generatedId)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found after save"));
        return employeeMapper.mapToResponse(saved);
    }

    @Override
    public EmployeeResponse update(Long id, EmployeeRequest request) {
        Employee existing = employeeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found with id: " + id));

        Employee employee = employeeMapper.mapToEntity(request);
        employee.setId(existing.getId());

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
