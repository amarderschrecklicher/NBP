package ba.unsa.etf.employeemanagement.model.mongo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(collection = "login_events")
public class LoginEvent {
    @Id
    private String id;
    private Long userId;
    private Instant timestamp;
}

