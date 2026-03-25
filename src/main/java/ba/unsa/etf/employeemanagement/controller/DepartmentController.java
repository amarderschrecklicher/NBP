package ba.unsa.etf.employeemanagement.controller;

import ba.unsa.etf.employeemanagement.dto.request.DepartmentRequest;
import ba.unsa.etf.employeemanagement.dto.response.DepartmentResponse;
import ba.unsa.etf.employeemanagement.service.api.IDepartmentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/departments")
@RequiredArgsConstructor
public class DepartmentController {
    private final IDepartmentService service;

    @GetMapping
    public List<DepartmentResponse> findAll() { return service.findAll(); }

    @GetMapping("/{id}")
    public DepartmentResponse findById(@PathVariable Long id) { return service.findById(id); }

    @PostMapping
    public ResponseEntity<DepartmentResponse> create(@Valid @RequestBody DepartmentRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.save(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<DepartmentResponse> update(@PathVariable Long id, @Valid @RequestBody DepartmentRequest request) {
        return ResponseEntity.ok(service.update(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}

