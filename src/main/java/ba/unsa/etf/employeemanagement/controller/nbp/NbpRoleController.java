package ba.unsa.etf.employeemanagement.controller.nbp;

import ba.unsa.etf.employeemanagement.dto.request.NbpRoleRequest;
import ba.unsa.etf.employeemanagement.dto.response.NbpRoleResponse;
import ba.unsa.etf.employeemanagement.service.impl.nbp.NbpRoleService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@SecurityRequirement(name = "bearerAuth")
@RestController
@RequestMapping("/api/roles")
@RequiredArgsConstructor
public class NbpRoleController {

    private final NbpRoleService service;

    @GetMapping
    public List<NbpRoleResponse> findAll() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    public NbpRoleResponse findById(@PathVariable Long id) {
        return service.findById(id);
    }

    @PostMapping
    public ResponseEntity<NbpRoleResponse> create(@Valid @RequestBody NbpRoleRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.save(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<NbpRoleResponse> update(@PathVariable Long id,
                                                    @Valid @RequestBody NbpRoleRequest request) {
        return ResponseEntity.ok(service.update(id, request));
    }

    @GetMapping("/count")
    public ResponseEntity<Long> countRoles() {
        return ResponseEntity.ok(service.count());
    }
}

