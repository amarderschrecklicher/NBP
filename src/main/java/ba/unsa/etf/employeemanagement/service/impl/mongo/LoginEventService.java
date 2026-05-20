package ba.unsa.etf.employeemanagement.service.impl.mongo;

import ba.unsa.etf.employeemanagement.model.mongo.LoginEvent;
import ba.unsa.etf.employeemanagement.repository.mongo.LoginEventRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class LoginEventService {

    private final LoginEventRepository repository;

    public void saveLoginEvent(Long userId) {
        try {
            LoginEvent e = LoginEvent.builder()
                    .userId(userId)
                    .timestamp(Instant.now())
                    .build();
            repository.save(e);
        } catch (Exception ex) {
            // Log and swallow so login flow isn't affected by Mongo issues
            log.warn("Failed to persist login event for user {}: {}", userId, ex.getMessage());
        }
    }

    public List<LoginEvent> findByUserId(Long userId) {
        return repository.findByUserIdOrderByTimestampDesc(userId);
    }
}

