package ba.unsa.etf.employeemanagement.dto.response;

import lombok.*;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VacationReportResponse {
    private Long id;
    private Integer reportMonth;
    private Integer reportYear;
    private Date generatedAt;
    private Long generatedBy;
    private String downloadUrl;
}
