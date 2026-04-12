package ba.unsa.etf.employeemanagement.controller;

import ba.unsa.etf.employeemanagement.dto.request.NbpUserRequest;
import ba.unsa.etf.employeemanagement.dto.response.NbpUserResponse;
import ba.unsa.etf.employeemanagement.service.impl.NbpUserService;
import jakarta.validation.Valid;
import jakarta.validation.groups.Default;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class NbpUserController {

    private final NbpUserService service;

    @GetMapping
    public List<NbpUserResponse> findAll() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    public NbpUserResponse findById(@PathVariable Long id) {
        return service.findById(id);
    }

    @GetMapping("/username/{username}")
    public NbpUserResponse findByUsername(@PathVariable String username) {
        return service.findByUsername(username);
    }

    @PostMapping
    public ResponseEntity<NbpUserResponse> create(
            @Validated({Default.class, NbpUserRequest.OnCreate.class}) @RequestBody NbpUserRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.save(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<NbpUserResponse> update(@PathVariable Long id,
                                                    @Valid @RequestBody NbpUserRequest request) {
        return ResponseEntity.ok(service.update(id, request));
    }

    @GetMapping("/count")
    public ResponseEntity<Long> countUsers() {
        return ResponseEntity.ok(service.count());
    }
}

