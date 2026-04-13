package ba.unsa.etf.employeemanagement.controller;

import ba.unsa.etf.employeemanagement.model.*;
import ba.unsa.etf.employeemanagement.service.VacationRequestService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class VacationRequestControllerTest {
    @Mock
    private VacationRequestService vacationRequestService;

    @InjectMocks
    private VacationRequestController vacationRequestController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void requestVacation_returnsVacationRequest() {
        VacationRequest req = new VacationRequest(1L, 2L, new Date(100000), new Date(200000), VacationStatus.PENDING);
        when(vacationRequestService.requestVacation(2L, req.getStartDate(), req.getEndDate())).thenReturn(req);
        ResponseEntity<VacationRequest> response = vacationRequestController.requestVacation(2L, req.getStartDate(), req.getEndDate());
        assertEquals(req, response.getBody());
        verify(vacationRequestService).requestVacation(2L, req.getStartDate(), req.getEndDate());
    }

    @Test
    void getUserRequests_returnsList() {
        List<VacationRequest> list = Arrays.asList(
                new VacationRequest(1L, 2L, new Date(100000), new Date(200000), VacationStatus.PENDING),
                new VacationRequest(2L, 2L, new Date(300000), new Date(400000), VacationStatus.APPROVED)
        );
        when(vacationRequestService.getRequestsForUser(2L)).thenReturn(list);
        ResponseEntity<List<VacationRequest>> response = vacationRequestController.getUserRequests(2L);
        assertEquals(list, response.getBody());
        verify(vacationRequestService).getRequestsForUser(2L);
    }

    @Test
    void updateStatus_returnsUpdatedRequest() {
        VacationRequest updated = new VacationRequest(1L, 2L, new Date(100000), new Date(200000), VacationStatus.APPROVED);
        when(vacationRequestService.updateRequestStatus(1L, VacationStatus.APPROVED)).thenReturn(updated);
        ResponseEntity<VacationRequest> response = vacationRequestController.updateStatus(1L, VacationStatus.APPROVED);
        assertEquals(updated, response.getBody());
        verify(vacationRequestService).updateRequestStatus(1L, VacationStatus.APPROVED);
    }
}

