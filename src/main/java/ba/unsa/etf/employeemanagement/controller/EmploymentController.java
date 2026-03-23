package ba.unsa.etf.employeemanagement.controller;

import ba.unsa.etf.employeemanagement.dto.request.EmploymentRequest;
import ba.unsa.etf.employeemanagement.dto.response.EmploymentResponse;
import ba.unsa.etf.employeemanagement.service.api.IEmploymentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/employments")
@RequiredArgsConstructor
public class EmploymentController {

    private final IEmploymentService employmentService;

    @GetMapping
    public List<EmploymentResponse> findAll() {
        return employmentService.findAll();
    }

    @GetMapping("/{id}")
    public EmploymentResponse findById(@PathVariable Long id) {
        return employmentService.findById(id);
    }

    @PostMapping
    public ResponseEntity<EmploymentResponse> create(@Valid @RequestBody EmploymentRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(employmentService.save(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<EmploymentResponse> update(@PathVariable Long id,
                                                   @Valid @RequestBody EmploymentRequest request) {
        return ResponseEntity.ok(employmentService.update(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        employmentService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
