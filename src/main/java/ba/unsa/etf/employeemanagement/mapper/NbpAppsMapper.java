package ba.unsa.etf.employeemanagement.mapper;
import ba.unsa.etf.employeemanagement.dto.apps.NbpAppsDto;
import ba.unsa.etf.employeemanagement.model.NbpApps;
import org.springframework.stereotype.Component;

@Component
public class NbpAppsMapper {

    public NbpAppsDto toDto(NbpApps entity) {
        if (entity == null) {
            return null;
        }
        return NbpAppsDto.builder()
                .id(entity.getId())
                .appId(entity.getAppId())
                .managerId(entity.getManagerId())
                .expiryDate(entity.getExpiryDate())
                .build();
    }

    public NbpApps toEntity(NbpAppsDto dto) {
        if (dto == null) {
            return null;
        }
        return NbpApps.builder()
                .id(dto.getId())
                .appId(dto.getAppId())
                .managerId(dto.getManagerId())
                .expiryDate(dto.getExpiryDate())
                .build();
    }
}

