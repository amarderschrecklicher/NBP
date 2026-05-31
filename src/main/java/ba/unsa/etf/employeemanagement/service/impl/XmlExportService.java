package ba.unsa.etf.employeemanagement.service.impl;

import ba.unsa.etf.employeemanagement.exceptions.BadRequestException;
import ba.unsa.etf.employeemanagement.service.api.IXmlExportService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.ByteArrayOutputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Slf4j
public class XmlExportService implements IXmlExportService {

    private final JdbcTemplate jdbcTemplate;

    private static final String SCHEMA = "NBPT9";
    private static final Set<String> ALLOWED_TABLES = Set.of(
            "ADDRESS", "DEPARTMENT", "EMPLOYMENT", "VACATION", "EMERGENCY_CONTACT",
            "FAMILY_MEMBER", "DISABILITY", "FINANCE", "PERSONAL_CONTACT",
            "VEHICLE", "WORK_PERMIT", "HOLIDAY"
    );

    @Override
    public byte[] exportTableAsXml(String tableName) {
        if (tableName == null || tableName.isBlank()) {
            throw new BadRequestException("Table name must not be blank");
        }
        String normalized = tableName.trim().toUpperCase();
        if (!ALLOWED_TABLES.contains(normalized)) {
            throw new BadRequestException("Table '" + tableName + "' is not available for export");
        }

        log.debug("Exporting table: {}", normalized);
        List<Map<String, Object>> rows = jdbcTemplate.queryForList(
                "SELECT * FROM " + SCHEMA + "." + normalized
        );

        try {
            return buildXml(normalized, rows);
        } catch (Exception e) {
            log.error("Failed to build XML for table {}", normalized, e);
            throw new RuntimeException("Export failed");
        }
    }

    private byte[] buildXml(String tableName, List<Map<String, Object>> rows) throws Exception {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = builder.newDocument();

        Element root = doc.createElement("export");
        root.setAttribute("generatedAt",
                LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
        doc.appendChild(root);

        Element tableEl = doc.createElement("table");
        tableEl.setAttribute("name", tableName);
        root.appendChild(tableEl);

        for (Map<String, Object> row : rows) {
            Element rowEl = doc.createElement("row");
            for (Map.Entry<String, Object> entry : row.entrySet()) {
                String tagName = toXmlTagName(entry.getKey());
                Element col = doc.createElement(tagName);
                col.setTextContent(entry.getValue() != null ? entry.getValue().toString() : "");
                rowEl.appendChild(col);
            }
            tableEl.appendChild(rowEl);
        }

        TransformerFactory tf = TransformerFactory.newInstance();
        tf.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);
        Transformer transformer = tf.newTransformer();
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
        transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        transformer.transform(new DOMSource(doc), new StreamResult(baos));
        return baos.toByteArray();
    }

    private String toXmlTagName(String columnName) {
        if (columnName == null || columnName.isBlank()) return "_column";
        String sanitized = columnName.replaceAll("[^a-zA-Z0-9_]", "_");
        if (Character.isDigit(sanitized.charAt(0))) {
            sanitized = "_" + sanitized;
        }
        return sanitized;
    }
}
