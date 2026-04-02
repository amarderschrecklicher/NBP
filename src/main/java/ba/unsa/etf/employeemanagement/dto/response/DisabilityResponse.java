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
public class DisabilityResponse {
    private Long id;
    private Long employeeId;
    private String disabilityType;
    private String disabilityLevel;
    private String description;
    private Date registeredDate;
}
