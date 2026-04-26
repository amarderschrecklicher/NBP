package ba.unsa.etf.employeemanagement.service;

import ba.unsa.etf.employeemanagement.dto.request.VacationRequest;
import ba.unsa.etf.employeemanagement.dto.response.VacationResponse;
import ba.unsa.etf.employeemanagement.model.Vacation;
import ba.unsa.etf.employeemanagement.repository.VacationRepository;
import ba.unsa.etf.employeemanagement.mapper.VacationMapper;
import ba.unsa.etf.employeemanagement.service.impl.VacationService;
import ba.unsa.etf.employeemanagement.util.enums.VacationStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class VacationServiceTest {
    @Mock
    private VacationRepository vacationRepository;
    @Mock
    private VacationMapper vacationMapper;
    @InjectMocks
    private VacationService vacationService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void requestVacation_withinLimit_succeeds() {
        VacationRequest req = new VacationRequest(1L, new Date(100000), new Date(200000), "ANNUAL", "Family trip");
        when(vacationRepository.findByEmployeeIdAndYear(eq(1L), anyInt())).thenReturn(Collections.emptyList());
        Vacation entity = new Vacation(null, 1L, req.getStartDate(), req.getEndDate(), req.getVacationType(), null, null, req.getReason());
        when(vacationMapper.mapToEntity(req)).thenReturn(entity);
        Vacation saved = new Vacation(1L, 1L, req.getStartDate(), req.getEndDate(), req.getVacationType(), "PENDING", null, req.getReason());
        when(vacationRepository.save(any())).thenReturn(1L);
        when(vacationRepository.findById(1L)).thenReturn(Optional.of(saved));
        when(vacationMapper.mapToResponse(saved)).thenReturn(VacationResponse.builder().id(1L).employeeId(1L).startDate(req.getStartDate()).endDate(req.getEndDate()).vacationType("ANNUAL").status("PENDING").reason("Family trip").build());
        VacationResponse resp = vacationService.requestVacation(req);
        assertEquals("PENDING", resp.getStatus());
        assertEquals(1L, resp.getEmployeeId());
        assertEquals(req.getStartDate(), resp.getStartDate());
        assertEquals(req.getEndDate(), resp.getEndDate());
    }

    @Test
    void requestVacation_exceedsLimit_throws() {
        List<Vacation> vacations = new ArrayList<>();
        for (int i = 0; i < 21; i++) {
            Date d = new Date(100000 + i * 86400000L);
            vacations.add(new Vacation((long) i, 1L, d, d, "ANNUAL", "APPROVED", null, ""));
        }
        VacationRequest req = new VacationRequest(1L, new Date(2000000), new Date(2000000), "ANNUAL", "");
        when(vacationRepository.findByEmployeeIdAndYear(eq(1L), anyInt())).thenReturn(vacations);
        assertThrows(IllegalArgumentException.class, () -> vacationService.requestVacation(req));
    }

    @Test
    void requestVacation_setsStatusToPending() {
        VacationRequest req = new VacationRequest(1L, new Date(100000), new Date(200000), "ANNUAL", "");
        when(vacationRepository.findByEmployeeIdAndYear(eq(1L), anyInt())).thenReturn(Collections.emptyList());
        Vacation entity = new Vacation(null, 1L, req.getStartDate(), req.getEndDate(), req.getVacationType(), null, null, req.getReason());
        when(vacationMapper.mapToEntity(req)).thenReturn(entity);
        Vacation saved = new Vacation(1L, 1L, req.getStartDate(), req.getEndDate(), req.getVacationType(), "PENDING", null, req.getReason());
        when(vacationRepository.save(any())).thenReturn(1L);
        when(vacationRepository.findById(1L)).thenReturn(Optional.of(saved));
        when(vacationMapper.mapToResponse(saved)).thenReturn(VacationResponse.builder().id(1L).employeeId(1L).startDate(req.getStartDate()).endDate(req.getEndDate()).vacationType("ANNUAL").status("PENDING").reason("").build());
        VacationResponse resp = vacationService.requestVacation(req);
        assertEquals("PENDING", resp.getStatus());
    }
}
