package ba.unsa.etf.employeemanagement.mapper;

import ba.unsa.etf.employeemanagement.dto.response.VacationReportResponse;
import ba.unsa.etf.employeemanagement.model.VacationReport;
import org.springframework.stereotype.Component;

@Component
public class VacationReportMapper {

    public VacationReportResponse mapToResponse(VacationReport report) {
        VacationReportResponse response = new VacationReportResponse();
        response.setId(report.getId());
        response.setReportMonth(report.getReportMonth());
        response.setReportYear(report.getReportYear());
        response.setGeneratedAt(report.getGeneratedAt());
        response.setGeneratedBy(report.getGeneratedBy());
        response.setDownloadUrl("/api/vacation-reports/" + report.getId() + "/download");
        return response;
    }
}
