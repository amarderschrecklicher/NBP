package ba.unsa.etf.employeemanagement.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WorkPermit {
    private Long id;
    private Long employeeId;
    private String permitNumber;
    private String permitType;
    private String issuingCountry;
    private Date issueDate;
    private Date expiryDate;
    private String status;
}
