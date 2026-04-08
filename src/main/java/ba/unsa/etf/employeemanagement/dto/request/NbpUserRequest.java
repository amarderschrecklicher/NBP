package ba.unsa.etf.employeemanagement.dto.request;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NbpUserRequest {

    public interface OnCreate {}

    @NotBlank(message = "First name is required")
    @Size(min = 2, max = 255, message = "First name must be between 2 and 255 characters")
    @Pattern(regexp = "^[A-Za-zÀ-ÖØ-öø-ÿčćžšđČĆŽŠĐ' -]+$", message = "First name can only contain letters, spaces, hyphens, and apostrophes")
    private String firstName;

    @NotBlank(message = "Last name is required")
    @Size(min = 2, max = 255, message = "Last name must be between 2 and 255 characters")
    @Pattern(regexp = "^[A-Za-zÀ-ÖØ-öø-ÿčćžšđČĆŽŠĐ' -]+$", message = "Last name can only contain letters, spaces, hyphens, and apostrophes")
    private String lastName;

    @NotBlank(message = "Email is required")
    @Email(message = "Email must be a valid email address")
    @Size(max = 255, message = "Email must not exceed 255 characters")
    private String email;

    /** Required on create; on update omit, leave null, or provide a blank/whitespace-only value to keep the existing password (see NbpUserService). */
    @NotBlank(groups = OnCreate.class, message = "Password is required")
    @Size(min = 8, max = 128, message = "Password must be between 8 and 128 characters when provided")
    private String password;

    @NotBlank(message = "Username is required")
    @Size(min = 3, max = 50, message = "Username must be between 3 and 50 characters")
    @Pattern(regexp = "^[A-Za-z0-9._-]+$", message = "Username can only contain letters, digits, dots, underscores, and hyphens")
    private String username;

    @Pattern(regexp = "^\\+?[0-9\\s-]{7,20}$", message = "Phone number must be a valid format (7-20 digits, optional + prefix)")
    private String phoneNumber;

    @Past(message = "Birth date must be in the past")
    private Date birthDate;

    @Positive(message = "Address ID must be a positive number")
    private Long addressId;

    @Positive(message = "Role ID must be a positive number")
    private Long roleId;
}
