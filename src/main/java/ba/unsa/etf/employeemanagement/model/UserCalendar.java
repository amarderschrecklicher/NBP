package ba.unsa.etf.employeemanagement.model;

import java.util.Set;

public class UserCalendar {
    private Long userId;
    private Set<Integer> workingDays; // 1=Sunday, ..., 7=Saturday (Java Calendar)

    public UserCalendar() {}
    public UserCalendar(Long userId, Set<Integer> workingDays) {
        this.userId = userId;
        this.workingDays = workingDays;
    }
    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
    public Set<Integer> getWorkingDays() { return workingDays; }
    public void setWorkingDays(Set<Integer> workingDays) { this.workingDays = workingDays; }
}
