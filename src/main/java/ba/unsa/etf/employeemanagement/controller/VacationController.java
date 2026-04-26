package ba.unsa.etf.employeemanagement.controller;

import ba.unsa.etf.employeemanagement.dto.request.VacationRequest;
import ba.unsa.etf.employeemanagement.dto.response.VacationResponse;
import ba.unsa.etf.employeemanagement.service.api.IVacationService;
import ba.unsa.etf.employeemanagement.util.validation.VacationValidator;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/vacations")
@RequiredArgsConstructor
public class VacationController {
    private final IVacationService service;
    private final VacationValidator vacationValidator;

    @GetMapping
    public List<VacationResponse> findAll() { return service.findAll(); }

    @GetMapping("/{id}")
    public VacationResponse findById(@PathVariable Long id) { return service.findById(id); }

    @PostMapping
    public ResponseEntity<?> create(@Valid @RequestBody VacationRequest request,
                                    BindingResult bindingResult) {
        vacationValidator.validate(request, bindingResult);

        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body(getErrors(bindingResult));
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(service.save(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Long id,
                                    @Valid @RequestBody VacationRequest request,
                                    BindingResult bindingResult) {
        vacationValidator.validate(request, bindingResult);

        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body(getErrors(bindingResult));
        }

        return ResponseEntity.ok(service.update(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/request")
    public ResponseEntity<?> requestVacation(@Valid @RequestBody VacationRequest request, BindingResult bindingResult) {
        vacationValidator.validate(request, bindingResult);
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body(getErrors(bindingResult));
        }
        try {
            return ResponseEntity.status(HttpStatus.CREATED).body(service.requestVacation(request));
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().body(Map.of("error", ex.getMessage()));
        }
    }

    @PreAuthorize("hasAnyRole('EMS_MANAGER', 'EMS_ADMINISTRATOR')")
    @PostMapping("/{id}/approve")
    public ResponseEntity<?> approveVacation(@PathVariable Long id, @RequestParam Long approverId) {
        return ResponseEntity.ok(service.approveVacation(id, approverId));
    }

    @PreAuthorize("hasAnyRole('EMS_MANAGER', 'EMS_ADMINISTRATOR')")
    @PostMapping("/{id}/reject")
    public ResponseEntity<?> rejectVacation(@PathVariable Long id, @RequestParam Long approverId, @RequestParam String reason) {
        return ResponseEntity.ok(service.rejectVacation(id, approverId, reason));
    }

    @GetMapping("/{id}/status")
    public ResponseEntity<?> getVacationStatus(@PathVariable Long id) {
        return ResponseEntity.ok(Map.of("status", service.getVacationStatus(id)));
    }

    private Map<String, String> getErrors(BindingResult bindingResult) {
        Map<String, String> errors = new HashMap<>();
        bindingResult.getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return errors;
    }
}
