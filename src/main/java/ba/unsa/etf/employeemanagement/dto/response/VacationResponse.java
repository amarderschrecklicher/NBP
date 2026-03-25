package ba.unsa.etf.employeemanagement.dto.response;

import lombok.*;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VacationResponse {
    private Long id;
    private Long employeeId;
    private Date startDate;
    private Date endDate;
    private String vacationType;
    private String status;
    private Long approvedBy;
    private String reason;
}

