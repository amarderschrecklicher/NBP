package ba.unsa.etf.employeemanagement.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FinanceResponse {
    private Long id;
    private Long employeeId;
    private String bankName;
    private String bankAccountNumber;
    private String iban;
    private String taxNumber;
    private Double salary;
    private String currency;
    private String paymentFrequency;
    private Integer bonusEligible;
}
