package ba.unsa.etf.employeemanagement.controller;

import ba.unsa.etf.employeemanagement.dto.response.EmployeeWorkProfileResponse;
import ba.unsa.etf.employeemanagement.service.impl.EmployeeWorkProfileService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/views/employee-profiles")
@RequiredArgsConstructor
@Tag(name = "Employee Work Profiles", description = "View endpoints for HR employee profiles with employment and organizational context")
public class EmployeeWorkProfileController {

    private final EmployeeWorkProfileService service;

    @GetMapping
    @Operation(
            summary = "Get all employee work profiles",
            description = "Returns comprehensive HR profiles for all employees including identity, employment, manager, and department information from VW_EMPLOYEE_WORK_PROFILE view"
    )
    public ResponseEntity<List<EmployeeWorkProfileResponse>> getAllProfiles() {
        return ResponseEntity.ok(service.getAllProfiles());
    }

    @GetMapping("/{employeeId}")
    @Operation(
            summary = "Get employee work profile by ID",
            description = "Returns complete HR profile for a single employee with manager hierarchy and employment details"
    )
    public ResponseEntity<EmployeeWorkProfileResponse> getProfileByEmployeeId(@PathVariable Long employeeId) {
        return service.getProfileByEmployeeId(employeeId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/active")
    @Operation(
            summary = "Get all active employees",
            description = "Returns HR profiles for all currently employed staff (lifecycle_state = 'ACTIVE')"
    )
    public ResponseEntity<List<EmployeeWorkProfileResponse>> getActiveEmployees() {
        return ResponseEntity.ok(service.getActiveEmployees());
    }

    @GetMapping("/department/{departmentId}")
    @Operation(
            summary = "Get employees by department",
            description = "Returns HR profiles for all employees in a specific department"
    )
    public ResponseEntity<List<EmployeeWorkProfileResponse>> getProfilesByDepartment(@PathVariable Long departmentId) {
        return ResponseEntity.ok(service.getProfilesByDepartmentId(departmentId));
    }

    @GetMapping("/manager/{managerId}/direct-reports")
    @Operation(
            summary = "Get direct reports for a manager",
            description = "Returns HR profiles of all employees reporting to a specific manager"
    )
    public ResponseEntity<List<EmployeeWorkProfileResponse>> getDirectReports(@PathVariable Long managerId) {
        return ResponseEntity.ok(service.getDirectReports(managerId));
    }
}

