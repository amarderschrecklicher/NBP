package ba.unsa.etf.employeemanagement.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DisabilityRequest {
    private Long employeeId;
    private String disabilityType;
    private String disabilityLevel;
    private String description;
    private Date registeredDate;
}
