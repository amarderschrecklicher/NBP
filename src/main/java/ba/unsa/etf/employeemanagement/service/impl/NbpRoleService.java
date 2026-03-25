package ba.unsa.etf.employeemanagement.service.impl;

import ba.unsa.etf.employeemanagement.dto.response.NbpRoleResponse;
import ba.unsa.etf.employeemanagement.exceptions.ResourceNotFoundException;
import ba.unsa.etf.employeemanagement.mapper.NbpRoleMapper;
import ba.unsa.etf.employeemanagement.model.NbpRole;
import ba.unsa.etf.employeemanagement.repository.NbpRoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NbpRoleService {

    private final NbpRoleRepository repository;
    private final NbpRoleMapper mapper;

    public List<NbpRoleResponse> findAll() {
        return repository.findAll().stream().map(mapper::mapToResponse).toList();
    }

    public NbpRoleResponse findById(Long id) {
        NbpRole entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("NBP_ROLE not found"));
        return mapper.mapToResponse(entity);
    }

    public NbpRoleResponse update(Long id, NbpRoleResponse dto) {
        if (repository.findById(id).isEmpty()) {
            throw new ResourceNotFoundException("NBP_ROLE not found");
        }

        dto.setId(id); // ID dolazi iz path-a.
        NbpRole entity = mapper.mapToEntity(dto);
        entity.setId(id);
        repository.update(id, entity);
        return mapper.mapToResponse(repository.findById(id).orElseThrow());
    }

    public long count() {
        return repository.count();
    }
}

