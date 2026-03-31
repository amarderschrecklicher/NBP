package ba.unsa.etf.employeemanagement.service.impl;

import ba.unsa.etf.employeemanagement.dto.request.DisabilityRequest;
import ba.unsa.etf.employeemanagement.dto.response.DisabilityResponse;
import ba.unsa.etf.employeemanagement.exceptions.ResourceNotFoundException;
import ba.unsa.etf.employeemanagement.mapper.DisabilityMapper;
import ba.unsa.etf.employeemanagement.model.Disability;
import ba.unsa.etf.employeemanagement.repository.DisabilityRepository;
import ba.unsa.etf.employeemanagement.service.api.IDisabilityService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DisabilityService implements IDisabilityService {

    private final DisabilityRepository disabilityRepository;
    private final DisabilityMapper disabilityMapper;

    @Override
    public List<DisabilityResponse> findAll() {
        return disabilityRepository.findAll()
                .stream()
                .map(disabilityMapper::mapToResponse)
                .toList();
    }

    @Override
    public DisabilityResponse findById(Long id) {
        Disability disability = disabilityRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Disability not found with id: " + id));
        return disabilityMapper.mapToResponse(disability);
    }

    @Override
    public DisabilityResponse save(DisabilityRequest request) {
        Disability disability = disabilityMapper.mapToEntity(request);
        Long id = disabilityRepository.save(disability);

        Disability saved = disabilityRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Disability not found after save"));

        return disabilityMapper.mapToResponse(saved);
    }

    @Override
    public DisabilityResponse update(Long id, DisabilityRequest request) {
        Disability existing = disabilityRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Disability not found with id: " + id));

        Disability disability = disabilityMapper.mapToEntity(request);
        disability.setId(existing.getId());

        disabilityRepository.update(id, disability);

        Disability updated = disabilityRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Disability not found after update"));

        return disabilityMapper.mapToResponse(updated);
    }

    @Override
    public void delete(Long id) {
        disabilityRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Disability not found with id: " + id));

        disabilityRepository.deleteById(id);
    }
}
