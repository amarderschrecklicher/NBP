package ba.unsa.etf.employeemanagement.dto.request;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PersonalContactRequest {
    @NotNull(message = "Employee ID is required")
    @Positive(message = "Employee ID must be a positive number")
    private Long employeeId;

    @NotBlank(message = "Phone number is required")
    @Pattern(regexp = "^\\+?[0-9\\s-]{7,20}$", message = "Phone number must be a valid format (7-20 digits, optional + prefix)")
    private String phoneNumber;

    @NotBlank(message = "Personal email is required")
    @Email(message = "Personal email must be a valid email address")
    @Size(max = 100, message = "Personal email must not exceed 100 characters")
    private String personalEmail;
}