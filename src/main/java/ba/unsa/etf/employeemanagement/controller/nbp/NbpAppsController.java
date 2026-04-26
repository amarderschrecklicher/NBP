package ba.unsa.etf.employeemanagement.controller.nbp;
import ba.unsa.etf.employeemanagement.dto.apps.NbpAppsDto;
import ba.unsa.etf.employeemanagement.service.impl.nbp.NbpAppsService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@SecurityRequirement(name = "bearerAuth")
@RestController
@RequestMapping("/api/apps")
@RequiredArgsConstructor
public class NbpAppsController {

    private final NbpAppsService nbpAppsService;

    @GetMapping
    public ResponseEntity<List<NbpAppsDto>> getAllApps() {
        return ResponseEntity.ok(nbpAppsService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<NbpAppsDto> getAppById(@PathVariable Long id) {
        return ResponseEntity.ok(nbpAppsService.findById(id));
    }

    @GetMapping("/manager/{managerId}")
    public ResponseEntity<List<NbpAppsDto>> getAppsByManagerId(@PathVariable Long managerId) {
        return ResponseEntity.ok(nbpAppsService.findByManagerId(managerId));
    }

    @GetMapping("/expired")
    public ResponseEntity<List<NbpAppsDto>> getExpiredApps() {
        return ResponseEntity.ok(nbpAppsService.findExpiredApps());
    }

    @PutMapping("/{id}")
    public ResponseEntity<NbpAppsDto> updateApp(@PathVariable Long id,
                                                @RequestBody NbpAppsDto nbpAppsDto) {
        NbpAppsDto updated = nbpAppsService.update(id, nbpAppsDto);
        return ResponseEntity.ok(updated);
    }

    @GetMapping("/count")
    public ResponseEntity<Long> countApps() {
        return ResponseEntity.ok(nbpAppsService.count());
    }
}

