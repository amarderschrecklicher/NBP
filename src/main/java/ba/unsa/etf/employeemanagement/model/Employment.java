package ba.unsa.etf.employeemanagement.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Employment {
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
