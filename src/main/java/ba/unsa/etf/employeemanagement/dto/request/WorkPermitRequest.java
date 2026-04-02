package ba.unsa.etf.employeemanagement.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WorkPermitRequest {
    private Long employeeId;
    private String permitNumber;
    private String permitType;
    private String issuingCountry;
    private Date issueDate;
    private Date expiryDate;
    private String status;
}
