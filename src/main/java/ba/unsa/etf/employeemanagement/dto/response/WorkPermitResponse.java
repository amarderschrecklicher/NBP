package ba.unsa.etf.employeemanagement.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WorkPermitResponse {
    private Long id;
    private Long employeeId;
    private String permitNumber;
    private String permitType;
    private String issuingCountry;
    private Date issueDate;
    private Date expiryDate;
    private String status;
}
