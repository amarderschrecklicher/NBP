package ba.unsa.etf.employeemanagement.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmployeePhoto {
    private Long id;
    private Long userId;
    private byte[] photo;
    private String contentType;
    private LocalDateTime uploadedAt;
}


