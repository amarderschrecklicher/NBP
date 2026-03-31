package ba.unsa.etf.employeemanagement.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PersonalContactRequest {
    private Long employeeId;
    private String phoneNumber;
    private String personalEmail;
}