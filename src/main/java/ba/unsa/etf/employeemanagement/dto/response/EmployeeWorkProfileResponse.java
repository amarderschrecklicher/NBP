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
public class EmployeeWorkProfileResponse {
    private Long employeeId;
    private Long userId;
    private String username;
    private String employeeFullName;
    private String email;
    private String phoneNumber;
    private Date birthDate;
    private String gender;
    private String nationality;
    private String maritalStatus;
    private Long managerId;
    private String managerFullName;
    private Long employmentId;
    private String employmentNumber;
    private Date hireDate;
    private Date terminationDate;
    private Long daysSinceHire;
    private String jobTitle;
    private String employmentType;
    private String employmentStatus;
    private Long departmentId;
    private String departmentName;
    private String lifecycleState;
}

