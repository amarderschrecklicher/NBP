package ba.unsa.etf.employeemanagement.controller;

import ba.unsa.etf.employeemanagement.dto.request.WorkPermitRequest;
import ba.unsa.etf.employeemanagement.dto.response.WorkPermitResponse;
import ba.unsa.etf.employeemanagement.service.api.IWorkPermitService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/work-permits")
@RequiredArgsConstructor
public class WorkPermitController {

    private final IWorkPermitService workPermitService;

    @GetMapping
    public List<WorkPermitResponse> findAll() {
        return workPermitService.findAll();
    }

    @GetMapping("/{id}")
    public WorkPermitResponse findById(@PathVariable Long id) {
        return workPermitService.findById(id);
    }

    @PostMapping
    public ResponseEntity<WorkPermitResponse> create(@Valid @RequestBody WorkPermitRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(workPermitService.save(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<WorkPermitResponse> update(@PathVariable Long id,
                                                     @Valid @RequestBody WorkPermitRequest request) {
        return ResponseEntity.ok(workPermitService.update(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        workPermitService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
