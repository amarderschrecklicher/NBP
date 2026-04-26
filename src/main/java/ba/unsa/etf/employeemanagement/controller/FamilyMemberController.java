package ba.unsa.etf.employeemanagement.controller;

import ba.unsa.etf.employeemanagement.dto.request.FamilyMemberRequest;
import ba.unsa.etf.employeemanagement.dto.response.FamilyMemberResponse;
import ba.unsa.etf.employeemanagement.service.api.IFamilyMemberService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@SecurityRequirement(name = "bearerAuth")
@RestController
@RequestMapping("/api/family-members")
@RequiredArgsConstructor
public class FamilyMemberController {

    private final IFamilyMemberService familyMemberService;

    @GetMapping
    public List<FamilyMemberResponse> findAll() {
        return familyMemberService.findAll();
    }

    @GetMapping("/{id}")
    public FamilyMemberResponse findById(@PathVariable Long id) {
        return familyMemberService.findById(id);
    }

    @PostMapping
    public ResponseEntity<FamilyMemberResponse> create(@Valid @RequestBody FamilyMemberRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(familyMemberService.save(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<FamilyMemberResponse> update(@PathVariable Long id,
                                                       @Valid @RequestBody FamilyMemberRequest request) {
        return ResponseEntity.ok(familyMemberService.update(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        familyMemberService.delete(id);
        return ResponseEntity.noContent().build();
    }
}