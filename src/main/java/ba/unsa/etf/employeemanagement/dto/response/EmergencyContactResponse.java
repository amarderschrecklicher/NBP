package ba.unsa.etf.employeemanagement.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EmergencyContactResponse {
    private Long id;
    private Long employeeId;
    private String firstName;
    private String lastName;
    private String relationship;
    private String phoneNumber;
    private String email;
    private String address;
}