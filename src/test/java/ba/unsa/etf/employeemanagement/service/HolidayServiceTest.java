package ba.unsa.etf.employeemanagement.service;

import ba.unsa.etf.employeemanagement.dto.request.HolidayRequest;
import ba.unsa.etf.employeemanagement.dto.response.HolidayResponse;
import ba.unsa.etf.employeemanagement.exceptions.ResourceNotFoundException;
import ba.unsa.etf.employeemanagement.mapper.HolidayMapper;
import ba.unsa.etf.employeemanagement.model.Holiday;
import ba.unsa.etf.employeemanagement.repository.HolidayRepository;
import ba.unsa.etf.employeemanagement.service.impl.HolidayService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class HolidayServiceTest {

    @Mock
    private HolidayRepository repository;

    @Mock
    private HolidayMapper mapper;

    @InjectMocks
    private HolidayService service;

    @Test
    void findById_whenExists_returnsMappedResponse() {
        Holiday entity = new Holiday(1L, "New Year", new Date(1704067200000L), "BA", "Public holiday", null);
        HolidayResponse response = HolidayResponse.builder().id(1L).holidayName("New Year").country("BA").description("Public holiday").build();

        when(repository.findById(1L)).thenReturn(Optional.of(entity));
        when(mapper.mapToResponse(entity)).thenReturn(response);

        HolidayResponse result = service.findById(1L);

        assertEquals("New Year", result.getHolidayName());
        verify(repository).findById(1L);
    }

    @Test
    void findAll_returnsMappedResponses() {
        Holiday entity = new Holiday(2L, "Labor Day", new Date(1714521600000L), "BA", "Public holiday", null);
        HolidayResponse response = HolidayResponse.builder().id(2L).holidayName("Labor Day").country("BA").build();

        when(repository.findAll()).thenReturn(List.of(entity));
        when(mapper.mapToResponse(entity)).thenReturn(response);

        List<HolidayResponse> results = service.findAll();

        assertEquals(1, results.size());
        assertEquals("Labor Day", results.get(0).getHolidayName());
        verify(repository).findAll();
    }

    @Test
    void update_whenMissing_throwsResourceNotFound() {
        HolidayRequest request = new HolidayRequest("Statehood Day", new Date(), "BA", "", null);
        when(repository.findById(10L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> service.update(10L, request));
        verify(repository, never()).update(anyLong(), any(Holiday.class));
    }

    @Test
    void delete_whenExists_deletesById() {
        when(repository.findById(4L)).thenReturn(Optional.of(new Holiday(4L, "Any", new Date(), "BA", "", null)));

        service.delete(4L);

        verify(repository).deleteById(4L);
    }

    @Test
    void save_persistsAndReturnsResponse() {
        Date holidayDate = new Date(1735689600000L);
        HolidayRequest request = new HolidayRequest("New Year", holidayDate, "BA", "Public holiday", null);
        Holiday mapped = new Holiday(null, "New Year", holidayDate, "BA", "Public holiday", null);
        Holiday saved = new Holiday(6L, "New Year", holidayDate, "BA", "Public holiday", null);
        HolidayResponse response = HolidayResponse.builder().id(6L).holidayName("New Year").holidayDate(holidayDate).country("BA").build();

        when(mapper.mapToEntity(request)).thenReturn(mapped);
        when(repository.save(mapped)).thenReturn(6L);
        when(repository.findById(6L)).thenReturn(Optional.of(saved));
        when(mapper.mapToResponse(saved)).thenReturn(response);

        HolidayResponse result = service.save(request);

        assertEquals(6L, result.getId());
        verify(repository).save(mapped);
    }
}

