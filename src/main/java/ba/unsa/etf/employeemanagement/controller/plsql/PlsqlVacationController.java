package ba.unsa.etf.employeemanagement.controller.plsql;

import ba.unsa.etf.employeemanagement.dto.plsql.RemainingVacationDays;
import ba.unsa.etf.employeemanagement.dto.request.VacationRequest;
import ba.unsa.etf.employeemanagement.dto.response.VacationResponse;
import ba.unsa.etf.employeemanagement.service.impl.plsql.PlsqlVacationService;
import ba.unsa.etf.employeemanagement.util.validation.VacationValidator;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@SecurityRequirement(name = "bearerAuth")
@RestController
@RequestMapping("/api/plsql/vacations")
@RequiredArgsConstructor
public class PlsqlVacationController {

    private final PlsqlVacationService plsqlVacationService;
    private final VacationValidator vacationValidator;

    @PostMapping("/request")
    public ResponseEntity<?> submitRequest(@Valid @RequestBody VacationRequest request,
                                           BindingResult bindingResult) {
        vacationValidator.validate(request, bindingResult);
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body(getErrors(bindingResult));
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(plsqlVacationService.submitRequest(request));
    }

    @PreAuthorize("hasAnyRole('EMS_MANAGER', 'EMS_ADMINISTRATOR')")
    @PostMapping("/{id}/decision")
    public VacationResponse decide(
            @PathVariable Long id,
            @RequestParam Long approverId,
            @RequestParam boolean approve,
            @RequestParam(required = false) String reason
    ) {
        return plsqlVacationService.decide(id, approverId, approve, reason);
    }

    @GetMapping("/remaining")
    public RemainingVacationDays remainingDays(
            @RequestParam Long employeeId,
            @RequestParam(required = false) Integer year
    ) {
        return plsqlVacationService.remainingDays(employeeId, year);
    }

    private Map<String, String> getErrors(BindingResult bindingResult) {
        Map<String, String> errors = new HashMap<>();
        bindingResult.getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            errors.put(fieldName, error.getDefaultMessage());
        });
        return errors;
    }
}
