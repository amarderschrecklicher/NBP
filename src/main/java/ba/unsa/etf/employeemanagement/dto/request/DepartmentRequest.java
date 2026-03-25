package ba.unsa.etf.employeemanagement.dto.request;

import lombok.*;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DepartmentRequest {
    private String name;
    private String description;
}

