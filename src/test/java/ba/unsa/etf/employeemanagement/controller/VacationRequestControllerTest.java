package ba.unsa.etf.employeemanagement.controller;

import ba.unsa.etf.employeemanagement.dto.request.VacationRequest;
import ba.unsa.etf.employeemanagement.dto.response.VacationResponse;
import ba.unsa.etf.employeemanagement.service.api.IVacationService;
import ba.unsa.etf.employeemanagement.util.enums.VacationStatus;
import ba.unsa.etf.employeemanagement.util.validation.VacationValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;

import java.util.Date;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class VacationRequestControllerTest {
    @Mock
    private IVacationService vacationService;
    @Mock
    private BindingResult bindingResult;
    @Mock
    private VacationValidator vacationValidator;
    @InjectMocks
    private VacationController vacationController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        doNothing().when(vacationValidator).validate(any(), any());
    }

    @Test
    void requestVacation_returnsCreated() {
        VacationRequest req = new VacationRequest(1L, new Date(100000), new Date(200000), "ANNUAL", "Family trip");
        VacationResponse resp = VacationResponse.builder().id(1L).employeeId(1L).startDate(req.getStartDate()).endDate(req.getEndDate()).vacationType("ANNUAL").status("PENDING").reason("Family trip").build();
        when(bindingResult.hasErrors()).thenReturn(false);
        when(vacationService.requestVacation(req)).thenReturn(resp);
        ResponseEntity<?> response = vacationController.requestVacation(req, bindingResult);
        assertEquals(201, response.getStatusCode().value());
        assertEquals(resp, response.getBody());
        verify(vacationService).requestVacation(req);
    }

    @Test
    void approveVacation_returnsOk() {
        VacationResponse resp = VacationResponse.builder().id(1L).employeeId(1L).status("APPROVED").build();
        when(vacationService.approveVacation(1L, 10L)).thenReturn(resp);
        ResponseEntity<?> response = vacationController.approveVacation(1L, 10L);
        assertEquals(200, response.getStatusCode().value());
        assertEquals(resp, response.getBody());
        verify(vacationService).approveVacation(1L, 10L);
    }

    @Test
    void rejectVacation_returnsOk() {
        VacationResponse resp = VacationResponse.builder().id(1L).employeeId(1L).status("REJECTED").reason("Not enough days").build();
        when(vacationService.rejectVacation(1L, 10L, "Not enough days")).thenReturn(resp);
        ResponseEntity<?> response = vacationController.rejectVacation(1L, 10L, "Not enough days");
        assertEquals(200, response.getStatusCode().value());
        assertEquals(resp, response.getBody());
        verify(vacationService).rejectVacation(1L, 10L, "Not enough days");
    }

    @Test
    void getVacationStatus_returnsStatus() {
        when(vacationService.getVacationStatus(1L)).thenReturn(VacationStatus.PENDING);
        ResponseEntity<?> response = vacationController.getVacationStatus(1L);
        assertEquals(200, response.getStatusCode().value());
        assertTrue(((Map<?, ?>) response.getBody()).containsKey("status"));
        assertEquals(VacationStatus.PENDING, ((Map<?, ?>) response.getBody()).get("status"));
        verify(vacationService).getVacationStatus(1L);
    }
}
