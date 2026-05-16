package ba.unsa.etf.employeemanagement.service.impl;

import ba.unsa.etf.employeemanagement.dto.request.VacationReportRequest;
import ba.unsa.etf.employeemanagement.dto.response.VacationReportDetailedResponse;
import ba.unsa.etf.employeemanagement.dto.response.VacationReportResponse;
import ba.unsa.etf.employeemanagement.exceptions.ResourceNotFoundException;
import ba.unsa.etf.employeemanagement.mapper.VacationReportMapper;
import ba.unsa.etf.employeemanagement.model.Vacation;
import ba.unsa.etf.employeemanagement.model.VacationReport;
import ba.unsa.etf.employeemanagement.model.nbp.NbpUser;
import ba.unsa.etf.employeemanagement.model.Employee;
import ba.unsa.etf.employeemanagement.repository.VacationReportRepository;
import ba.unsa.etf.employeemanagement.repository.VacationRepository;
import ba.unsa.etf.employeemanagement.repository.EmployeeRepository;
import ba.unsa.etf.employeemanagement.repository.nbp.NbpUserRepository;
import ba.unsa.etf.employeemanagement.service.api.IVacationReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static ba.unsa.etf.employeemanagement.util.VacationUtil.daysBetween;

@Service
@RequiredArgsConstructor
public class VacationReportService implements IVacationReportService {

    private final VacationRepository vacationRepository;
    private final VacationReportRepository reportRepository;
    private final EmployeeRepository employeeRepository;
    private final NbpUserRepository nbpUserRepository;
    private final VacationPdfGenerator pdfGenerator;
    private final VacationReportMapper reportMapper;

    @Override
    @Transactional
    public VacationReportResponse generateAndSaveReport(VacationReportRequest request) {
        Integer month = request.getReportMonth();
        Integer year = request.getReportYear();

        // Fetch all vacations for the given month
        List<Vacation> vacations = vacationRepository.findByMonthAndYear(month, year);

        // Build detail list with employee info
        List<VacationReportDetailedResponse> details = new ArrayList<>();
        for (Vacation vacation : vacations) {
            VacationReportDetailedResponse detail = new VacationReportDetailedResponse();
            detail.setVacationId(vacation.getId());
            detail.setEmployeeId(vacation.getEmployeeId());
            detail.setStartDate(vacation.getStartDate());
            detail.setEndDate(vacation.getEndDate());
            detail.setVacationType(vacation.getVacationType());
            detail.setStatus(vacation.getStatus());
            detail.setTotalDays(daysBetween(vacation.getStartDate(), vacation.getEndDate()) + 1);

            // Get employee and user info for name
            Optional<Employee> employeeOpt = employeeRepository.findById(vacation.getEmployeeId());
            if (employeeOpt.isPresent()) {
                Employee employee = employeeOpt.get();
                Optional<NbpUser> userOpt = nbpUserRepository.findById(employee.getUserId());
                if (userOpt.isPresent()) {
                    NbpUser user = userOpt.get();
                    detail.setEmployeeFirstName(user.getFirstName());
                    detail.setEmployeeLastName(user.getLastName());
                }
            }

            details.add(detail);
        }
        byte[] pdfBytes = pdfGenerator.generateMonthlyReport(month, year, details);


        Optional<VacationReport> existingReport = reportRepository.findByMonthAndYear(month, year);

        VacationReport report;
        if (existingReport.isPresent()) {
            report = existingReport.get();
            report.setPdfContent(pdfBytes);
            report.setGeneratedAt(new Date());
            report.setGeneratedBy(request.getGeneratedBy());
            reportRepository.update(report);
        } else {
            report = new VacationReport();
            report.setReportMonth(month);
            report.setReportYear(year);
            report.setPdfContent(pdfBytes);
            report.setGeneratedAt(new Date());
            report.setGeneratedBy(request.getGeneratedBy());
            Long id = reportRepository.save(report);
            report.setId(id);
        }

        return reportMapper.mapToResponse(report);
    }
    @Override
    public VacationReportResponse uploadReport(MultipartFile file, Integer month, Integer year, Long generatedBy) {
        if (file.isEmpty()) {
            throw new IllegalArgumentException("Uploaded file is empty.");
        }

        String contentType = file.getContentType();
        if (contentType == null || !contentType.equals(MediaType.APPLICATION_PDF_VALUE)) {
            throw new IllegalArgumentException("Only PDF files are accepted.");
        }

        try {
            byte[] pdfBytes = file.getBytes();

            Optional<VacationReport> existingReport = reportRepository.findByMonthAndYear(month, year);

            VacationReport report;
            if (existingReport.isPresent()) {
                report = existingReport.get();
                report.setPdfContent(pdfBytes);
                report.setGeneratedAt(new Date());
                report.setGeneratedBy(generatedBy);
                reportRepository.update(report);
            } else {
                report = new VacationReport();
                report.setReportMonth(month);
                report.setReportYear(year);
                report.setPdfContent(pdfBytes);
                report.setGeneratedAt(new Date());
                report.setGeneratedBy(generatedBy);
                Long id = reportRepository.save(report);
                report.setId(id);
            }

            return reportMapper.mapToResponse(report);
        } catch (IOException e) {
            throw new RuntimeException("Failed to read uploaded file.", e);
        }
    }


    @Override
    public byte[] downloadReport(Long id) {
        VacationReport report = reportRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Vacation report not found with id: " + id));
        return report.getPdfContent();
    }

    @Override
    public byte[] downloadReportByMonthAndYear(Integer month, Integer year) {
        VacationReport report = reportRepository.findByMonthAndYear(month, year)
                .orElseThrow(() -> new ResourceNotFoundException(
                        String.format("Vacation report not found for %02d/%d", month, year)));
        return report.getPdfContent();
    }

    @Override
    public List<VacationReportResponse> findAllReports() {
        return reportRepository.findAll().stream()
                .map(reportMapper::mapToResponse)
                .toList();
    }

    @Override
    public VacationReportResponse findById(Long id) {
        VacationReport report = reportRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Vacation report not found with id: " + id));
        return reportMapper.mapToResponse(report);
    }

    @Override
    public void deleteReport(Long id) {
        reportRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Vacation report not found with id: " + id));
        reportRepository.deleteById(id);
    }
}
