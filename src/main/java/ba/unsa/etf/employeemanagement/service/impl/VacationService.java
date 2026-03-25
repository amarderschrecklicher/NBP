package ba.unsa.etf.employeemanagement.service.impl;

import ba.unsa.etf.employeemanagement.dto.request.VacationRequest;
import ba.unsa.etf.employeemanagement.dto.response.VacationResponse;
import ba.unsa.etf.employeemanagement.exceptions.ResourceNotFoundException;
import ba.unsa.etf.employeemanagement.mapper.VacationMapper;
import ba.unsa.etf.employeemanagement.model.Vacation;
import ba.unsa.etf.employeemanagement.repository.VacationRepository;
import ba.unsa.etf.employeemanagement.service.api.IVacationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class VacationService implements IVacationService {
    private final VacationRepository repository;
    private final VacationMapper mapper;

    @Override
    public List<VacationResponse> findAll() {
        return repository.findAll().stream().map(mapper::mapToResponse).toList();
    }

    @Override
    public VacationResponse findById(Long id) {
        Vacation entity = repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Vacation not found"));
        return mapper.mapToResponse(entity);
    }

    @Override
    public VacationResponse save(VacationRequest request) {
        Vacation entity = mapper.mapToEntity(request);
        Long id = repository.save(entity);
        return mapper.mapToResponse(repository.findById(id).orElseThrow());
    }

    @Override
    public VacationResponse update(Long id, VacationRequest request) {
        if(repository.findById(id).isEmpty()) throw new ResourceNotFoundException("Vacation not found");
        Vacation entity = mapper.mapToEntity(request);
        entity.setId(id);
        repository.update(id, entity);
        return mapper.mapToResponse(repository.findById(id).orElseThrow());
    }

    @Override
    public void delete(Long id) {
        if(repository.findById(id).isEmpty()) throw new ResourceNotFoundException("Vacation not found");
        repository.deleteById(id);
    }
}

