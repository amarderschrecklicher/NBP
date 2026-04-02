package ba.unsa.etf.employeemanagement.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Finance {
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
