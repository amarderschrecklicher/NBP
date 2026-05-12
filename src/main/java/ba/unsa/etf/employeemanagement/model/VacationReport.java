package ba.unsa.etf.employeemanagement.model;

import lombok.*;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VacationReport {
    private Long id;
    private Integer reportMonth;
    private Integer reportYear;
    private byte[] pdfContent; // BLOB
    private Date generatedAt;
    private Long generatedBy;
}

