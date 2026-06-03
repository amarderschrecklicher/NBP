package ba.unsa.etf.employeemanagement.controller.nbp;

import ba.unsa.etf.employeemanagement.exceptions.BadRequestException;
import ba.unsa.etf.employeemanagement.service.api.IXmlExportService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@SecurityRequirement(name = "bearerAuth")
@RestController
@RequestMapping("/api/export")
@RequiredArgsConstructor
@Slf4j
public class ExportController {

    private final IXmlExportService exportService;

    @PreAuthorize("hasRole('EMS_ADMINISTRATOR')")
    @GetMapping("/xml")
    public ResponseEntity<byte[]> exportXml(@RequestParam String table) {
        try {
            byte[] xml = exportService.exportTableAsXml(table);
            String filename = "export-"
                    + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd-HHmmss"))
                    + ".xml";
            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_XML)
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filename + "\"")
                    .header(HttpHeaders.CACHE_CONTROL, "no-store")
                    .body(xml);
        } catch (BadRequestException e) {
            log.warn("Invalid export request for table '{}': {}", table, e.getMessage());
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            log.error("XML export failed for table: {}", table, e);
            return ResponseEntity.internalServerError().build();
        }
    }
}
