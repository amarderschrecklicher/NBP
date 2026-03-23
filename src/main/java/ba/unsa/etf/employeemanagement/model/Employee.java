package ba.unsa.etf.employeemanagement.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Employee {
    private Long id;
    private Long userId;
    private String firstName;
    private String lastName;
    private Date dateOfBirth;
    private String gender;
    private String nationality;
    private String maritalStatus;
}
