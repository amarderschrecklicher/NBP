package ba.unsa.etf.employeemanagement.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NbpRoleRequest {

    @NotBlank(message = "Role name is required")
    @Size(min = 2, max = 100, message = "Role name must be between 2 and 100 characters")
    @Pattern(regexp = "^[A-Za-z0-9_\\- ]+$", message = "Role name may only contain letters, digits, spaces, hyphens, and underscores")
    private String name;
}
