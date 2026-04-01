package ba.unsa.etf.employeemanagement.controller;

import ba.unsa.etf.employeemanagement.dto.address.AddressDto;
import ba.unsa.etf.employeemanagement.service.impl.AddressService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/addresses")
@RequiredArgsConstructor
public class AddressController {

    private final AddressService service;

    @GetMapping
    public List<AddressDto> findAll() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    public AddressDto findById(@PathVariable Long id) {
        return service.findById(id);
    }

    @PostMapping
    public ResponseEntity<AddressDto> create(@Valid @RequestBody AddressDto addressDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(addressDto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<AddressDto> update(@PathVariable Long id,
                                             @Valid @RequestBody AddressDto addressDto) {
        return ResponseEntity.ok(service.update(id, addressDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
