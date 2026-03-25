package ba.unsa.etf.employeemanagement.service.impl;

import ba.unsa.etf.employeemanagement.dto.request.DepartmentRequest;
import ba.unsa.etf.employeemanagement.dto.response.DepartmentResponse;
import ba.unsa.etf.employeemanagement.exceptions.ResourceNotFoundException;
import ba.unsa.etf.employeemanagement.mapper.DepartmentMapper;
import ba.unsa.etf.employeemanagement.model.Department;
import ba.unsa.etf.employeemanagement.repository.DepartmentRepository;
import ba.unsa.etf.employeemanagement.service.api.IDepartmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DepartmentService implements IDepartmentService {
    private final DepartmentRepository repository;
    private final DepartmentMapper mapper;

    @Override
    public List<DepartmentResponse> findAll() {
        return repository.findAll().stream().map(mapper::mapToResponse).toList();
    }

    @Override
    public DepartmentResponse findById(Long id) {
        Department entity = repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Department not found"));
        return mapper.mapToResponse(entity);
    }

    @Override
    public DepartmentResponse save(DepartmentRequest request) {
        Department entity = mapper.mapToEntity(request);
        Long id = repository.save(entity);
        return mapper.mapToResponse(repository.findById(id).orElseThrow());
    }

    @Override
    public DepartmentResponse update(Long id, DepartmentRequest request) {
        if(repository.findById(id).isEmpty()) throw new ResourceNotFoundException("Department not found");
        Department entity = mapper.mapToEntity(request);
        entity.setId(id);
        repository.update(id, entity);
        return mapper.mapToResponse(repository.findById(id).orElseThrow());
    }

    @Override
    public void delete(Long id) {
        if(repository.findById(id).isEmpty()) throw new ResourceNotFoundException("Department not found");
        repository.deleteById(id);
    }
}

