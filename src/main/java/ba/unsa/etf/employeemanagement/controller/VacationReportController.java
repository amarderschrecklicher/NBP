package ba.unsa.etf.employeemanagement.controller;

import ba.unsa.etf.employeemanagement.dto.request.VacationReportRequest;
import ba.unsa.etf.employeemanagement.dto.response.VacationReportResponse;
import ba.unsa.etf.employeemanagement.service.api.IVacationReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/vacation-reports")
@RequiredArgsConstructor
public class VacationReportController {

    private final IVacationReportService reportService;

    /**
     * Generate (or regenerate) a monthly vacation report and save as PDF BLOB.
     */
    @PostMapping("/generate")
    public ResponseEntity<VacationReportResponse> generateReport(@RequestBody VacationReportRequest request) {
        VacationReportResponse response = reportService.generateAndSaveReport(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * List all generated reports (metadata only, no BLOB).
     */
    @GetMapping
    public ResponseEntity<List<VacationReportResponse>> getAllReports() {
        return ResponseEntity.ok(reportService.findAllReports());
    }

    /**
     * Get report metadata by ID.
     */
    @GetMapping("/{id}")
    public ResponseEntity<VacationReportResponse> getReportById(@PathVariable Long id) {
        return ResponseEntity.ok(reportService.findById(id));
    }

    /**
     * Upload a vacation report PDF manually.
     */
    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<VacationReportResponse> uploadReport(
            @RequestParam("file") MultipartFile file,
            @RequestParam("month") Integer month,
            @RequestParam("year") Integer year,
            @RequestParam("generatedBy") Long generatedBy) {

        VacationReportResponse response = reportService.uploadReport(file, month, year, generatedBy);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }



    /**
     * Download the PDF by report ID.
     */
    @GetMapping("/{id}/download")
    public ResponseEntity<byte[]> downloadReport(@PathVariable Long id) {
        byte[] pdfBytes = reportService.downloadReport(id);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDispositionFormData("attachment", "vacation_report_" + id + ".pdf");
        headers.setContentLength(pdfBytes.length);

        return new ResponseEntity<>(pdfBytes, headers, HttpStatus.OK);
    }

    /**
     * Download the PDF by month and year.
     */
    @GetMapping("/download")
    public ResponseEntity<byte[]> downloadReportByMonthAndYear(
            @RequestParam Integer month,
            @RequestParam Integer year) {

        byte[] pdfBytes = reportService.downloadReportByMonthAndYear(month, year);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDispositionFormData("attachment",
                String.format("vacation_report_%02d_%d.pdf", month, year));
        headers.setContentLength(pdfBytes.length);

        return new ResponseEntity<>(pdfBytes, headers, HttpStatus.OK);
    }

    /**
     * Delete a report by ID.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReport(@PathVariable Long id) {
        reportService.deleteReport(id);
        return ResponseEntity.noContent().build();
    }
}
