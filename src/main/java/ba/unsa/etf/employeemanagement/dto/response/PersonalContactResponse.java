package ba.unsa.etf.employeemanagement.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PersonalContactResponse {
    private Long id;
    private Long employeeId;
    private String phoneNumber;
    private String personalEmail;
}