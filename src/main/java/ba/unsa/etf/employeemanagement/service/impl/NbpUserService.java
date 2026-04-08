package ba.unsa.etf.employeemanagement.service.impl;

import ba.unsa.etf.employeemanagement.dto.request.NbpUserRequest;
import ba.unsa.etf.employeemanagement.dto.response.NbpUserResponse;
import ba.unsa.etf.employeemanagement.exceptions.ResourceNotFoundException;
import ba.unsa.etf.employeemanagement.mapper.NbpUserMapper;
import ba.unsa.etf.employeemanagement.model.NbpUser;
import ba.unsa.etf.employeemanagement.repository.NbpUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

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

    public NbpUserResponse findByUsername(String username) {
        NbpUser entity = repository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("NBP_USER not found with username: " + username));
        return mapper.mapToResponse(entity);
    }

    public NbpUserResponse update(Long id, NbpUserRequest request) {
        repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("NBP_USER not found"));

        NbpUser entity = mapper.mapToEntity(request);
        entity.setId(id);

        // Ako password nije poslan ili je prazan, zadrži postojeći.
        if (!StringUtils.hasText(request.getPassword())) {
            String existingPassword = repository.findPasswordById(id).orElse(null);
            entity.setPassword(existingPassword);
        }

        int rowsAffected = repository.update(id, entity);
        if (rowsAffected == 0) {
            throw new ResourceNotFoundException("NBP_USER not found");
        }

        NbpUser updated = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("NBP_USER not found after update"));
        return mapper.mapToResponse(updated);
    }

    public NbpUserResponse save(NbpUserRequest request) {
        if (!StringUtils.hasText(request.getPassword())) {
            throw new IllegalArgumentException("Password is required");
        }
        NbpUser entity = mapper.mapToEntity(request);
        Long generatedId = repository.save(entity);
        NbpUser saved = repository.findById(generatedId)
                .orElseThrow(() -> new ResourceNotFoundException("NBP_USER not found after save"));
        return mapper.mapToResponse(saved);
    }

    public long count() {
        return repository.count();
    }
}

