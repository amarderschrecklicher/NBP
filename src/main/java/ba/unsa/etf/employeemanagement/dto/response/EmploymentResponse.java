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
public class EmploymentResponse {
    private Long id;
    private Long employeeId;
    private String number;
    private Date hireDate;
    private Date terminationDate;
    private String jobTitle;
    private String employmentType;
    private String status;
    private Long departmentId;
}
