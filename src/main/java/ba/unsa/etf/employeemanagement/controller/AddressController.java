package ba.unsa.etf.employeemanagement.controller;
import ba.unsa.etf.employeemanagement.dto.address.AddressDto;
import ba.unsa.etf.employeemanagement.service.impl.AddressService;
import ba.unsa.etf.employeemanagement.util.validation.AddressValidator;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/addresses")
@RequiredArgsConstructor
public class AddressController {

    private final AddressService service;
    private final AddressValidator addressValidator;

    @GetMapping
    public List<AddressDto> findAll() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    public AddressDto findById(@PathVariable Long id) {
        return service.findById(id);
    }

    @PostMapping
    public ResponseEntity<?> create(@Valid @RequestBody AddressDto addressDto,
                                    BindingResult bindingResult) {
        addressValidator.validate(addressDto, bindingResult);

        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body(getErrors(bindingResult));
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(addressDto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Long id,
                                    @Valid @RequestBody AddressDto addressDto,
                                    BindingResult bindingResult) {
        addressValidator.validate(addressDto, bindingResult);

        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body(getErrors(bindingResult));
        }

        return ResponseEntity.ok(service.update(id, addressDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    private Map<String, String> getErrors(BindingResult bindingResult) {
        Map<String, String> errors = new HashMap<>();
        bindingResult.getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return errors;
    }
}

