package ba.unsa.etf.employeemanagement.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PayrollCostOverviewResponse {
    private Long financeId;
    private Long employeeId;
    private String username;
    private String employeeFullName;
    private String jobTitle;
    private String departmentName;
    private String bankName;
    private String bankAccountNumber;
    private String iban;
    private String taxNumber;
    private Double salary;
    private String currency;
    private String paymentFrequency;
    private Double annualSalaryEquivalent;
    private String bonusEligibleFlag;
    private String compensationBand;
    private String employmentStatus;
}

