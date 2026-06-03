package ba.unsa.etf.employeemanagement.controller;

import ba.unsa.etf.employeemanagement.controller.nbp.ExportController;
import ba.unsa.etf.employeemanagement.exceptions.BadRequestException;
import ba.unsa.etf.employeemanagement.service.api.IXmlExportService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class ExportControllerTest {

    @Mock
    private IXmlExportService exportService;
    @InjectMocks
    private ExportController exportController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void exportXml_successfulExport_returnsOkWithBody() {
        byte[] xml = "<export generatedAt=\"2026-01-01\"><table name=\"DEPARTMENT\"/></export>".getBytes();
        when(exportService.exportTableAsXml("DEPARTMENT")).thenReturn(xml);

        ResponseEntity<byte[]> response = exportController.exportXml("DEPARTMENT");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().length > 0);
    }

    @Test
    void exportXml_successfulExport_hasCorrectHeaders() {
        byte[] xml = "<export/>".getBytes();
        when(exportService.exportTableAsXml("DEPARTMENT")).thenReturn(xml);

        ResponseEntity<byte[]> response = exportController.exportXml("DEPARTMENT");

        assertEquals(MediaType.APPLICATION_XML, response.getHeaders().getContentType());

        String disposition = response.getHeaders().getFirst(HttpHeaders.CONTENT_DISPOSITION);
        assertNotNull(disposition);
        assertTrue(disposition.startsWith("attachment; filename=\"export-"));
        assertTrue(disposition.endsWith(".xml\""));

        assertEquals("no-store", response.getHeaders().getFirst(HttpHeaders.CACHE_CONTROL));
    }

    @Test
    void exportXml_invalidTable_returnsBadRequest() {
        when(exportService.exportTableAsXml("UNKNOWN")).thenThrow(new BadRequestException("not allowed"));

        ResponseEntity<byte[]> response = exportController.exportXml("UNKNOWN");

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void exportXml_unexpectedException_returnsInternalServerError() {
        when(exportService.exportTableAsXml("DEPARTMENT")).thenThrow(new RuntimeException("db error"));

        ResponseEntity<byte[]> response = exportController.exportXml("DEPARTMENT");

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }
}
