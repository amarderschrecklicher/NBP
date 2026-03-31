package ba.unsa.etf.employeemanagement.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmergencyContactRequest {
    private Long employeeId;
    private String firstName;
    private String lastName;
    private String relationship;
    private String phoneNumber;
    private String email;
    private String address;
}