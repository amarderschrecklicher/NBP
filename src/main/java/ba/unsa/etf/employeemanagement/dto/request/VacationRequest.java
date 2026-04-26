package ba.unsa.etf.employeemanagement.dto.request;

import lombok.*;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VacationRequest {
    private Long employeeId;
    private Date startDate;
    private Date endDate;
    private String vacationType;
    private String reason;
}

