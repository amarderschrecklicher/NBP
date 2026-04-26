package ba.unsa.etf.employeemanagement.util.enums;

public enum VacationStatus {
    PENDING("Pending"),
    APPROVED("Approved"),
    REJECTED("Rejected");

    private final String description;

    VacationStatus(final String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}

