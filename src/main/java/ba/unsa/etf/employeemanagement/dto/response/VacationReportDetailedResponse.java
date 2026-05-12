package ba.unsa.etf.employeemanagement.dto.response;

import lombok.*;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VacationReportDetailedResponse {
    private Long vacationId;
    private Long employeeId;
    private String employeeFirstName;
    private String employeeLastName;
    private Date startDate;
    private Date endDate;
    private String vacationType;
    private String status;
    private int totalDays;
}
