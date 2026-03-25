package ba.unsa.etf.employeemanagement.dto.response;

import lombok.*;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HolidayResponse {
    private Long id;
    private String holidayName;
    private Date holidayDate;
    private String country;
    private String description;
}

