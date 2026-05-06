package ba.unsa.etf.employeemanagement.service;

import ba.unsa.etf.employeemanagement.dto.request.DisabilityRequest;
import ba.unsa.etf.employeemanagement.dto.response.DisabilityResponse;
import ba.unsa.etf.employeemanagement.exceptions.ResourceNotFoundException;
import ba.unsa.etf.employeemanagement.mapper.DisabilityMapper;
import ba.unsa.etf.employeemanagement.model.Disability;
import ba.unsa.etf.employeemanagement.repository.DisabilityRepository;
import ba.unsa.etf.employeemanagement.service.impl.DisabilityService;
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
class DisabilityServiceTest {

    @Mock
    private DisabilityRepository repository;

    @Mock
    private DisabilityMapper mapper;

    @InjectMocks
    private DisabilityService service;

    @Test
    void findAll_returnsMappedResponses() {
        Disability entity = new Disability(1L, 1L, "PHYSICAL", "MILD", "Desc", new Date());
        DisabilityResponse response = DisabilityResponse.builder().id(1L).disabilityType("PHYSICAL").build();

        when(repository.findAll()).thenReturn(List.of(entity));
        when(mapper.mapToResponse(entity)).thenReturn(response);

        List<DisabilityResponse> results = service.findAll();

        assertEquals(1, results.size());
        assertEquals("PHYSICAL", results.get(0).getDisabilityType());
    }

    @Test
    void findById_whenMissing_throwsResourceNotFound() {
        when(repository.findById(99L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> service.findById(99L));
    }

    @Test
    void save_persistsAndReturnsResponse() {
        Date date = new Date();
        DisabilityRequest request = new DisabilityRequest(1L, "PHYSICAL", "MILD", "Desc", date);
        Disability mapped = new Disability(null, 1L, "PHYSICAL", "MILD", "Desc", date);
        Disability saved = new Disability(1L, 1L, "PHYSICAL", "MILD", "Desc", date);
        DisabilityResponse response = DisabilityResponse.builder().id(1L).disabilityType("PHYSICAL").build();

        when(mapper.mapToEntity(request)).thenReturn(mapped);
        when(repository.save(mapped)).thenReturn(1L);
        when(repository.findById(1L)).thenReturn(Optional.of(saved));
        when(mapper.mapToResponse(saved)).thenReturn(response);

        DisabilityResponse result = service.save(request);

        assertEquals(1L, result.getId());
        verify(repository).save(mapped);
    }

    @Test
    void update_whenExists_updatesById() {
        Date date = new Date();
        DisabilityRequest request = new DisabilityRequest(1L, "SENSORY", "SEVERE", "New Desc", date);
        Disability existing = new Disability(1L, 1L, "PHYSICAL", "MILD", "Desc", date);
        Disability mapped = new Disability(null, 1L, "SENSORY", "SEVERE", "New Desc", date);
        Disability updated = new Disability(1L, 1L, "SENSORY", "SEVERE", "New Desc", date);
        DisabilityResponse response = DisabilityResponse.builder().id(1L).disabilityType("SENSORY").build();

        when(repository.findById(1L)).thenReturn(Optional.of(existing), Optional.of(updated));
        when(mapper.mapToEntity(request)).thenReturn(mapped);
        when(mapper.mapToResponse(updated)).thenReturn(response);

        DisabilityResponse result = service.update(1L, request);

        ArgumentCaptor<Disability> captor = ArgumentCaptor.forClass(Disability.class);
        verify(repository).update(eq(1L), captor.capture());
        assertEquals("SENSORY", captor.getValue().getDisabilityType());
        assertEquals("SENSORY", result.getDisabilityType());
    }

    @Test
    void delete_whenMissing_throwsResourceNotFound() {
        when(repository.findById(7L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> service.delete(7L));
    }
}
