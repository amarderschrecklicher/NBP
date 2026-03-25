package ba.unsa.etf.employeemanagement.dto.request;

import lombok.*;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class HolidayRequest {
    private String holidayName;
    private Date holidayDate;
    private String country;
    private String description;
}

