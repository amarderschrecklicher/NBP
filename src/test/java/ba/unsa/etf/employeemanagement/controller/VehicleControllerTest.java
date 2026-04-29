package ba.unsa.etf.employeemanagement.controller;

import ba.unsa.etf.employeemanagement.dto.request.VehicleRequest;
import ba.unsa.etf.employeemanagement.dto.response.VehicleResponse;
import ba.unsa.etf.employeemanagement.service.api.IVehicleService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class VehicleControllerTest {

    @Mock
    private IVehicleService vehicleService;

    @InjectMocks
    private VehicleController vehicleController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void findAll_returnsList() {
        List<VehicleResponse> expected = List.of(new VehicleResponse());
        when(vehicleService.findAll()).thenReturn(expected);
        List<VehicleResponse> result = vehicleController.findAll();
        assertEquals(expected, result);
        verify(vehicleService).findAll();
    }

    @Test
    void findById_returnsResponse() {
        VehicleResponse expected = new VehicleResponse();
        when(vehicleService.findById(1L)).thenReturn(expected);
        VehicleResponse result = vehicleController.findById(1L);
        assertEquals(expected, result);
        verify(vehicleService).findById(1L);
    }

    @Test
    void create_returnsCreated() {
        VehicleRequest request = new VehicleRequest();
        VehicleResponse expected = new VehicleResponse();
        when(vehicleService.save(request)).thenReturn(expected);
        ResponseEntity<VehicleResponse> response = vehicleController.create(request);
        assertEquals(201, response.getStatusCode().value());
        assertEquals(expected, response.getBody());
        verify(vehicleService).save(request);
    }

    @Test
    void update_returnsOk() {
        VehicleRequest request = new VehicleRequest();
        VehicleResponse expected = new VehicleResponse();
        when(vehicleService.update(1L, request)).thenReturn(expected);
        ResponseEntity<VehicleResponse> response = vehicleController.update(1L, request);
        assertEquals(200, response.getStatusCode().value());
        assertEquals(expected, response.getBody());
        verify(vehicleService).update(1L, request);
    }

    @Test
    void delete_returnsNoContent() {
        doNothing().when(vehicleService).delete(1L);
        ResponseEntity<Void> response = vehicleController.delete(1L);
        assertEquals(204, response.getStatusCode().value());
        verify(vehicleService).delete(1L);
    }
}
