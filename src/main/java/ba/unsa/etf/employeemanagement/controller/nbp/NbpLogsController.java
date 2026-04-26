package ba.unsa.etf.employeemanagement.controller.nbp;
import ba.unsa.etf.employeemanagement.dto.logs.NbpLogsDto;
import ba.unsa.etf.employeemanagement.service.impl.nbp.NbpLogsService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/logs")
@RequiredArgsConstructor
public class NbpLogsController {

    private final NbpLogsService nbpLogService;

    @GetMapping
    public ResponseEntity<List<NbpLogsDto>> getAllLogs() {
        return ResponseEntity.ok(nbpLogService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<NbpLogsDto> getLogById(@PathVariable Long id) {
        return ResponseEntity.ok(nbpLogService.findById(id));
    }

    @GetMapping("/table/{tableName}")
    public ResponseEntity<List<NbpLogsDto>> getLogsByTableName(@PathVariable String tableName) {
        return ResponseEntity.ok(nbpLogService.findByTableName(tableName));
    }

    @GetMapping("/action/{actionName}")
    public ResponseEntity<List<NbpLogsDto>> getLogsByActionName(@PathVariable String actionName) {
        return ResponseEntity.ok(nbpLogService.findByActionName(actionName));
    }

    @GetMapping("/user/{dbUser}")
    public ResponseEntity<List<NbpLogsDto>> getLogsByDbUser(@PathVariable String dbUser) {
        return ResponseEntity.ok(nbpLogService.findByDbUser(dbUser));
    }

    @GetMapping("/date-range")
    public ResponseEntity<List<NbpLogsDto>> getLogsByDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        return ResponseEntity.ok(nbpLogService.findByDateRange(startDate, endDate));
    }

    @GetMapping("/recent")
    public ResponseEntity<List<NbpLogsDto>> getRecentLogs(
            @RequestParam(defaultValue = "100") int limit) {
        return ResponseEntity.ok(nbpLogService.findRecentLogs(limit));
    }

    @GetMapping("/count")
    public ResponseEntity<Long> countLogs() {
        return ResponseEntity.ok(nbpLogService.count());
    }

    @GetMapping("/count/table/{tableName}")
    public ResponseEntity<Long> countLogsByTableName(@PathVariable String tableName) {
        return ResponseEntity.ok(nbpLogService.countByTableName(tableName));
    }
}

