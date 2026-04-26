package ba.unsa.etf.employeemanagement.mapper.nbp;
import ba.unsa.etf.employeemanagement.dto.logs.NbpLogsDto;
import ba.unsa.etf.employeemanagement.model.nbp.NbpLogs;
import org.springframework.stereotype.Component;

@Component
public class NbpLogsMapper {

    public NbpLogsDto toDto(NbpLogs entity) {
        if (entity == null) {
            return null;
        }
        return NbpLogsDto.builder()
                .id(entity.getId())
                .actionName(entity.getActionName())
                .tableName(entity.getTableName())
                .dateTime(entity.getDateTime())
                .dbUser(entity.getDbUser())
                .build();
    }

    public NbpLogs toEntity(NbpLogsDto dto) {
        if (dto == null) {
            return null;
        }
        return NbpLogs.builder()
                .id(dto.getId())
                .actionName(dto.getActionName())
                .tableName(dto.getTableName())
                .dateTime(dto.getDateTime())
                .dbUser(dto.getDbUser())
                .build();
    }
}

