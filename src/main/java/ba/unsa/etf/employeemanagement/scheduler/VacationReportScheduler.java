package ba.unsa.etf.employeemanagement.scheduler;

import ba.unsa.etf.employeemanagement.dto.request.VacationReportRequest;
import ba.unsa.etf.employeemanagement.service.api.IVacationReportService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
@RequiredArgsConstructor
@Slf4j
public class VacationReportScheduler {

    private final IVacationReportService reportService;

    /**
     * Runs on the 1st of every month at 00:05 AM.
     * Generates the report for the previous month.
     */
    @Scheduled(cron = "0 5 0 1 * *")
    public void generateMonthlyReport() {
        LocalDate lastMonth = LocalDate.now().minusMonths(1);
        int month = lastMonth.getMonthValue();
        int year = lastMonth.getYear();

        log.info("Generating vacation report for {}/{}", month, year);

        VacationReportRequest request = new VacationReportRequest();
        request.setReportMonth(month);
        request.setReportYear(year);
        request.setGeneratedBy(null);

        reportService.generateAndSaveReport(request);

        log.info("Vacation report for {}/{} generated successfully", month, year);
    }
}
