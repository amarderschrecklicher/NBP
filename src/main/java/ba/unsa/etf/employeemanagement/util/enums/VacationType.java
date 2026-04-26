package ba.unsa.etf.employeemanagement.util.enums;

public enum VacationType {
    ANNUAL("Annual Leave"),
    SICK("Sick Leave"),
    UNPAID("Unpaid Leave"),
    OTHER("Other");

    private final String description;

    VacationType(final String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
