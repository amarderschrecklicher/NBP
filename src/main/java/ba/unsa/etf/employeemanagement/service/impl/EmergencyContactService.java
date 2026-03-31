package ba.unsa.etf.employeemanagement.service.impl;

import ba.unsa.etf.employeemanagement.dto.request.EmergencyContactRequest;
import ba.unsa.etf.employeemanagement.dto.response.EmergencyContactResponse;
import ba.unsa.etf.employeemanagement.exceptions.ResourceNotFoundException;
import ba.unsa.etf.employeemanagement.mapper.EmergencyContactMapper;
import ba.unsa.etf.employeemanagement.model.EmergencyContact;
import ba.unsa.etf.employeemanagement.repository.EmergencyContactRepository;
import ba.unsa.etf.employeemanagement.service.api.IEmergencyContactService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EmergencyContactService implements IEmergencyContactService {

    private final EmergencyContactRepository emergencyContactRepository;
    private final EmergencyContactMapper emergencyContactMapper;

    @Override
    public List<EmergencyContactResponse> findAll() {
        return emergencyContactRepository.findAll()
                .stream()
                .map(emergencyContactMapper::mapToResponse)
                .toList();
    }

    @Override
    public EmergencyContactResponse findById(Long id) {
        EmergencyContact emergencyContact = emergencyContactRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Emergency contact not found with id: " + id));
        return emergencyContactMapper.mapToResponse(emergencyContact);
    }

    @Override
    public EmergencyContactResponse save(EmergencyContactRequest request) {
        EmergencyContact emergencyContact = emergencyContactMapper.mapToEntity(request);
        Long generatedId = emergencyContactRepository.save(emergencyContact);

        EmergencyContact saved = emergencyContactRepository.findById(generatedId)
                .orElseThrow(() -> new ResourceNotFoundException("Emergency contact not found after save"));

        return emergencyContactMapper.mapToResponse(saved);
    }

    @Override
    public EmergencyContactResponse update(Long id, EmergencyContactRequest request) {
        EmergencyContact existing = emergencyContactRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Emergency contact not found with id: " + id));

        EmergencyContact emergencyContact = emergencyContactMapper.mapToEntity(request);
        emergencyContact.setId(existing.getId());

        emergencyContactRepository.update(id, emergencyContact);

        EmergencyContact updated = emergencyContactRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Emergency contact not found after update"));

        return emergencyContactMapper.mapToResponse(updated);
    }

    @Override
    public void delete(Long id) {
        emergencyContactRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Emergency contact not found with id: " + id));

        emergencyContactRepository.deleteById(id);
    }
}