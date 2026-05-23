package ba.unsa.etf.employeemanagement.dto.plsql;

public record RemainingVacationDays(Long employeeId, Integer year, int usedDays, int remainingDays) {
}
