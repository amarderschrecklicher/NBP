package ba.unsa.etf.employeemanagement.service.impl.nbp;
import ba.unsa.etf.employeemanagement.dto.logs.NbpLogsDto;
import ba.unsa.etf.employeemanagement.mapper.nbp.NbpLogsMapper;
import ba.unsa.etf.employeemanagement.repository.nbp.NbpLogsRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class NbpLogsService {

    private final NbpLogsRepository nbpLogRepository;
    private final NbpLogsMapper nbpLogMapper;

    public List<NbpLogsDto> findAll() {
        log.info("Finding all NBP_LOG entries");
        return nbpLogRepository.findAll()
                .stream()
                .map(nbpLogMapper::toDto)
                .collect(Collectors.toList());
    }

    public NbpLogsDto findById(Long id) {
        log.info("Finding NBP_LOG by id: {}", id);
        return nbpLogRepository.findById(id)
                .map(nbpLogMapper::toDto)
                .orElseThrow(() -> new RuntimeException("NBP_LOG not found with id: " + id));
    }

    public List<NbpLogsDto> findByTableName(String tableName) {
        log.info("Finding NBP_LOG entries by table name: {}", tableName);
        return nbpLogRepository.findByTableName(tableName)
                .stream()
                .map(nbpLogMapper::toDto)
                .collect(Collectors.toList());
    }

    public List<NbpLogsDto> findByActionName(String actionName) {
        log.info("Finding NBP_LOG entries by action name: {}", actionName);
        return nbpLogRepository.findByActionName(actionName)
                .stream()
                .map(nbpLogMapper::toDto)
                .collect(Collectors.toList());
    }

    public List<NbpLogsDto> findByDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        log.info("Finding NBP_LOG entries between {} and {}", startDate, endDate);
        return nbpLogRepository.findByDateRange(startDate, endDate)
                .stream()
                .map(nbpLogMapper::toDto)
                .collect(Collectors.toList());
    }

    public List<NbpLogsDto> findByDbUser(String dbUser) {
        log.info("Finding NBP_LOG entries by db user: {}", dbUser);
        return nbpLogRepository.findByDbUser(dbUser)
                .stream()
                .map(nbpLogMapper::toDto)
                .collect(Collectors.toList());
    }

    public List<NbpLogsDto> findRecentLogs(int limit) {
        log.info("Finding {} most recent NBP_LOG entries", limit);
        return nbpLogRepository.findRecentLogs(limit)
                .stream()
                .map(nbpLogMapper::toDto)
                .collect(Collectors.toList());
    }

    public long count() {
        return nbpLogRepository.count();
    }

    public long countByTableName(String tableName) {
        return nbpLogRepository.countByTableName(tableName);
    }
}

