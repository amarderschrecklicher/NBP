package ba.unsa.etf.employeemanagement.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FamilyMemberRequest {
    private Long employeeId;
    private String firstName;
    private String lastName;
    private String relation;
    private Date dateOfBirth;
    private Integer dependent;
    private String occupation;
}