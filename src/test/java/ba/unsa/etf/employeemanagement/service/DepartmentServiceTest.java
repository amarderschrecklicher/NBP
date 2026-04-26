package ba.unsa.etf.employeemanagement.service;

import ba.unsa.etf.employeemanagement.dto.request.DepartmentRequest;
import ba.unsa.etf.employeemanagement.dto.response.DepartmentResponse;
import ba.unsa.etf.employeemanagement.exceptions.ResourceNotFoundException;
import ba.unsa.etf.employeemanagement.mapper.DepartmentMapper;
import ba.unsa.etf.employeemanagement.model.Department;
import ba.unsa.etf.employeemanagement.repository.DepartmentRepository;
import ba.unsa.etf.employeemanagement.service.impl.DepartmentService;
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
class DepartmentServiceTest {

    @Mock
    private DepartmentRepository repository;

    @Mock
    private DepartmentMapper mapper;

    @InjectMocks
    private DepartmentService service;

    @Test
    void findAll_returnsMappedResponses() {
        Department entity = new Department(1L, "IT", "Information Technology");
        DepartmentResponse response = DepartmentResponse.builder().id(1L).name("IT").description("Information Technology").build();

        when(repository.findAll()).thenReturn(List.of(entity));
        when(mapper.mapToResponse(entity)).thenReturn(response);

        List<DepartmentResponse> results = service.findAll();

        assertEquals(1, results.size());
        assertEquals("IT", results.get(0).getName());
        verify(repository).findAll();
        verify(mapper).mapToResponse(entity);
    }

    @Test
    void findById_whenMissing_throwsResourceNotFound() {
        when(repository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> service.findById(99L));
    }

    @Test
    void save_persistsAndReturnsResponse() {
        DepartmentRequest request = new DepartmentRequest("HR", "People operations");
        Department entity = new Department(null, "HR", "People operations");
        Department saved = new Department(5L, "HR", "People operations");
        DepartmentResponse response = DepartmentResponse.builder().id(5L).name("HR").description("People operations").build();

        when(mapper.mapToEntity(request)).thenReturn(entity);
        when(repository.save(entity)).thenReturn(5L);
        when(repository.findById(5L)).thenReturn(Optional.of(saved));
        when(mapper.mapToResponse(saved)).thenReturn(response);

        DepartmentResponse result = service.save(request);

        assertEquals(5L, result.getId());
        assertEquals("HR", result.getName());
        verify(repository).save(entity);
    }

    @Test
    void update_whenExists_updatesById() {
        DepartmentRequest request = new DepartmentRequest("Legal", "Legal affairs");
        Department mapped = new Department(null, "Legal", "Legal affairs");
        Department updated = new Department(3L, "Legal", "Legal affairs");
        DepartmentResponse response = DepartmentResponse.builder().id(3L).name("Legal").description("Legal affairs").build();

        when(repository.findById(3L)).thenReturn(Optional.of(new Department(3L, "Old", "Old")), Optional.of(updated));
        when(mapper.mapToEntity(request)).thenReturn(mapped);
        when(mapper.mapToResponse(updated)).thenReturn(response);

        DepartmentResponse result = service.update(3L, request);

        ArgumentCaptor<Department> entityCaptor = ArgumentCaptor.forClass(Department.class);
        verify(repository).update(eq(3L), entityCaptor.capture());
        assertEquals(3L, entityCaptor.getValue().getId());
        assertEquals("Legal", result.getName());
    }

    @Test
    void delete_whenMissing_throwsResourceNotFound() {
        when(repository.findById(7L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> service.delete(7L));
        verify(repository, never()).deleteById(anyLong());
    }
}

