package ba.unsa.etf.employeemanagement.service.impl;

import ba.unsa.etf.employeemanagement.dto.response.VacationRequestOverviewResponse;
import ba.unsa.etf.employeemanagement.repository.VacationRequestOverviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class VacationRequestOverviewService {

    private final VacationRequestOverviewRepository repository;

    public List<VacationRequestOverviewResponse> getAllVacationRequests() {
        return repository.findAll();
    }

    public Optional<VacationRequestOverviewResponse> getVacationRequestById(Long vacationId) {
        return repository.findByVacationId(vacationId);
    }

    public List<VacationRequestOverviewResponse> getVacationsByEmployeeId(Long employeeId) {
        return repository.findByEmployeeId(employeeId);
    }

    public List<VacationRequestOverviewResponse> getOpenRequests() {
        return repository.findOpenRequests();
    }

    public List<VacationRequestOverviewResponse> getApprovedRequests() {
        return repository.findApprovedRequests();
    }

    public List<VacationRequestOverviewResponse> getVacationsByYear(Integer year) {
        return repository.findByYear(year);
    }

    public List<VacationRequestOverviewResponse> getVacationsByDepartment(String departmentName) {
        return repository.findByDepartmentName(departmentName);
    }

    public List<VacationRequestOverviewResponse> getPendingApprovals() {
        return repository.findPendingApprovalByApproverId(null);
    }
}

