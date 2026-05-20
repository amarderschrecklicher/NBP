package ba.unsa.etf.employeemanagement.controller.nbp;

import ba.unsa.etf.employeemanagement.dto.request.NbpUserRequest;
import ba.unsa.etf.employeemanagement.dto.response.NbpUserResponse;
import ba.unsa.etf.employeemanagement.service.impl.nbp.NbpUserService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import jakarta.validation.groups.Default;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

import ba.unsa.etf.employeemanagement.dto.response.LoginEventDto;
import ba.unsa.etf.employeemanagement.dto.response.UserWithLoginEventsResponse;
import ba.unsa.etf.employeemanagement.service.impl.mongo.LoginEventService;

@SecurityRequirement(name = "bearerAuth")
@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class NbpUserController {

    private final NbpUserService service;
    private final LoginEventService loginEventService;

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

    @GetMapping("/{id}/with-logins")
    public ResponseEntity<UserWithLoginEventsResponse> getUserWithLoginEvents(@PathVariable Long id) {
        NbpUserResponse user = service.findById(id);
        var events = loginEventService.findByUserId(id);

        List<LoginEventDto> dtoEvents = events.stream().map(e -> LoginEventDto.builder()
                .id(e.getId())
                .userId(e.getUserId())
                .timestamp(e.getTimestamp())
                .build()).collect(Collectors.toList());

        UserWithLoginEventsResponse resp = UserWithLoginEventsResponse.builder()
                .user(user)
                .loginEvents(dtoEvents)
                .build();

        return ResponseEntity.ok(resp);
    }
}

