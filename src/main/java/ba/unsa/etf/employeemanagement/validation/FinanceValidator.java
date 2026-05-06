package ba.unsa.etf.employeemanagement.validation;

import ba.unsa.etf.employeemanagement.model.Finance;
import ba.unsa.etf.employeemanagement.util.enums.Currency;
import ba.unsa.etf.employeemanagement.util.enums.PaymentFrequency;

import java.util.Arrays;
import java.util.stream.Collectors;

public class FinanceValidator {

    public static void validate(Finance finance) {
        if (finance == null) {
            throw new IllegalArgumentException("Finance cannot be null");
        }
        if (finance.getEmployeeId() == null) {
            throw new IllegalArgumentException("Employee ID is required");
        }
        if (finance.getBankName() == null || finance.getBankName().trim().isEmpty()) {
            throw new IllegalArgumentException("Bank name is required");
        }
        if (finance.getBankAccountNumber() == null || finance.getBankAccountNumber().trim().isEmpty()) {
            throw new IllegalArgumentException("Bank account number is required");
        }
        if (finance.getIban() == null || finance.getIban().trim().isEmpty()) {
            throw new IllegalArgumentException("IBAN is required");
        }
        if (finance.getTaxNumber() == null || finance.getTaxNumber().trim().isEmpty()) {
            throw new IllegalArgumentException("Tax number is required");
        }
        if (finance.getSalary() == null || finance.getSalary() <= 0) {
            throw new IllegalArgumentException("Salary must be a positive number");
        }
        if (finance.getCurrency() == null || !isValidCurrency(finance.getCurrency())) {
            throw new IllegalArgumentException("Invalid currency. Must be one of: " + 
                Arrays.stream(Currency.values()).map(Enum::name).collect(Collectors.joining(", ")));
        }
        if (finance.getPaymentFrequency() == null || !isValidPaymentFrequency(finance.getPaymentFrequency())) {
            throw new IllegalArgumentException("Invalid payment frequency. Must be one of: " + 
                Arrays.stream(PaymentFrequency.values()).map(Enum::name).collect(Collectors.joining(", ")));
        }
        if (finance.getBonusEligible() != null && (finance.getBonusEligible() < 0 || finance.getBonusEligible() > 1)) {
            throw new IllegalArgumentException("Bonus eligible must be 0 or 1");
        }
    }

    private static boolean isValidCurrency(String currency) {
        try {
            Currency.valueOf(currency);
            return true;
        } catch (IllegalArgumentException | NullPointerException e) {
            return false;
        }
    }

    private static boolean isValidPaymentFrequency(String frequency) {
        try {
            PaymentFrequency.valueOf(frequency);
            return true;
        } catch (IllegalArgumentException | NullPointerException e) {
            return false;
        }
    }
}
