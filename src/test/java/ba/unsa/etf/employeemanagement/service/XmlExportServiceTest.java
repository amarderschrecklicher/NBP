package ba.unsa.etf.employeemanagement.service;

import ba.unsa.etf.employeemanagement.exceptions.BadRequestException;
import ba.unsa.etf.employeemanagement.service.impl.XmlExportService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

class XmlExportServiceTest {

    @Mock
    private JdbcTemplate jdbcTemplate;
    @InjectMocks
    private XmlExportService exportService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void exportTableAsXml_validTable_returnsXmlBytes() {
        Map<String, Object> row = new LinkedHashMap<>();
        row.put("ID", 1L);
        row.put("NAME", "Engineering");
        when(jdbcTemplate.queryForList(anyString())).thenReturn(List.of(row));

        byte[] result = exportService.exportTableAsXml("DEPARTMENT");

        assertNotNull(result);
        assertTrue(result.length > 0);
        String xml = new String(result);
        assertTrue(xml.contains("<export"));
        assertTrue(xml.contains("DEPARTMENT"));
        assertTrue(xml.contains("<row>"));
        assertTrue(xml.contains("<NAME>Engineering</NAME>"));
    }

    @Test
    void exportTableAsXml_emptyTable_returnsValidXmlWithoutRows() {
        when(jdbcTemplate.queryForList(anyString())).thenReturn(Collections.emptyList());

        byte[] result = exportService.exportTableAsXml("DEPARTMENT");

        assertNotNull(result);
        String xml = new String(result);
        assertTrue(xml.contains("<export"));
        assertTrue(xml.contains("<table"));
        assertFalse(xml.contains("<row>"));
    }

    @Test
    void exportTableAsXml_caseInsensitiveInput_works() {
        when(jdbcTemplate.queryForList(anyString())).thenReturn(Collections.emptyList());

        byte[] result = exportService.exportTableAsXml("department");

        assertNotNull(result);
        assertTrue(new String(result).contains("DEPARTMENT"));
    }

    @Test
    void exportTableAsXml_invalidTableName_throwsBadRequestException() {
        assertThrows(BadRequestException.class, () -> exportService.exportTableAsXml("UNKNOWN_TABLE"));
    }

    @Test
    void exportTableAsXml_blankTableName_throwsBadRequestException() {
        assertThrows(BadRequestException.class, () -> exportService.exportTableAsXml("   "));
    }

    @Test
    void exportTableAsXml_nullTableName_throwsBadRequestException() {
        assertThrows(BadRequestException.class, () -> exportService.exportTableAsXml(null));
    }
}
