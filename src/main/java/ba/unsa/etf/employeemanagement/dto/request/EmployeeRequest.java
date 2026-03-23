package ba.unsa.etf.employeemanagement.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeRequest {
    @NotBlank
    private String firstName;
    @NotBlank
    private String lastName;

    @NotNull
    private Date dateOfBirth;

    private String gender;
    private String nationality;
    private String maritalStatus;

    @NotNull
    private Long userId;
}
