package ba.unsa.etf.employeemanagement.service.impl;

import ba.unsa.etf.employeemanagement.dto.response.EmployeeWorkProfileResponse;
import ba.unsa.etf.employeemanagement.repository.EmployeeWorkProfileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class EmployeeWorkProfileService {

    private final EmployeeWorkProfileRepository repository;

    public List<EmployeeWorkProfileResponse> getAllProfiles() {
        return repository.findAll();
    }

    public Optional<EmployeeWorkProfileResponse> getProfileByEmployeeId(Long employeeId) {
        return repository.findByEmployeeId(employeeId);
    }

    public List<EmployeeWorkProfileResponse> getActiveEmployees() {
        return repository.findActiveEmployees();
    }

    public List<EmployeeWorkProfileResponse> getProfilesByDepartmentId(Long departmentId) {
        return repository.findByDepartmentId(departmentId);
    }

    public List<EmployeeWorkProfileResponse> getDirectReports(Long managerId) {
        return repository.findByManagerId(managerId);
    }
}

