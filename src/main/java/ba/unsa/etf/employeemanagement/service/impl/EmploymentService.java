package ba.unsa.etf.employeemanagement.service.impl;

import ba.unsa.etf.employeemanagement.dto.request.EmploymentRequest;
import ba.unsa.etf.employeemanagement.dto.response.EmploymentResponse;
import ba.unsa.etf.employeemanagement.exceptions.ResourceNotFoundException;
import ba.unsa.etf.employeemanagement.mapper.EmploymentMapper;
import ba.unsa.etf.employeemanagement.model.Employment;
import ba.unsa.etf.employeemanagement.repository.EmploymentRepository;
import ba.unsa.etf.employeemanagement.service.api.IEmploymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class EmploymentService implements IEmploymentService {

    private final EmploymentRepository employmentRepository;

    private  final EmploymentMapper employmentMapper;

    @Override
    public List<EmploymentResponse> findAll() {
        return employmentRepository.findAll()
                .stream()
                .map(employmentMapper::mapToResponse)
                .toList();
    }

    @Override
    public EmploymentResponse findById(Long id) {
        Employment employment = employmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Employment not found with id: " + id));
        return employmentMapper.mapToResponse(employment);
    }

    @Override
    public EmploymentResponse save(EmploymentRequest request) {
        Employment employment = employmentMapper.mapToEntity(request);
        Long generatedId = employmentRepository.save(employment);
        Employment saved = employmentRepository.findById(generatedId)
                .orElseThrow(() -> new ResourceNotFoundException("Employment not found after save"));
        return employmentMapper.mapToResponse(saved);
    }

    @Override
    public EmploymentResponse update(Long id, EmploymentRequest request) {
        Employment existing = employmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Employment not found with id: " + id));

        Employment employment = employmentMapper.mapToEntity(request);
        employment.setId(existing.getId());

        employmentRepository.update(id, employment);

        Employment updated = employmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Employment not found after update"));

        return employmentMapper.mapToResponse(updated);
    }

    @Override
    public void delete(Long id) {
        employmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Employment not found with id: " + id));

        employmentRepository.deleteById(id);
    }

}
