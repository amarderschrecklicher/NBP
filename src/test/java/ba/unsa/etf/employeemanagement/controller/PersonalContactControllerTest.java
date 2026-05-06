package ba.unsa.etf.employeemanagement.controller;

import ba.unsa.etf.employeemanagement.dto.request.PersonalContactRequest;
import ba.unsa.etf.employeemanagement.dto.response.PersonalContactResponse;
import ba.unsa.etf.employeemanagement.service.api.IPersonalContactService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class PersonalContactControllerTest {

    @Mock
    private IPersonalContactService service;

    @InjectMocks
    private PersonalContactController controller;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void findAll_returnsList() {
        List<PersonalContactResponse> list = List.of(new PersonalContactResponse());
        when(service.findAll()).thenReturn(list);
        List<PersonalContactResponse> result = controller.findAll();
        assertEquals(list, result);
        verify(service).findAll();
    }

    @Test
    void findById_returnsResponse() {
        PersonalContactResponse response = new PersonalContactResponse();
        when(service.findById(1L)).thenReturn(response);
        PersonalContactResponse result = controller.findById(1L);
        assertEquals(response, result);
        verify(service).findById(1L);
    }

    @Test
    void create_returnsCreatedStatus() {
        PersonalContactRequest request = new PersonalContactRequest();
        PersonalContactResponse response = new PersonalContactResponse();
        when(service.save(request)).thenReturn(response);

        ResponseEntity<PersonalContactResponse> result = controller.create(request);

        assertEquals(201, result.getStatusCode().value());
        assertEquals(response, result.getBody());
        verify(service).save(request);
    }

    @Test
    void update_returnsOkStatus() {
        PersonalContactRequest request = new PersonalContactRequest();
        PersonalContactResponse response = new PersonalContactResponse();
        when(service.update(1L, request)).thenReturn(response);

        ResponseEntity<PersonalContactResponse> result = controller.update(1L, request);

        assertEquals(200, result.getStatusCode().value());
        assertEquals(response, result.getBody());
        verify(service).update(1L, request);
    }

    @Test
    void delete_returnsNoContentStatus() {
        doNothing().when(service).delete(1L);
        ResponseEntity<Void> result = controller.delete(1L);
        assertEquals(204, result.getStatusCode().value());
        verify(service).delete(1L);
    }
}
