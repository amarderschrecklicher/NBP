package ba.unsa.etf.employeemanagement.controller;

import ba.unsa.etf.employeemanagement.dto.response.VacationRequestOverviewResponse;
import ba.unsa.etf.employeemanagement.service.impl.VacationRequestOverviewService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/views/vacation-requests")
@RequiredArgsConstructor
@Tag(name = "Vacation Request Overview", description = "View endpoints for vacation request analytics with approval workflows, year-to-date aggregates, and coverage planning")
public class VacationRequestOverviewController {

    private final VacationRequestOverviewService service;

    @GetMapping
    @Operation(
            summary = "Get all vacation requests",
            description = "Returns all vacation requests with employee, dates, approver details, total days, and year-to-date aggregates (requests_this_year, requested_days_this_year) from VW_VACATION_REQUEST_OVERVIEW view"
    )
    public ResponseEntity<List<VacationRequestOverviewResponse>> getAllVacationRequests() {
        return ResponseEntity.ok(service.getAllVacationRequests());
    }

    @GetMapping("/{vacationId}")
    @Operation(
            summary = "Get vacation request by ID",
            description = "Returns complete vacation details including duration, approver information, and year-to-date aggregates"
    )
    public ResponseEntity<VacationRequestOverviewResponse> getVacationRequestById(@PathVariable Long vacationId) {
        return service.getVacationRequestById(vacationId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/employee/{employeeId}")
    @Operation(
            summary = "Get vacation requests by employee",
            description = "Returns all vacation requests for a specific employee showing history, aggregates, and year-to-date totals"
    )
    public ResponseEntity<List<VacationRequestOverviewResponse>> getVacationsByEmployee(@PathVariable Long employeeId) {
        return ResponseEntity.ok(service.getVacationsByEmployeeId(employeeId));
    }

    @GetMapping("/open-requests")
    @Operation(
            summary = "Get all open vacation requests",
            description = "Returns pending vacation requests (workflow_bucket = 'OPEN') for approval workflows"
    )
    public ResponseEntity<List<VacationRequestOverviewResponse>> getOpenRequests() {
        return ResponseEntity.ok(service.getOpenRequests());
    }

    @GetMapping("/approved-requests")
    @Operation(
            summary = "Get all approved vacation requests",
            description = "Returns approved vacation requests for department coverage planning and resource allocation"
    )
    public ResponseEntity<List<VacationRequestOverviewResponse>> getApprovedRequests() {
        return ResponseEntity.ok(service.getApprovedRequests());
    }

    @GetMapping("/year/{year}")
    @Operation(
            summary = "Get vacation requests by year",
            description = "Returns all vacation requests for a specific calendar year for annual compliance audits and policy enforcement"
    )
    public ResponseEntity<List<VacationRequestOverviewResponse>> getVacationsByYear(@PathVariable Integer year) {
        return ResponseEntity.ok(service.getVacationsByYear(year));
    }

    @GetMapping("/department/{departmentName}")
    @Operation(
            summary = "Get vacation requests by department",
            description = "Returns all vacation requests for a department showing coverage gaps and resource planning needs"
    )
    public ResponseEntity<List<VacationRequestOverviewResponse>> getVacationsByDepartment(@PathVariable String departmentName) {
        return ResponseEntity.ok(service.getVacationsByDepartment(departmentName));
    }

    @GetMapping("/pending-approvals")
    @Operation(
            summary = "Get pending approvals",
            description = "Returns vacation requests awaiting approval for manager review queues"
    )
    public ResponseEntity<List<VacationRequestOverviewResponse>> getPendingApprovals() {
        return ResponseEntity.ok(service.getPendingApprovals());
    }
}

