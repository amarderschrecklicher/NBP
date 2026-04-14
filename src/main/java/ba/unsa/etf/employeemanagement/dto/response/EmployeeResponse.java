package ba.unsa.etf.employeemanagement.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EmployeeResponse {
    private Long id;
    private Long userId;
    private String gender;
    private String nationality;
    private String maritalStatus;
    private Long managerId;

    private NbpUserResponse user;
    private EmploymentResponse employment;
}
