package ba.unsa.etf.employeemanagement.controller;

import ba.unsa.etf.employeemanagement.dto.request.WorkPermitRequest;
import ba.unsa.etf.employeemanagement.dto.response.WorkPermitResponse;
import ba.unsa.etf.employeemanagement.service.api.IWorkPermitService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class WorkPermitControllerTest {

    @Mock
    private IWorkPermitService workPermitService;

    @InjectMocks
    private WorkPermitController workPermitController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void findAll_returnsList() {
        List<WorkPermitResponse> expected = List.of(new WorkPermitResponse());
        when(workPermitService.findAll()).thenReturn(expected);
        List<WorkPermitResponse> result = workPermitController.findAll();
        assertEquals(expected, result);
        verify(workPermitService).findAll();
    }

    @Test
    void findById_returnsResponse() {
        WorkPermitResponse expected = new WorkPermitResponse();
        when(workPermitService.findById(1L)).thenReturn(expected);
        WorkPermitResponse result = workPermitController.findById(1L);
        assertEquals(expected, result);
        verify(workPermitService).findById(1L);
    }

    @Test
    void create_returnsCreated() {
        WorkPermitRequest request = new WorkPermitRequest();
        WorkPermitResponse expected = new WorkPermitResponse();
        when(workPermitService.save(request)).thenReturn(expected);
        ResponseEntity<WorkPermitResponse> response = workPermitController.create(request);
        assertEquals(201, response.getStatusCode().value());
        assertEquals(expected, response.getBody());
        verify(workPermitService).save(request);
    }

    @Test
    void update_returnsOk() {
        WorkPermitRequest request = new WorkPermitRequest();
        WorkPermitResponse expected = new WorkPermitResponse();
        when(workPermitService.update(1L, request)).thenReturn(expected);
        ResponseEntity<WorkPermitResponse> response = workPermitController.update(1L, request);
        assertEquals(200, response.getStatusCode().value());
        assertEquals(expected, response.getBody());
        verify(workPermitService).update(1L, request);
    }

    @Test
    void delete_returnsNoContent() {
        doNothing().when(workPermitService).delete(1L);
        ResponseEntity<Void> response = workPermitController.delete(1L);
        assertEquals(204, response.getStatusCode().value());
        verify(workPermitService).delete(1L);
    }
}
