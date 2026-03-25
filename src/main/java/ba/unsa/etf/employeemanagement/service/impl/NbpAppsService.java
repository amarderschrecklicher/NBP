package ba.unsa.etf.employeemanagement.service.impl;

import ba.unsa.etf.employeemanagement.dto.apps.NbpAppsDto;
import ba.unsa.etf.employeemanagement.mapper.NbpAppsMapper;
import ba.unsa.etf.employeemanagement.model.NbpApps;
import ba.unsa.etf.employeemanagement.repository.NbpAppsRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class NbpAppsService {

    private final NbpAppsRepository nbpAppsRepository;
    private final NbpAppsMapper nbpAppsMapper;

    public List<NbpAppsDto> findAll() {
        log.info("Finding all NBP_APPS");
        return nbpAppsRepository.findAll()
                .stream()
                .map(nbpAppsMapper::toDto)
                .collect(Collectors.toList());
    }

    public NbpAppsDto findById(Long id) {
        log.info("Finding NBP_APPS by id: {}", id);
        return nbpAppsRepository.findById(id)
                .map(nbpAppsMapper::toDto)
                .orElseThrow(() -> new RuntimeException("NBP_APPS not found with id: " + id));
    }

    public List<NbpAppsDto> findByManagerId(Long managerId) {
        log.info("Finding NBP_APPS by manager id: {}", managerId);
        return nbpAppsRepository.findByManagerId(managerId)
                .stream()
                .map(nbpAppsMapper::toDto)
                .collect(Collectors.toList());
    }

    public List<NbpAppsDto> findExpiredApps() {
        log.info("Finding expired NBP_APPS");
        return nbpAppsRepository.findExpiredApps()
                .stream()
                .map(nbpAppsMapper::toDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public NbpAppsDto update(Long id, NbpAppsDto nbpAppsDto) {
        log.info("Updating NBP_APPS with id: {}", id);
        nbpAppsRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("NBP_APPS not found with id: " + id));

        nbpAppsDto.setId(id);
        NbpApps nbpApps = nbpAppsMapper.toEntity(nbpAppsDto);
        NbpApps updatedApps = nbpAppsRepository.save(nbpApps);
        return nbpAppsMapper.toDto(updatedApps);
    }

    public long count() {
        return nbpAppsRepository.count();
    }
}

