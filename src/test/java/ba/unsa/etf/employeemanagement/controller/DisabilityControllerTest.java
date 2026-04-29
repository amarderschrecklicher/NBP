package ba.unsa.etf.employeemanagement.controller;

import ba.unsa.etf.employeemanagement.dto.request.DisabilityRequest;
import ba.unsa.etf.employeemanagement.dto.response.DisabilityResponse;
import ba.unsa.etf.employeemanagement.service.api.IDisabilityService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class DisabilityControllerTest {

    @Mock
    private IDisabilityService disabilityService;

    @InjectMocks
    private DisabilityController disabilityController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void findAll_returnsList() {
        List<DisabilityResponse> expected = List.of(new DisabilityResponse());
        when(disabilityService.findAll()).thenReturn(expected);
        List<DisabilityResponse> result = disabilityController.findAll();
        assertEquals(expected, result);
        verify(disabilityService).findAll();
    }

    @Test
    void findById_returnsResponse() {
        DisabilityResponse expected = new DisabilityResponse();
        when(disabilityService.findById(1L)).thenReturn(expected);
        DisabilityResponse result = disabilityController.findById(1L);
        assertEquals(expected, result);
        verify(disabilityService).findById(1L);
    }

    @Test
    void create_returnsCreated() {
        DisabilityRequest request = new DisabilityRequest();
        DisabilityResponse expected = new DisabilityResponse();
        when(disabilityService.save(request)).thenReturn(expected);
        ResponseEntity<DisabilityResponse> response = disabilityController.create(request);
        assertEquals(201, response.getStatusCode().value());
        assertEquals(expected, response.getBody());
        verify(disabilityService).save(request);
    }

    @Test
    void update_returnsOk() {
        DisabilityRequest request = new DisabilityRequest();
        DisabilityResponse expected = new DisabilityResponse();
        when(disabilityService.update(1L, request)).thenReturn(expected);
        ResponseEntity<DisabilityResponse> response = disabilityController.update(1L, request);
        assertEquals(200, response.getStatusCode().value());
        assertEquals(expected, response.getBody());
        verify(disabilityService).update(1L, request);
    }

    @Test
    void delete_returnsNoContent() {
        doNothing().when(disabilityService).delete(1L);
        ResponseEntity<Void> response = disabilityController.delete(1L);
        assertEquals(204, response.getStatusCode().value());
        verify(disabilityService).delete(1L);
    }
}
