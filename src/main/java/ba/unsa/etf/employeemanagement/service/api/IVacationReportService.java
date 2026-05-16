package ba.unsa.etf.employeemanagement.service.api;

import ba.unsa.etf.employeemanagement.dto.request.VacationReportRequest;
import ba.unsa.etf.employeemanagement.dto.response.VacationReportResponse;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface IVacationReportService {
    VacationReportResponse generateAndSaveReport(VacationReportRequest request);
    byte[] downloadReport(Long id);
    byte[] downloadReportByMonthAndYear(Integer month, Integer year);
    List<VacationReportResponse> findAllReports();
    VacationReportResponse findById(Long id);
    void deleteReport(Long id);
    VacationReportResponse uploadReport(MultipartFile file, Integer month, Integer year, Long generatedBy);

}
