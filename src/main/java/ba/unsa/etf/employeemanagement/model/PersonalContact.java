package ba.unsa.etf.employeemanagement.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PersonalContact {
    private Long id;
    private Long employeeId;
    private String phoneNumber;
    private String personalEmail;
}