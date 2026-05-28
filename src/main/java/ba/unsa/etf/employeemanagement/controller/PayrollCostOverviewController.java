package ba.unsa.etf.employeemanagement.controller;

import ba.unsa.etf.employeemanagement.dto.response.PayrollCostOverviewResponse;
import ba.unsa.etf.employeemanagement.service.impl.PayrollCostOverviewService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/views/payroll-cost")
@RequiredArgsConstructor
@Tag(name = "Payroll Cost Overview", description = "View endpoints for payroll and compensation analysis with salary normalization and compensation bands")
public class PayrollCostOverviewController {

    private final PayrollCostOverviewService service;

    @GetMapping
    @Operation(
            summary = "Get all payroll records",
            description = "Returns complete payroll profiles for all employees including salary, payment frequency, annual equivalent, and compensation band from VW_PAYROLL_COST_OVERVIEW view"
    )
    public ResponseEntity<List<PayrollCostOverviewResponse>> getAllPayrollRecords() {
        return ResponseEntity.ok(service.getAllPayrollRecords());
    }

    @GetMapping("/employee/{employeeId}")
    @Operation(
            summary = "Get payroll record by employee ID",
            description = "Returns compensation profile including salary, payment frequency, annual salary equivalent, bonus eligibility, and compensation band"
    )
    public ResponseEntity<PayrollCostOverviewResponse> getPayrollByEmployeeId(@PathVariable Long employeeId) {
        return service.getPayrollByEmployeeId(employeeId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/department/{departmentId}")
    @Operation(
            summary = "Get payroll records by department",
            description = "Returns aggregated payroll data for all employees in a department for cost-center and budget analysis"
    )
    public ResponseEntity<List<PayrollCostOverviewResponse>> getPayrollByDepartment(@PathVariable Long departmentId) {
        return ResponseEntity.ok(service.getPayrollByDepartmentId(departmentId));
    }

    @GetMapping("/bonus-eligible")
    @Operation(
            summary = "Get all bonus-eligible employees",
            description = "Returns payroll records for employees where bonus_eligible_flag = 'YES' for bonus planning"
    )
    public ResponseEntity<List<PayrollCostOverviewResponse>> getBonusEligible() {
        return ResponseEntity.ok(service.getBonusEligibleEmployees());
    }

    @GetMapping("/compensation-band/{band}")
    @Operation(
            summary = "Get employees by compensation band",
            description = "Returns payroll profiles filtered by compensation band (ENTRY_LEVEL, MID_MARKET, SENIOR_COST) for equity analysis"
    )
    public ResponseEntity<List<PayrollCostOverviewResponse>> getByCompensationBand(
            @PathVariable String band
    ) {
        return ResponseEntity.ok(service.getPayrollByCompensationBand(band));
    }

    @GetMapping("/unspecified-salary")
    @Operation(
            summary = "Get employees with unspecified salary",
            description = "Returns payroll records with NULL salary for data quality audits"
    )
    public ResponseEntity<List<PayrollCostOverviewResponse>> getUnspecifiedSalary() {
        return ResponseEntity.ok(service.getUnspecifiedSalaryRecords());
    }
}

