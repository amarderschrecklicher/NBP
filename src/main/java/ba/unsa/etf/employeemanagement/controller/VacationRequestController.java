package ba.unsa.etf.employeemanagement.controller;

import ba.unsa.etf.employeemanagement.model.VacationRequest;
import ba.unsa.etf.employeemanagement.model.VacationStatus;
import ba.unsa.etf.employeemanagement.service.VacationRequestService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api/vacation-requests")
public class VacationRequestController {
    private final VacationRequestService vacationRequestService;

    public VacationRequestController(VacationRequestService vacationRequestService) {
        this.vacationRequestService = vacationRequestService;
    }

    @PostMapping
    public ResponseEntity<VacationRequest> requestVacation(@RequestParam Long userId,
                                                          @RequestParam Date startDate,
                                                          @RequestParam Date endDate) {
        VacationRequest request = vacationRequestService.requestVacation(userId, startDate, endDate);
        return ResponseEntity.ok(request);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<VacationRequest>> getUserRequests(@PathVariable Long userId) {
        List<VacationRequest> requests = vacationRequestService.getRequestsForUser(userId);
        return ResponseEntity.ok(requests);
    }

    @PostMapping("/{requestId}/status")
    public ResponseEntity<VacationRequest> updateStatus(@PathVariable Long requestId,
                                                       @RequestParam VacationStatus status) {
        VacationRequest updated = vacationRequestService.updateRequestStatus(requestId, status);
        return ResponseEntity.ok(updated);
    }
}

