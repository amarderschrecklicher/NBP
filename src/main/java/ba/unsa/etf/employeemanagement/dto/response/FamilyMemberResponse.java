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
public class FamilyMemberResponse {
    private Long id;
    private Long employeeId;
    private String firstName;
    private String lastName;
    private String relation;
    private Date dateOfBirth;
    private Integer dependent;
    private String occupation;
}