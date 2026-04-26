package ba.unsa.etf.employeemanagement.model.nbp;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NbpLogs {
    private Long id;
    private String actionName;
    private String tableName;
    private LocalDateTime dateTime;
    private String dbUser;
}

