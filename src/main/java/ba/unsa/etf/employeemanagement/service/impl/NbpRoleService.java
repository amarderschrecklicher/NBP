package ba.unsa.etf.employeemanagement.service.impl;

import ba.unsa.etf.employeemanagement.dto.request.NbpRoleRequest;
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

    public NbpRoleResponse update(Long id, NbpRoleRequest request) {
        if (repository.findById(id).isEmpty()) {
            throw new ResourceNotFoundException("NBP_ROLE not found");
        }

        NbpRole entity = mapper.mapToEntity(request);
        entity.setId(id);
        int rowsAffected = repository.update(id, entity);
        if (rowsAffected == 0) {
            throw new ResourceNotFoundException("NBP_ROLE not found");
        }

        NbpRole updated = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("NBP_ROLE not found after update"));
        return mapper.mapToResponse(updated);
    }

    public NbpRoleResponse save(NbpRoleRequest request) {
        NbpRole entity = mapper.mapToEntity(request);
        Long generatedId = repository.save(entity);
        NbpRole saved = repository.findById(generatedId)
                .orElseThrow(() -> new ResourceNotFoundException("NBP_ROLE not found after save"));
        return mapper.mapToResponse(saved);
    }

    public long count() {
        return repository.count();
    }
}

