package ba.unsa.etf.employeemanagement.controller;

import ba.unsa.etf.employeemanagement.dto.request.FinanceRequest;
import ba.unsa.etf.employeemanagement.dto.response.FinanceResponse;
import ba.unsa.etf.employeemanagement.service.api.IFinanceService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@SecurityRequirement(name = "bearerAuth")
@RestController
@RequestMapping("/api/finances")
@RequiredArgsConstructor
public class FinanceController {

    private final IFinanceService financeService;

    @GetMapping
    public List<FinanceResponse> findAll() {
        return financeService.findAll();
    }

    @GetMapping("/{id}")
    public FinanceResponse findById(@PathVariable Long id) {
        return financeService.findById(id);
    }

    @PostMapping
    public ResponseEntity<FinanceResponse> create(@Valid @RequestBody FinanceRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(financeService.save(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<FinanceResponse> update(@PathVariable Long id,
                                                  @Valid @RequestBody FinanceRequest request) {
        return ResponseEntity.ok(financeService.update(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        financeService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
