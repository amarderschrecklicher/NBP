package ba.unsa.etf.employeemanagement.service;

import ba.unsa.etf.employeemanagement.dto.request.EmergencyContactRequest;
import ba.unsa.etf.employeemanagement.dto.response.EmergencyContactResponse;
import ba.unsa.etf.employeemanagement.exceptions.ResourceNotFoundException;
import ba.unsa.etf.employeemanagement.mapper.EmergencyContactMapper;
import ba.unsa.etf.employeemanagement.model.EmergencyContact;
import ba.unsa.etf.employeemanagement.repository.EmergencyContactRepository;
import ba.unsa.etf.employeemanagement.service.impl.EmergencyContactService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EmergencyContactServiceTest {

    @Mock
    private EmergencyContactRepository repository;

    @Mock
    private EmergencyContactMapper mapper;

    @InjectMocks
    private EmergencyContactService service;

    @Test
    void findAll_ReturnsList() {
        EmergencyContact contact = new EmergencyContact();
        EmergencyContactResponse response = new EmergencyContactResponse();
        when(repository.findAll()).thenReturn(List.of(contact));
        when(mapper.mapToResponse(contact)).thenReturn(response);

        List<EmergencyContactResponse> result = service.findAll();

        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
        verify(repository).findAll();
    }

    @Test
    void findById_WhenExists_ReturnsResponse() {
        EmergencyContact contact = new EmergencyContact();
        EmergencyContactResponse response = new EmergencyContactResponse();
        when(repository.findById(1L)).thenReturn(Optional.of(contact));
        when(mapper.mapToResponse(contact)).thenReturn(response);

        EmergencyContactResponse result = service.findById(1L);

        assertNotNull(result);
        verify(repository).findById(1L);
    }

    @Test
    void findById_WhenNotExists_ThrowsException() {
        when(repository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> service.findById(1L));
    }

    @Test
    void save_ValidRequest_ReturnsResponse() {
        EmergencyContactRequest request = new EmergencyContactRequest(1L, "John", "Doe", "Brother", "123456789", "john@example.com", "Main St");
        EmergencyContact contact = new EmergencyContact(null, 1L, "John", "Doe", "Brother", "123456789", "john@example.com", "Main St");
        EmergencyContact savedContact = new EmergencyContact(1L, 1L, "John", "Doe", "Brother", "123456789", "john@example.com", "Main St");
        EmergencyContactResponse response = new EmergencyContactResponse();

        when(mapper.mapToEntity(request)).thenReturn(contact);
        when(repository.save(contact)).thenReturn(1L);
        when(repository.findById(1L)).thenReturn(Optional.of(savedContact));
        when(mapper.mapToResponse(savedContact)).thenReturn(response);

        EmergencyContactResponse result = service.save(request);

        assertNotNull(result);
        verify(repository).save(contact);
    }

    @Test
    void update_WhenExists_ReturnsResponse() {
        EmergencyContactRequest request = new EmergencyContactRequest(1L, "John", "Updated", "Brother", "123456789", "john@example.com", "Main St");
        EmergencyContact existing = new EmergencyContact(1L, 1L, "John", "Doe", "Brother", "123456789", "john@example.com", "Main St");
        EmergencyContact updated = new EmergencyContact(1L, 1L, "John", "Updated", "Brother", "123456789", "john@example.com", "Main St");
        EmergencyContactResponse response = new EmergencyContactResponse();

        when(repository.findById(1L)).thenReturn(Optional.of(existing), Optional.of(updated));
        when(mapper.mapToEntity(request)).thenReturn(updated);
        when(mapper.mapToResponse(updated)).thenReturn(response);

        EmergencyContactResponse result = service.update(1L, request);

        assertNotNull(result);
        verify(repository).update(eq(1L), any(EmergencyContact.class));
    }

    @Test
    void delete_WhenExists_CallsRepository() {
        EmergencyContact contact = new EmergencyContact();
        when(repository.findById(1L)).thenReturn(Optional.of(contact));

        service.delete(1L);

        verify(repository).deleteById(1L);
    }
}
