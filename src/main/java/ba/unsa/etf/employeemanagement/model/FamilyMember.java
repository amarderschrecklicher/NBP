package ba.unsa.etf.employeemanagement.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FamilyMember {
    private Long id;
    private Long employeeId;
    private String firstName;
    private String lastName;
    private String relation;
    private Date dateOfBirth;
    private Integer dependent;
    private String occupation;
}