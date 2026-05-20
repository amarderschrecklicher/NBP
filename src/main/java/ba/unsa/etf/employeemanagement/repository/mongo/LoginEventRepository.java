package ba.unsa.etf.employeemanagement.repository.mongo;

import ba.unsa.etf.employeemanagement.model.mongo.LoginEvent;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LoginEventRepository extends MongoRepository<LoginEvent, String> {
    List<LoginEvent> findByUserIdOrderByTimestampDesc(Long userId);
}

