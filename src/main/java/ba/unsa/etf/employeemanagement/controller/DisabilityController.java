package ba.unsa.etf.employeemanagement.controller;

import ba.unsa.etf.employeemanagement.dto.request.DisabilityRequest;
import ba.unsa.etf.employeemanagement.dto.response.DisabilityResponse;
import ba.unsa.etf.employeemanagement.service.api.IDisabilityService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/disabilities")
@RequiredArgsConstructor
public class DisabilityController {

    private final IDisabilityService disabilityService;

    @GetMapping
    public List<DisabilityResponse> findAll() {
        return disabilityService.findAll();
    }

    @GetMapping("/{id}")
    public DisabilityResponse findById(@PathVariable Long id) {
        return disabilityService.findById(id);
    }

    @PostMapping
    public ResponseEntity<DisabilityResponse> create(@Valid @RequestBody DisabilityRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(disabilityService.save(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<DisabilityResponse> update(@PathVariable Long id,
                                                     @Valid @RequestBody DisabilityRequest request) {
        return ResponseEntity.ok(disabilityService.update(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        disabilityService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
