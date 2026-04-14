package ba.unsa.etf.employeemanagement.controller.auth;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class JwtResponse {
    private String token;

    @JsonIgnore
    private String refreshToken;
}
