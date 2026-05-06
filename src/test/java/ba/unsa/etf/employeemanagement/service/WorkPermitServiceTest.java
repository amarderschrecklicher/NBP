package ba.unsa.etf.employeemanagement.service;

import ba.unsa.etf.employeemanagement.dto.request.WorkPermitRequest;
import ba.unsa.etf.employeemanagement.dto.response.WorkPermitResponse;
import ba.unsa.etf.employeemanagement.exceptions.ResourceNotFoundException;
import ba.unsa.etf.employeemanagement.mapper.WorkPermitMapper;
import ba.unsa.etf.employeemanagement.model.WorkPermit;
import ba.unsa.etf.employeemanagement.repository.WorkPermitRepository;
import ba.unsa.etf.employeemanagement.service.impl.WorkPermitService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class WorkPermitServiceTest {

    @Mock
    private WorkPermitRepository repository;

    @Mock
    private WorkPermitMapper mapper;

    @InjectMocks
    private WorkPermitService service;

    @Test
    void findAll_returnsMappedResponses() {
        WorkPermit entity = new WorkPermit(1L, 1L, "P123", "OPEN", "USA", new Date(), new Date(), "ACTIVE");
        WorkPermitResponse response = WorkPermitResponse.builder().id(1L).permitNumber("P123").build();

        when(repository.findAll()).thenReturn(List.of(entity));
        when(mapper.mapToResponse(entity)).thenReturn(response);

        List<WorkPermitResponse> results = service.findAll();

        assertEquals(1, results.size());
        assertEquals("P123", results.get(0).getPermitNumber());
    }

    @Test
    void findById_whenMissing_throwsResourceNotFound() {
        when(repository.findById(99L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> service.findById(99L));
    }

    @Test
    void save_persistsAndReturnsResponse() {
        Date issueDate = new Date(System.currentTimeMillis() - 1000000);
        Date expiryDate = new Date(System.currentTimeMillis() + 1000000);
        WorkPermitRequest request = new WorkPermitRequest(1L, "P123", "OPEN", "USA", issueDate, expiryDate, "ACTIVE");
        WorkPermit mapped = new WorkPermit(null, 1L, "P123", "OPEN", "USA", issueDate, expiryDate, "ACTIVE");
        WorkPermit saved = new WorkPermit(1L, 1L, "P123", "OPEN", "USA", issueDate, expiryDate, "ACTIVE");
        WorkPermitResponse response = WorkPermitResponse.builder().id(1L).permitNumber("P123").build();

        when(mapper.mapToEntity(request)).thenReturn(mapped);
        when(repository.save(mapped)).thenReturn(1L);
        when(repository.findById(1L)).thenReturn(Optional.of(saved));
        when(mapper.mapToResponse(saved)).thenReturn(response);

        WorkPermitResponse result = service.save(request);

        assertEquals(1L, result.getId());
        verify(repository).save(mapped);
    }

    @Test
    void update_whenExists_updatesById() {
        Date issueDate = new Date(System.currentTimeMillis() - 1000000);
        Date expiryDate = new Date(System.currentTimeMillis() + 1000000);
        WorkPermitRequest request = new WorkPermitRequest(1L, "P456", "OPEN", "UK", issueDate, expiryDate, "ACTIVE");
        WorkPermit existing = new WorkPermit(1L, 1L, "P123", "OPEN", "USA", issueDate, expiryDate, "ACTIVE");
        WorkPermit mapped = new WorkPermit(null, 1L, "P456", "OPEN", "UK", issueDate, expiryDate, "ACTIVE");
        WorkPermit updated = new WorkPermit(1L, 1L, "P456", "OPEN", "UK", issueDate, expiryDate, "ACTIVE");
        WorkPermitResponse response = WorkPermitResponse.builder().id(1L).permitNumber("P456").build();

        when(repository.findById(1L)).thenReturn(Optional.of(existing), Optional.of(updated));
        when(mapper.mapToEntity(request)).thenReturn(mapped);
        when(mapper.mapToResponse(updated)).thenReturn(response);

        WorkPermitResponse result = service.update(1L, request);

        ArgumentCaptor<WorkPermit> captor = ArgumentCaptor.forClass(WorkPermit.class);
        verify(repository).update(eq(1L), captor.capture());
        assertEquals("P456", captor.getValue().getPermitNumber());
        assertEquals("P456", result.getPermitNumber());
    }

    @Test
    void delete_whenMissing_throwsResourceNotFound() {
        when(repository.findById(7L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> service.delete(7L));
    }
}
