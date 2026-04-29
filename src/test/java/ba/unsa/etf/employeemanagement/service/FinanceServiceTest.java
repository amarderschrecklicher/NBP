package ba.unsa.etf.employeemanagement.service;

import ba.unsa.etf.employeemanagement.dto.request.FinanceRequest;
import ba.unsa.etf.employeemanagement.dto.response.FinanceResponse;
import ba.unsa.etf.employeemanagement.exceptions.ResourceNotFoundException;
import ba.unsa.etf.employeemanagement.mapper.FinanceMapper;
import ba.unsa.etf.employeemanagement.model.Finance;
import ba.unsa.etf.employeemanagement.repository.FinanceRepository;
import ba.unsa.etf.employeemanagement.service.impl.FinanceService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FinanceServiceTest {

    @Mock
    private FinanceRepository repository;

    @Mock
    private FinanceMapper mapper;

    @InjectMocks
    private FinanceService service;

    @Test
    void findAll_returnsMappedResponses() {
        Finance entity = new Finance(1L, 1L, "Bank", "123", "IBAN", "TAX", 1000.0, "BAM", "MONTHLY", 1);
        FinanceResponse response = FinanceResponse.builder().id(1L).employeeId(1L).bankName("Bank").build();

        when(repository.findAll()).thenReturn(List.of(entity));
        when(mapper.mapToResponse(entity)).thenReturn(response);

        List<FinanceResponse> results = service.findAll();

        assertEquals(1, results.size());
        assertEquals("Bank", results.get(0).getBankName());
        verify(repository).findAll();
    }

    @Test
    void findById_whenMissing_throwsResourceNotFound() {
        when(repository.findById(99L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> service.findById(99L));
    }

    @Test
    void save_persistsAndReturnsResponse() {
        FinanceRequest request = new FinanceRequest(1L, "Bank", "123", "BA123", "TAX123", 1000.0, "BAM", "MONTHLY", 1);
        Finance mapped = new Finance(null, 1L, "Bank", "123", "BA123", "TAX123", 1000.0, "BAM", "MONTHLY", 1);
        Finance saved = new Finance(1L, 1L, "Bank", "123", "BA123", "TAX123", 1000.0, "BAM", "MONTHLY", 1);
        FinanceResponse response = FinanceResponse.builder().id(1L).bankName("Bank").build();

        when(mapper.mapToEntity(request)).thenReturn(mapped);
        when(repository.save(mapped)).thenReturn(1L);
        when(repository.findById(1L)).thenReturn(Optional.of(saved));
        when(mapper.mapToResponse(saved)).thenReturn(response);

        FinanceResponse result = service.save(request);

        assertEquals(1L, result.getId());
        verify(repository).save(mapped);
    }

    @Test
    void update_whenExists_updatesById() {
        FinanceRequest request = new FinanceRequest(1L, "New Bank", "123", "BA123", "TAX123", 1000.0, "BAM", "MONTHLY", 1);
        Finance existing = new Finance(1L, 1L, "Old Bank", "123", "BA123", "TAX123", 1000.0, "BAM", "MONTHLY", 1);
        Finance mapped = new Finance(null, 1L, "New Bank", "123", "BA123", "TAX123", 1000.0, "BAM", "MONTHLY", 1);
        Finance updated = new Finance(1L, 1L, "New Bank", "123", "BA123", "TAX123", 1000.0, "BAM", "MONTHLY", 1);
        FinanceResponse response = FinanceResponse.builder().id(1L).bankName("New Bank").build();

        when(repository.findById(1L)).thenReturn(Optional.of(existing), Optional.of(updated));
        when(mapper.mapToEntity(request)).thenReturn(mapped);
        when(mapper.mapToResponse(updated)).thenReturn(response);

        FinanceResponse result = service.update(1L, request);

        ArgumentCaptor<Finance> captor = ArgumentCaptor.forClass(Finance.class);
        verify(repository).update(eq(1L), captor.capture());
        assertEquals("New Bank", captor.getValue().getBankName());
        assertEquals("New Bank", result.getBankName());
    }

    @Test
    void delete_whenMissing_throwsResourceNotFound() {
        when(repository.findById(7L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> service.delete(7L));
    }
}
