package ba.unsa.etf.employeemanagement.controller;

import ba.unsa.etf.employeemanagement.dto.request.PersonalContactRequest;
import ba.unsa.etf.employeemanagement.dto.response.PersonalContactResponse;
import ba.unsa.etf.employeemanagement.service.api.IPersonalContactService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/personal-contacts")
@RequiredArgsConstructor
public class PersonalContactController {

    private final IPersonalContactService personalContactService;

    @GetMapping
    public List<PersonalContactResponse> findAll() {
        return personalContactService.findAll();
    }

    @GetMapping("/{id}")
    public PersonalContactResponse findById(@PathVariable Long id) {
        return personalContactService.findById(id);
    }

    @PostMapping
    public ResponseEntity<PersonalContactResponse> create(@Valid @RequestBody PersonalContactRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(personalContactService.save(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<PersonalContactResponse> update(@PathVariable Long id,
                                                          @Valid @RequestBody PersonalContactRequest request) {
        return ResponseEntity.ok(personalContactService.update(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        personalContactService.delete(id);
        return ResponseEntity.noContent().build();
    }
}