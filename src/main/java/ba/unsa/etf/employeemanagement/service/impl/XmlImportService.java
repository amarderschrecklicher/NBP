package ba.unsa.etf.employeemanagement.service.impl;

import ba.unsa.etf.employeemanagement.exceptions.BadRequestException;
import ba.unsa.etf.employeemanagement.service.api.IXmlImportService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.CallableStatementCallback;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.sql.CallableStatement;
import java.sql.Clob;
import java.sql.SQLException;
import java.sql.Types;

@Service
@RequiredArgsConstructor
@Slf4j
public class XmlImportService implements IXmlImportService {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public String importXmlContent(byte[] xmlContent) {
        if (xmlContent == null || xmlContent.length == 0) {
            throw new IllegalArgumentException("XML content must not be empty");
        }

        String xmlString = new String(xmlContent, StandardCharsets.UTF_8);
        log.debug("Importing XML content of size: {} bytes", xmlContent.length);

        try {
            return jdbcTemplate.execute(
                    "{call PKG_XML_IMPORT.IMPORT_ALL_TABLES(?, ?)}",
                    (CallableStatementCallback<String>) cs -> {
                        // Get connection from CallableStatement
                        java.sql.Connection conn = cs.getConnection();

                        // Create CLOB from XML string
                        Clob xmlClob = conn.createClob();
                        xmlClob.setString(1, xmlString);

                        // Set input parameter
                        cs.setClob(1, xmlClob);

                        // Register output parameter
                        cs.registerOutParameter(2, Types.CLOB);

                        // Execute procedure
                        cs.execute();

                        // Get result
                        Clob resultClob = cs.getClob(2);
                        String result = resultClob.getSubString(1, (int) resultClob.length());
                        resultClob.free();
                        xmlClob.free();

                        return result;
                    });
        } catch (BadRequestException e) {
            log.warn("Bad request during XML import: {}", e.getMessage());
            throw e;
        } catch (IllegalArgumentException e) {
            log.warn("Validation error during XML import: {}", e.getMessage());
            throw new BadRequestException("Invalid XML: " + e.getMessage());
        } catch (org.springframework.dao.DataAccessException e) {
            log.error("Database error during XML import", e);
            // Extract error message from SQL exception
            String errorMsg = extractErrorMessage(e);
            throw new BadRequestException("XML import failed: " + errorMsg);
        } catch (Exception e) {
            log.error("Unexpected error during XML import", e);
            throw new RuntimeException("XML import failed: " + e.getMessage());
        }
    }

    /**
     * Extracts meaningful error message from Spring DataAccessException.
     * Oracle errors from RAISE_APPLICATION_ERROR appear in the exception message.
     */
    private String extractErrorMessage(org.springframework.dao.DataAccessException e) {
        Throwable cause = e.getCause();
        String message = e.getMessage();

        if (cause instanceof SQLException) {
            SQLException sqlEx = (SQLException) cause;
            message = sqlEx.getMessage();
        }

        if (message != null) {
            // Oracle error format: ORA-20XXX: message
            if (message.contains("ORA-")) {
                int startIdx = message.indexOf(":");
                if (startIdx > 0 && startIdx < message.length() - 1) {
                    return message.substring(startIdx + 1).trim();
                }
            }
            return message;
        }
        return "Unknown database error";
    }
}
