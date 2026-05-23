package ba.unsa.etf.employeemanagement.controller.plsql;

import ba.unsa.etf.employeemanagement.dto.plsql.AddEmployeePlsqlRequest;
import ba.unsa.etf.employeemanagement.dto.response.EmployeeResponse;
import ba.unsa.etf.employeemanagement.service.impl.plsql.PlsqlEmployeeService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@SecurityRequirement(name = "bearerAuth")
@RestController
@RequestMapping("/api/plsql/employees")
@RequiredArgsConstructor
public class PlsqlEmployeeController {

    private final PlsqlEmployeeService plsqlEmployeeService;

    @PostMapping
    @PreAuthorize("hasRole('EMS_ADMINISTRATOR')")
    public ResponseEntity<EmployeeResponse> create(@Valid @RequestBody AddEmployeePlsqlRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(plsqlEmployeeService.addEmployee(request));
    }

    @PatchMapping("/{id}/employment")
    @PreAuthorize("hasRole('EMS_ADMINISTRATOR')")
    public EmployeeResponse updateEmployment(
            @PathVariable Long id,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) Long departmentId
    ) {
        return plsqlEmployeeService.updateEmployment(id, status, departmentId);
    }

    @PostMapping("/{id}/archive")
    @PreAuthorize("hasRole('EMS_ADMINISTRATOR')")
    public ResponseEntity<?> archive(
            @PathVariable Long id,
            @RequestParam(defaultValue = "false") boolean hardDelete
    ) {
        EmployeeResponse response = plsqlEmployeeService.archiveEmployee(id, hardDelete);
        if (response == null) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(response);
    }
}
