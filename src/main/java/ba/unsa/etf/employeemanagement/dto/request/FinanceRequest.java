package ba.unsa.etf.employeemanagement.dto.request;

import ba.unsa.etf.employeemanagement.util.enums.Currency;
import ba.unsa.etf.employeemanagement.util.enums.PaymentFrequency;
import ba.unsa.etf.employeemanagement.util.validation.ValueOfEnum;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FinanceRequest {

    @NotNull(message = "Employee ID is required")
    @Positive(message = "Employee ID must be a positive number")
    private Long employeeId;

    @NotBlank(message = "Bank name is required")
    @Size(max = 255, message = "Bank name must not exceed 255 characters")
    private String bankName;

    @NotBlank(message = "Bank account number is required")
    @Size(max = 255, message = "Bank account number must not exceed 255 characters")
    @Pattern(regexp = "^[A-Z0-9]+$", message = "Bank account number must contain only uppercase letters and digits")
    private String bankAccountNumber;

    @NotBlank(message = "IBAN is required")
    @Size(max = 255, message = "IBAN must not exceed 255 characters")
    @Pattern(regexp = "^[A-Z]{2}[0-9]{2}[A-Z0-9]{1,30}$", message = "IBAN must be a valid format")
    private String iban;

    @NotBlank(message = "Tax number is required")
    @Size(max = 255, message = "Tax number must not exceed 255 characters")
    private String taxNumber;

    @NotNull(message = "Salary is required")
    @Positive(message = "Salary must be a positive number")
    private Double salary;

    @NotBlank(message = "Currency is required")
    @ValueOfEnum(enumClass = Currency.class, message = "Currency must be one of: BAM, EUR, USD, GBP, CHF")
    private String currency;

    @NotBlank(message = "Payment frequency is required")
    @ValueOfEnum(enumClass = PaymentFrequency.class, message = "Payment frequency must be one of: MONTHLY, WEEKLY, BI_WEEKLY, ANNUALLY")
    private String paymentFrequency;

    @Min(value = 0, message = "Bonus eligible must be 0 or 1")
    @Max(value = 1, message = "Bonus eligible must be 0 or 1")
    private Integer bonusEligible;
}
