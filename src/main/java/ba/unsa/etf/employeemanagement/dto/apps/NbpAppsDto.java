package ba.unsa.etf.employeemanagement.dto.apps;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NbpAppsDto {
    private Long id;
    private String appId;
    private Long managerId;
    private LocalDate expiryDate;
}
