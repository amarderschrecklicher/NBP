package ba.unsa.etf.employeemanagement.dto.request;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VacationReportRequest {
    private Integer reportMonth;
    private Integer reportYear;
    private Long generatedBy;
}

