package ba.unsa.etf.employeemanagement.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmploymentRequest {

    @NotNull
    private Long employeeId;

    private String number;
    private Date hireDate;
    private Date terminationDate;
    private String jobTitle;
    private String employmentType;
    private String status;

    @NotNull
    private Long departmentId;
}
