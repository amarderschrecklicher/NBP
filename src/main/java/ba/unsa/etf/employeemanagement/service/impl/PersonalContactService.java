package ba.unsa.etf.employeemanagement.service.impl;

import ba.unsa.etf.employeemanagement.dto.request.PersonalContactRequest;
import ba.unsa.etf.employeemanagement.dto.response.PersonalContactResponse;
import ba.unsa.etf.employeemanagement.exceptions.ResourceNotFoundException;
import ba.unsa.etf.employeemanagement.mapper.PersonalContactMapper;
import ba.unsa.etf.employeemanagement.model.PersonalContact;
import ba.unsa.etf.employeemanagement.repository.PersonalContactRepository;
import ba.unsa.etf.employeemanagement.service.api.IPersonalContactService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PersonalContactService implements IPersonalContactService {

    private final PersonalContactRepository personalContactRepository;
    private final PersonalContactMapper personalContactMapper;

    @Override
    public List<PersonalContactResponse> findAll() {
        return personalContactRepository.findAll()
                .stream()
                .map(personalContactMapper::mapToResponse)
                .toList();
    }

    @Override
    public PersonalContactResponse findById(Long id) {
        PersonalContact personalContact = personalContactRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Personal contact not found with id: " + id));
        return personalContactMapper.mapToResponse(personalContact);
    }

    @Override
    public PersonalContactResponse save(PersonalContactRequest request) {
        PersonalContact personalContact = personalContactMapper.mapToEntity(request);
        Long generatedId = personalContactRepository.save(personalContact);

        PersonalContact saved = personalContactRepository.findById(generatedId)
                .orElseThrow(() -> new ResourceNotFoundException("Personal contact not found after save"));

        return personalContactMapper.mapToResponse(saved);
    }

    @Override
    public PersonalContactResponse update(Long id, PersonalContactRequest request) {
        PersonalContact existing = personalContactRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Personal contact not found with id: " + id));

        PersonalContact personalContact = personalContactMapper.mapToEntity(request);
        personalContact.setId(existing.getId());

        personalContactRepository.update(id, personalContact);

        PersonalContact updated = personalContactRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Personal contact not found after update"));

        return personalContactMapper.mapToResponse(updated);
    }

    @Override
    public void delete(Long id) {
        personalContactRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Personal contact not found with id: " + id));

        personalContactRepository.deleteById(id);
    }
}