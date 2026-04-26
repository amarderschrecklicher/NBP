package ba.unsa.etf.employeemanagement.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserCalendar {
    private Long userId;
    private Set<Integer> workingDays; // 1=Sunday, ..., 7=Saturday (Java Calendar)
}
