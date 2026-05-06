package ba.unsa.etf.employeemanagement.service;

import ba.unsa.etf.employeemanagement.dto.request.VehicleRequest;
import ba.unsa.etf.employeemanagement.dto.response.VehicleResponse;
import ba.unsa.etf.employeemanagement.exceptions.ResourceNotFoundException;
import ba.unsa.etf.employeemanagement.mapper.VehicleMapper;
import ba.unsa.etf.employeemanagement.model.Vehicle;
import ba.unsa.etf.employeemanagement.repository.VehicleRepository;
import ba.unsa.etf.employeemanagement.service.impl.VehicleService;
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
class VehicleServiceTest {

    @Mock
    private VehicleRepository repository;

    @Mock
    private VehicleMapper mapper;

    @InjectMocks
    private VehicleService service;

    @Test
    void findAll_returnsMappedResponses() {
        Vehicle entity = new Vehicle(1L, 1L, "Toyota", "Camry", "ABC-123", "VIN123", "PETROL", "SEDAN", new Date(), null);
        VehicleResponse response = VehicleResponse.builder().id(1L).vehicleMake("Toyota").build();

        when(repository.findAll()).thenReturn(List.of(entity));
        when(mapper.mapToResponse(entity)).thenReturn(response);

        List<VehicleResponse> results = service.findAll();

        assertEquals(1, results.size());
        assertEquals("Toyota", results.get(0).getVehicleMake());
    }

    @Test
    void findById_whenMissing_throwsResourceNotFound() {
        when(repository.findById(99L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> service.findById(99L));
    }

    @Test
    void save_persistsAndReturnsResponse() {
        Date date = new Date();
        VehicleRequest request = new VehicleRequest(1L, "Toyota", "Camry", "ABC-123", "VIN1234567890ABCD", "PETROL", "SEDAN", date, null);
        Vehicle mapped = new Vehicle(null, 1L, "Toyota", "Camry", "ABC-123", "VIN1234567890ABCD", "PETROL", "SEDAN", date, null);
        Vehicle saved = new Vehicle(1L, 1L, "Toyota", "Camry", "ABC-123", "VIN1234567890ABCD", "PETROL", "SEDAN", date, null);
        VehicleResponse response = VehicleResponse.builder().id(1L).vehicleMake("Toyota").build();

        when(mapper.mapToEntity(request)).thenReturn(mapped);
        when(repository.save(mapped)).thenReturn(1L);
        when(repository.findById(1L)).thenReturn(Optional.of(saved));
        when(mapper.mapToResponse(saved)).thenReturn(response);

        VehicleResponse result = service.save(request);

        assertEquals(1L, result.getId());
        verify(repository).save(mapped);
    }

    @Test
    void update_whenExists_updatesById() {
        Date date = new Date();
        VehicleRequest request = new VehicleRequest(1L, "Honda", "Civic", "XYZ-789", "VIN1234567890ABCX", "PETROL", "SEDAN", date, null);
        Vehicle existing = new Vehicle(1L, 1L, "Toyota", "Camry", "ABC-123", "VIN1234567890ABCD", "PETROL", "SEDAN", date, null);
        Vehicle mapped = new Vehicle(null, 1L, "Honda", "Civic", "XYZ-789", "VIN1234567890ABCX", "PETROL", "SEDAN", date, null);
        Vehicle updated = new Vehicle(1L, 1L, "Honda", "Civic", "XYZ-789", "VIN1234567890ABCX", "PETROL", "SEDAN", date, null);
        VehicleResponse response = VehicleResponse.builder().id(1L).vehicleMake("Honda").build();

        when(repository.findById(1L)).thenReturn(Optional.of(existing), Optional.of(updated));
        when(mapper.mapToEntity(request)).thenReturn(mapped);
        when(mapper.mapToResponse(updated)).thenReturn(response);

        VehicleResponse result = service.update(1L, request);

        ArgumentCaptor<Vehicle> captor = ArgumentCaptor.forClass(Vehicle.class);
        verify(repository).update(eq(1L), captor.capture());
        assertEquals("Honda", captor.getValue().getVehicleMake());
        assertEquals("Honda", result.getVehicleMake());
    }

    @Test
    void delete_whenMissing_throwsResourceNotFound() {
        when(repository.findById(7L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> service.delete(7L));
    }
}
