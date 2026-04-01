package ba.unsa.etf.employeemanagement.controller;

import ba.unsa.etf.employeemanagement.dto.request.EmergencyContactRequest;
import ba.unsa.etf.employeemanagement.dto.response.EmergencyContactResponse;
import ba.unsa.etf.employeemanagement.service.api.IEmergencyContactService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/emergency-contacts")
@RequiredArgsConstructor
public class EmergencyContactController {

    private final IEmergencyContactService emergencyContactService;

    @GetMapping
    public List<EmergencyContactResponse> findAll() {
        return emergencyContactService.findAll();
    }

    @GetMapping("/{id}")
    public EmergencyContactResponse findById(@PathVariable Long id) {
        return emergencyContactService.findById(id);
    }

    @PostMapping
    public ResponseEntity<EmergencyContactResponse> create(@Valid @RequestBody EmergencyContactRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(emergencyContactService.save(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<EmergencyContactResponse> update(@PathVariable Long id,
                                                           @Valid @RequestBody EmergencyContactRequest request) {
        return ResponseEntity.ok(emergencyContactService.update(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        emergencyContactService.delete(id);
        return ResponseEntity.noContent().build();
    }
}