package ba.unsa.etf.employeemanagement.model;

import lombok.*;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Holiday {
    private Long id;
    private String holidayName;
    private Date holidayDate;
    private String country; // Used as nationality
    private String description;
    private String religion; // For user-specific holidays
}
