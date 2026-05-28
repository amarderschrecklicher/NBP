package ba.unsa.etf.employeemanagement.service.impl;

import ba.unsa.etf.employeemanagement.dto.response.PayrollCostOverviewResponse;
import ba.unsa.etf.employeemanagement.repository.PayrollCostOverviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PayrollCostOverviewService {

    private final PayrollCostOverviewRepository repository;

    public List<PayrollCostOverviewResponse> getAllPayrollRecords() {
        return repository.findAll();
    }

    public Optional<PayrollCostOverviewResponse> getPayrollByEmployeeId(Long employeeId) {
        return repository.findByEmployeeId(employeeId);
    }

    public List<PayrollCostOverviewResponse> getPayrollByDepartmentId(Long departmentId) {
        return repository.findByDepartmentId(departmentId);
    }

    public List<PayrollCostOverviewResponse> getBonusEligibleEmployees() {
        return repository.findBonusEligible();
    }

    public List<PayrollCostOverviewResponse> getPayrollByCompensationBand(String band) {
        return repository.findByCompensationBand(band);
    }

    public List<PayrollCostOverviewResponse> getUnspecifiedSalaryRecords() {
        return repository.findWithUnspecifiedSalary();
    }
}

