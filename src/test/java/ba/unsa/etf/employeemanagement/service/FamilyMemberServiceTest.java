package ba.unsa.etf.employeemanagement.service;

import ba.unsa.etf.employeemanagement.dto.request.FamilyMemberRequest;
import ba.unsa.etf.employeemanagement.dto.response.FamilyMemberResponse;
import ba.unsa.etf.employeemanagement.exceptions.ResourceNotFoundException;
import ba.unsa.etf.employeemanagement.mapper.FamilyMemberMapper;
import ba.unsa.etf.employeemanagement.model.FamilyMember;
import ba.unsa.etf.employeemanagement.repository.FamilyMemberRepository;
import ba.unsa.etf.employeemanagement.service.impl.FamilyMemberService;
import ba.unsa.etf.employeemanagement.util.enums.FamilyRelation;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FamilyMemberServiceTest {

    @Mock
    private FamilyMemberRepository repository;

    @Mock
    private FamilyMemberMapper mapper;

    @InjectMocks
    private FamilyMemberService service;

    @Test
    void findAll_ReturnsMappedResponses() {
        FamilyMember entity = new FamilyMember(1L, 1L, "John", "Doe", "CHILD", new Date(), 1, "Student");
        FamilyMemberResponse response = FamilyMemberResponse.builder().id(1L).firstName("John").build();

        when(repository.findAll()).thenReturn(List.of(entity));
        when(mapper.mapToResponse(entity)).thenReturn(response);

        List<FamilyMemberResponse> results = service.findAll();

        assertEquals(1, results.size());
        verify(repository).findAll();
    }

    @Test
    void findById_WhenMissing_ThrowsException() {
        when(repository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> service.findById(1L));
    }

    @Test
    void save_PersistsAndReturnsResponse() {
        FamilyMemberRequest request = new FamilyMemberRequest(1L, "John", "Doe", "CHILD", new Date(System.currentTimeMillis() - 10000), 1, "Student");
        FamilyMember entity = new FamilyMember(null, 1L, "John", "Doe", "CHILD", request.getDateOfBirth(), 1, "Student");
        FamilyMember saved = new FamilyMember(1L, 1L, "John", "Doe", "CHILD", request.getDateOfBirth(), 1, "Student");
        FamilyMemberResponse response = FamilyMemberResponse.builder().id(1L).firstName("John").build();

        when(mapper.mapToEntity(request)).thenReturn(entity);
        when(repository.save(entity)).thenReturn(1L);
        when(repository.findById(1L)).thenReturn(Optional.of(saved));
        when(mapper.mapToResponse(saved)).thenReturn(response);

        FamilyMemberResponse result = service.save(request);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        verify(repository).save(entity);
    }

    @Test
    void update_WhenExists_UpdatesAndReturnsResponse() {
        FamilyMemberRequest request = new FamilyMemberRequest(1L, "Updated", "Doe", "CHILD", new Date(System.currentTimeMillis() - 10000), 1, "Student");
        FamilyMember existing = new FamilyMember(1L, 1L, "Old", "Doe", "CHILD", new Date(), 1, "Student");
        FamilyMember mapped = new FamilyMember(1L, 1L, "Updated", "Doe", "CHILD", request.getDateOfBirth(), 1, "Student");
        FamilyMemberResponse response = FamilyMemberResponse.builder().id(1L).firstName("Updated").build();

        when(repository.findById(1L)).thenReturn(Optional.of(existing), Optional.of(mapped));
        when(mapper.mapToEntity(request)).thenReturn(mapped);
        when(mapper.mapToResponse(mapped)).thenReturn(response);

        FamilyMemberResponse result = service.update(1L, request);

        assertEquals("Updated", result.getFirstName());
        verify(repository).update(eq(1L), any());
    }

    @Test
    void delete_WhenMissing_ThrowsException() {
        when(repository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> service.delete(1L));
    }
}
