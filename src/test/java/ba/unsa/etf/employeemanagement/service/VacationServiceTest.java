package ba.unsa.etf.employeemanagement.service;

import ba.unsa.etf.employeemanagement.dto.request.VacationRequest;
import ba.unsa.etf.employeemanagement.dto.response.VacationResponse;
import ba.unsa.etf.employeemanagement.exceptions.ResourceNotFoundException;
import ba.unsa.etf.employeemanagement.mapper.VacationMapper;
import ba.unsa.etf.employeemanagement.model.Vacation;
import ba.unsa.etf.employeemanagement.repository.VacationRepository;
import ba.unsa.etf.employeemanagement.service.impl.VacationService;
import ba.unsa.etf.employeemanagement.util.enums.VacationStatus;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class VacationServiceTest {

    @Mock
    private VacationRepository repository;

    @Mock
    private VacationMapper mapper;

    @InjectMocks
    private VacationService service;

    @Test
    void findAll_returnsMappedResponses() {
        Vacation entity = new Vacation(1L, 10L, new Date(1704067200000L), new Date(1704153600000L), "ANNUAL", "PENDING", null, "Trip");
        VacationResponse response = VacationResponse.builder().id(1L).employeeId(10L).status("PENDING").build();

        when(repository.findAll()).thenReturn(List.of(entity));
        when(mapper.mapToResponse(entity)).thenReturn(response);

        List<VacationResponse> results = service.findAll();

        assertEquals(1, results.size());
        assertEquals("PENDING", results.get(0).getStatus());
    }

    @Test
    void findById_whenMissing_throwsResourceNotFound() {
        when(repository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> service.findById(99L));
    }

    @Test
    void save_persistsAndReturnsResponse() {
        VacationRequest request = new VacationRequest(1L, new Date(1704067200000L), new Date(1704153600000L), "ANNUAL", "Trip");
        Vacation entity = new Vacation(null, 1L, request.getStartDate(), request.getEndDate(), "ANNUAL", "PENDING", null, "Trip");
        Vacation saved = new Vacation(4L, 1L, request.getStartDate(), request.getEndDate(), "ANNUAL", "PENDING", null, "Trip");
        VacationResponse response = VacationResponse.builder().id(4L).employeeId(1L).status("PENDING").build();

        when(mapper.mapToEntity(request)).thenReturn(entity);
        when(repository.save(entity)).thenReturn(4L);
        when(repository.findById(4L)).thenReturn(Optional.of(saved));
        when(mapper.mapToResponse(saved)).thenReturn(response);

        VacationResponse result = service.save(request);

        assertEquals(4L, result.getId());
    }

    @Test
    void update_whenMissing_throwsResourceNotFound() {
        VacationRequest request = new VacationRequest(1L, new Date(), new Date(), "ANNUAL", "Trip");
        when(repository.findById(22L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> service.update(22L, request));
        verify(repository, never()).update(anyLong(), any(Vacation.class));
    }

    @Test
    void requestVacation_withinLimit_setsPendingAndSaves() {
        VacationRequest request = new VacationRequest(1L, new Date(1704067200000L), new Date(1704153600000L), "ANNUAL", "Family trip");
        Vacation mapped = new Vacation(null, 1L, request.getStartDate(), request.getEndDate(), "ANNUAL", null, null, "Family trip");
        Vacation saved = new Vacation(11L, 1L, request.getStartDate(), request.getEndDate(), "ANNUAL", "PENDING", null, "Family trip");
        VacationResponse response = VacationResponse.builder().id(11L).employeeId(1L).status("PENDING").build();

        when(repository.findByEmployeeIdAndYear(eq(1L), anyInt())).thenReturn(List.of());
        when(mapper.mapToEntity(request)).thenReturn(mapped);
        when(repository.save(any(Vacation.class))).thenReturn(11L);
        when(repository.findById(11L)).thenReturn(Optional.of(saved));
        when(mapper.mapToResponse(saved)).thenReturn(response);

        VacationResponse result = service.requestVacation(request);

        ArgumentCaptor<Vacation> captor = ArgumentCaptor.forClass(Vacation.class);
        verify(repository).save(captor.capture());
        assertEquals(VacationStatus.PENDING.name(), captor.getValue().getStatus());
        assertEquals("PENDING", result.getStatus());
    }

    @Test
    void requestVacation_exceedsLimit_throws() {
        List<Vacation> vacations = new ArrayList<>();
        for (int i = 0; i < 21; i++) {
            Date day = new Date(1704067200000L + i * 86400000L);
            vacations.add(new Vacation((long) i, 1L, day, day, "ANNUAL", VacationStatus.APPROVED.name(), null, ""));
        }
        VacationRequest request = new VacationRequest(1L, new Date(1710000000000L), new Date(1710000000000L), "ANNUAL", "");

        when(repository.findByEmployeeIdAndYear(eq(1L), anyInt())).thenReturn(vacations);

        assertThrows(IllegalArgumentException.class, () -> service.requestVacation(request));
    }

    @Test
    void approveVacation_whenFound_updatesStatusAndUsesExistingReason() {
        Vacation existing = new Vacation(8L, 1L, new Date(), new Date(), "ANNUAL", "PENDING", null, "Original reason");
        Vacation approved = new Vacation(8L, 1L, new Date(), new Date(), "ANNUAL", "APPROVED", 7L, "Original reason");
        VacationResponse response = VacationResponse.builder().id(8L).status("APPROVED").approvedBy(7L).reason("Original reason").build();

        when(repository.findById(8L)).thenReturn(Optional.of(existing), Optional.of(approved));
        when(mapper.mapToResponse(approved)).thenReturn(response);

        VacationResponse result = service.approveVacation(8L, 7L);

        verify(repository).updateStatus(8L, VacationStatus.APPROVED, 7L, "Original reason");
        assertEquals("APPROVED", result.getStatus());
    }

    @Test
    void rejectVacation_updatesStatusWithProvidedReason() {
        Vacation rejected = new Vacation(9L, 1L, new Date(), new Date(), "ANNUAL", "REJECTED", 5L, "No coverage");
        VacationResponse response = VacationResponse.builder().id(9L).status("REJECTED").reason("No coverage").build();

        when(repository.findById(9L)).thenReturn(Optional.of(rejected));
        when(mapper.mapToResponse(rejected)).thenReturn(response);

        VacationResponse result = service.rejectVacation(9L, 5L, "No coverage");

        verify(repository).updateStatus(9L, VacationStatus.REJECTED, 5L, "No coverage");
        assertEquals("REJECTED", result.getStatus());
    }

    @Test
    void getVacationStatus_returnsEnumValue() {
        when(repository.findById(12L)).thenReturn(Optional.of(new Vacation(12L, 1L, new Date(), new Date(), "ANNUAL", "APPROVED", 3L, "")));

        VacationStatus status = service.getVacationStatus(12L);

        assertEquals(VacationStatus.APPROVED, status);
    }

    @Test
    void delete_whenExists_callsDeleteById() {
        when(repository.findById(13L)).thenReturn(Optional.of(new Vacation(13L, 1L, new Date(), new Date(), "ANNUAL", "PENDING", null, "")));

        service.delete(13L);

        verify(repository).deleteById(13L);
    }
}

