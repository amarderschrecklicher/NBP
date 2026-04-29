package ba.unsa.etf.employeemanagement.controller;

import ba.unsa.etf.employeemanagement.dto.request.EmergencyContactRequest;
import ba.unsa.etf.employeemanagement.dto.response.EmergencyContactResponse;
import ba.unsa.etf.employeemanagement.service.api.IEmergencyContactService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class EmergencyContactControllerTest {

    @Mock
    private IEmergencyContactService service;

    @InjectMocks
    private EmergencyContactController controller;

    @Test
    void findAll_ReturnsList() {
        EmergencyContactResponse response = new EmergencyContactResponse();
        when(service.findAll()).thenReturn(List.of(response));

        List<EmergencyContactResponse> result = controller.findAll();

        assertEquals(1, result.size());
        verify(service).findAll();
    }

    @Test
    void findById_ReturnsResponse() {
        EmergencyContactResponse response = new EmergencyContactResponse();
        when(service.findById(1L)).thenReturn(response);

        EmergencyContactResponse result = controller.findById(1L);

        assertEquals(response, result);
        verify(service).findById(1L);
    }

    @Test
    void create_ReturnsCreated() {
        EmergencyContactRequest request = new EmergencyContactRequest();
        EmergencyContactResponse response = new EmergencyContactResponse();
        when(service.save(request)).thenReturn(response);

        ResponseEntity<EmergencyContactResponse> result = controller.create(request);

        assertEquals(HttpStatus.CREATED, result.getStatusCode());
        assertEquals(response, result.getBody());
        verify(service).save(request);
    }

    @Test
    void update_ReturnsOk() {
        EmergencyContactRequest request = new EmergencyContactRequest();
        EmergencyContactResponse response = new EmergencyContactResponse();
        when(service.update(1L, request)).thenReturn(response);

        ResponseEntity<EmergencyContactResponse> result = controller.update(1L, request);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(response, result.getBody());
        verify(service).update(1L, request);
    }

    @Test
    void delete_ReturnsNoContent() {
        ResponseEntity<Void> result = controller.delete(1L);

        assertEquals(HttpStatus.NO_CONTENT, result.getStatusCode());
        verify(service).delete(1L);
    }
}
