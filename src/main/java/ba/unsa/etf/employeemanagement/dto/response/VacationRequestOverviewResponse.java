package ba.unsa.etf.employeemanagement.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VacationRequestOverviewResponse {
    private Long vacationId;
    private Long employeeId;
    private String employeeFullName;
    private String employeeEmail;
    private String departmentName;
    private String jobTitle;
    private Date startDate;
    private Date endDate;
    private Long totalDays;
    private Integer vacationYear;
    private Integer vacationMonth;
    private String vacationType;
    private String status;
    private Long approvedBy;
    private String approvedByFullName;
    private String workflowBucket;
    private Long requestsThisYear;
    private Long requestedDaysThisYear;
}

