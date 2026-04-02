package ba.unsa.etf.employeemanagement.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Disability {
    private Long id;
    private Long employeeId;
    private String disabilityType;
    private String disabilityLevel;
    private String description;
    private Date registeredDate;
}
