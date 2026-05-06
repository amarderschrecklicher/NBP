package ba.unsa.etf.employeemanagement.service;

import ba.unsa.etf.employeemanagement.dto.request.PersonalContactRequest;
import ba.unsa.etf.employeemanagement.dto.response.PersonalContactResponse;
import ba.unsa.etf.employeemanagement.exceptions.ResourceNotFoundException;
import ba.unsa.etf.employeemanagement.mapper.PersonalContactMapper;
import ba.unsa.etf.employeemanagement.model.PersonalContact;
import ba.unsa.etf.employeemanagement.repository.PersonalContactRepository;
import ba.unsa.etf.employeemanagement.service.impl.PersonalContactService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PersonalContactServiceTest {

    @Mock
    private PersonalContactRepository repository;

    @Mock
    private PersonalContactMapper mapper;

    @InjectMocks
    private PersonalContactService service;

    @Test
    void findAll_returnsMappedResponses() {
        PersonalContact entity = new PersonalContact(1L, 1L, "+38761123456", "test@example.com");
        PersonalContactResponse response = PersonalContactResponse.builder().id(1L).employeeId(1L).phoneNumber("+38761123456").build();

        when(repository.findAll()).thenReturn(List.of(entity));
        when(mapper.mapToResponse(entity)).thenReturn(response);

        List<PersonalContactResponse> results = service.findAll();

        assertEquals(1, results.size());
        assertEquals("+38761123456", results.get(0).getPhoneNumber());
        verify(repository).findAll();
    }

    @Test
    void findById_whenMissing_throwsResourceNotFound() {
        when(repository.findById(99L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> service.findById(99L));
    }

    @Test
    void save_persistsAndReturnsResponse() {
        PersonalContactRequest request = new PersonalContactRequest(1L, "+38761123456", "test@example.com");
        PersonalContact mapped = new PersonalContact(null, 1L, "+38761123456", "test@example.com");
        PersonalContact saved = new PersonalContact(1L, 1L, "+38761123456", "test@example.com");
        PersonalContactResponse response = PersonalContactResponse.builder().id(1L).phoneNumber("+38761123456").build();

        when(mapper.mapToEntity(request)).thenReturn(mapped);
        when(repository.save(mapped)).thenReturn(1L);
        when(repository.findById(1L)).thenReturn(Optional.of(saved));
        when(mapper.mapToResponse(saved)).thenReturn(response);

        PersonalContactResponse result = service.save(request);

        assertEquals(1L, result.getId());
        verify(repository).save(mapped);
    }

    @Test
    void update_whenExists_updatesById() {
        PersonalContactRequest request = new PersonalContactRequest(1L, "+38761654321", "new@example.com");
        PersonalContact existing = new PersonalContact(1L, 1L, "+38761123456", "old@example.com");
        PersonalContact mapped = new PersonalContact(null, 1L, "+38761654321", "new@example.com");
        PersonalContact updated = new PersonalContact(1L, 1L, "+38761654321", "new@example.com");
        PersonalContactResponse response = PersonalContactResponse.builder().id(1L).phoneNumber("+38761654321").build();

        when(repository.findById(1L)).thenReturn(Optional.of(existing), Optional.of(updated));
        when(mapper.mapToEntity(request)).thenReturn(mapped);
        when(mapper.mapToResponse(updated)).thenReturn(response);

        PersonalContactResponse result = service.update(1L, request);

        ArgumentCaptor<PersonalContact> captor = ArgumentCaptor.forClass(PersonalContact.class);
        verify(repository).update(eq(1L), captor.capture());
        assertEquals("+38761654321", captor.getValue().getPhoneNumber());
        assertEquals("+38761654321", result.getPhoneNumber());
    }

    @Test
    void delete_whenMissing_throwsResourceNotFound() {
        when(repository.findById(7L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> service.delete(7L));
    }
}
