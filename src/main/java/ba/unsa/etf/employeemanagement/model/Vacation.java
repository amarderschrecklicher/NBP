package ba.unsa.etf.employeemanagement.model;

import lombok.*;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Vacation {
    private Long id;
    private Long employeeId;
    private Date startDate;
    private Date endDate;
    private String vacationType;
    private String status;
    private Long approvedBy;
    private String reason;
}

