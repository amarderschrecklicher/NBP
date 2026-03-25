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
    private String country;
    private String description;
}

