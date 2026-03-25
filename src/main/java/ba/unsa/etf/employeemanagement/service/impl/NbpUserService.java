package ba.unsa.etf.employeemanagement.service.impl;

import ba.unsa.etf.employeemanagement.dto.response.NbpUserResponse;
import ba.unsa.etf.employeemanagement.exceptions.ResourceNotFoundException;
import ba.unsa.etf.employeemanagement.mapper.NbpUserMapper;
import ba.unsa.etf.employeemanagement.model.NbpUser;
import ba.unsa.etf.employeemanagement.repository.NbpUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NbpUserService {

    private final NbpUserRepository repository;
    private final NbpUserMapper mapper;

    public List<NbpUserResponse> findAll() {
        return repository.findAll().stream().map(mapper::mapToResponse).toList();
    }

    public NbpUserResponse findById(Long id) {
        NbpUser entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("NBP_USER not found"));
        return mapper.mapToResponse(entity);
    }

    public NbpUserResponse update(Long id, NbpUserResponse dto) {
        if (repository.findById(id).isEmpty()) {
            throw new ResourceNotFoundException("NBP_USER not found");
        }

        dto.setId(id); // ID dolazi iz path-a, ne iz request body-a.
        NbpUser entity = mapper.mapToEntity(dto);
        entity.setId(id);
        repository.update(id, entity);
        return mapper.mapToResponse(repository.findById(id).orElseThrow());
    }

    public long count() {
        return repository.count();
    }
}

