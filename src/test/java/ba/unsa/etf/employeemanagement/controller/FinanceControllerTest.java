package ba.unsa.etf.employeemanagement.controller;

import ba.unsa.etf.employeemanagement.dto.request.FinanceRequest;
import ba.unsa.etf.employeemanagement.dto.response.FinanceResponse;
import ba.unsa.etf.employeemanagement.service.api.IFinanceService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class FinanceControllerTest {

    @Mock
    private IFinanceService financeService;

    @InjectMocks
    private FinanceController financeController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void findAll_returnsList() {
        List<FinanceResponse> expected = List.of(new FinanceResponse());
        when(financeService.findAll()).thenReturn(expected);
        List<FinanceResponse> result = financeController.findAll();
        assertEquals(expected, result);
        verify(financeService).findAll();
    }

    @Test
    void findById_returnsResponse() {
        FinanceResponse expected = new FinanceResponse();
        when(financeService.findById(1L)).thenReturn(expected);
        FinanceResponse result = financeController.findById(1L);
        assertEquals(expected, result);
        verify(financeService).findById(1L);
    }

    @Test
    void create_returnsCreated() {
        FinanceRequest request = new FinanceRequest();
        FinanceResponse expected = new FinanceResponse();
        when(financeService.save(request)).thenReturn(expected);
        ResponseEntity<FinanceResponse> response = financeController.create(request);
        assertEquals(201, response.getStatusCode().value());
        assertEquals(expected, response.getBody());
        verify(financeService).save(request);
    }

    @Test
    void update_returnsOk() {
        FinanceRequest request = new FinanceRequest();
        FinanceResponse expected = new FinanceResponse();
        when(financeService.update(1L, request)).thenReturn(expected);
        ResponseEntity<FinanceResponse> response = financeController.update(1L, request);
        assertEquals(200, response.getStatusCode().value());
        assertEquals(expected, response.getBody());
        verify(financeService).update(1L, request);
    }

    @Test
    void delete_returnsNoContent() {
        doNothing().when(financeService).delete(1L);
        ResponseEntity<Void> response = financeController.delete(1L);
        assertEquals(204, response.getStatusCode().value());
        verify(financeService).delete(1L);
    }
}
