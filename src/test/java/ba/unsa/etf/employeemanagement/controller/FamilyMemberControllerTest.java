package ba.unsa.etf.employeemanagement.controller;

import ba.unsa.etf.employeemanagement.dto.request.FamilyMemberRequest;
import ba.unsa.etf.employeemanagement.dto.response.FamilyMemberResponse;
import ba.unsa.etf.employeemanagement.service.api.IFamilyMemberService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FamilyMemberControllerTest {

    @Mock
    private IFamilyMemberService service;

    @InjectMocks
    private FamilyMemberController controller;

    @Test
    void findAll_ReturnsList() {
        FamilyMemberResponse response = FamilyMemberResponse.builder().id(1L).build();
        when(service.findAll()).thenReturn(List.of(response));

        List<FamilyMemberResponse> result = controller.findAll();

        assertEquals(1, result.size());
        verify(service).findAll();
    }

    @Test
    void findById_ReturnsResponse() {
        FamilyMemberResponse response = FamilyMemberResponse.builder().id(1L).build();
        when(service.findById(1L)).thenReturn(response);

        FamilyMemberResponse result = controller.findById(1L);

        assertEquals(1L, result.getId());
        verify(service).findById(1L);
    }

    @Test
    void create_ReturnsCreatedResponse() {
        FamilyMemberRequest request = new FamilyMemberRequest();
        FamilyMemberResponse response = FamilyMemberResponse.builder().id(1L).build();
        when(service.save(request)).thenReturn(response);

        ResponseEntity<FamilyMemberResponse> result = controller.create(request);

        assertEquals(HttpStatus.CREATED, result.getStatusCode());
        assertEquals(1L, result.getBody().getId());
        verify(service).save(request);
    }

    @Test
    void update_ReturnsOkResponse() {
        FamilyMemberRequest request = new FamilyMemberRequest();
        FamilyMemberResponse response = FamilyMemberResponse.builder().id(1L).build();
        when(service.update(1L, request)).thenReturn(response);

        ResponseEntity<FamilyMemberResponse> result = controller.update(1L, request);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(1L, result.getBody().getId());
        verify(service).update(1L, request);
    }

    @Test
    void delete_ReturnsNoContent() {
        doNothing().when(service).delete(1L);

        ResponseEntity<Void> result = controller.delete(1L);

        assertEquals(HttpStatus.NO_CONTENT, result.getStatusCode());
        verify(service).delete(1L);
    }
}
