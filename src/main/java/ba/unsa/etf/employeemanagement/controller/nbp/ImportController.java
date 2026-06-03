package ba.unsa.etf.employeemanagement.controller.nbp;

import ba.unsa.etf.employeemanagement.dto.response.XmlImportResponse;
import ba.unsa.etf.employeemanagement.exceptions.BadRequestException;
import ba.unsa.etf.employeemanagement.service.api.IXmlImportService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@SecurityRequirement(name = "bearerAuth")
@RestController
@RequestMapping("/api/import")
@RequiredArgsConstructor
@Slf4j
public class ImportController {

    private final IXmlImportService importService;

    @PreAuthorize("hasRole('EMS_ADMINISTRATOR')")
    @PostMapping("/xml")
    public ResponseEntity<?> importXml(
            @RequestParam(value = "file", required = false) MultipartFile file,
            @RequestBody(required = false) String xmlContent) {

        try {
            byte[] xmlBytes;

            // Handle multipart file upload
            if (file != null && !file.isEmpty()) {
                log.debug("Processing multipart XML file: {}", file.getOriginalFilename());
                xmlBytes = file.getBytes();
            }
            // Handle plain text XML body
            else if (xmlContent != null && !xmlContent.isBlank()) {
                log.debug("Processing XML from request body");
                xmlBytes = xmlContent.getBytes(StandardCharsets.UTF_8);
            }
            // Both are missing/empty
            else {
                log.warn("XML import request with no file and no content");
                return ResponseEntity.badRequest().body(
                        XmlImportResponse.builder()
                                .status("ERROR")
                                .message("Either 'file' parameter (multipart) or XML content in body must be provided")
                                .timestamp(System.currentTimeMillis())
                                .build()
                );
            }

            // Call service to import
            String importResult = importService.importXmlContent(xmlBytes);

            return ResponseEntity.ok(
                    XmlImportResponse.builder()
                            .status("SUCCESS")
                            .message(importResult)
                            .timestamp(System.currentTimeMillis())
                            .build()
            );

        } catch (IllegalArgumentException e) {
            log.warn("Validation error during XML import: {}", e.getMessage());
            return ResponseEntity.badRequest().body(
                    XmlImportResponse.builder()
                            .status("ERROR")
                            .message("Invalid XML: " + e.getMessage())
                            .timestamp(System.currentTimeMillis())
                            .build()
            );
        } catch (BadRequestException e) {
            log.warn("Bad request during XML import: {}", e.getMessage());
            return ResponseEntity.badRequest().body(
                    XmlImportResponse.builder()
                            .status("ERROR")
                            .message(e.getMessage())
                            .timestamp(System.currentTimeMillis())
                            .build()
            );
        } catch (IOException e) {
            log.error("IOException while reading file", e);
            return ResponseEntity.badRequest().body(
                    XmlImportResponse.builder()
                            .status("ERROR")
                            .message("Failed to read file: " + e.getMessage())
                            .timestamp(System.currentTimeMillis())
                            .build()
            );
        } catch (Exception e) {
            log.error("Unexpected error during XML import", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    XmlImportResponse.builder()
                            .status("ERROR")
                            .message("Internal server error: " + e.getMessage())
                            .timestamp(System.currentTimeMillis())
                            .build()
            );
        }
    }
}
