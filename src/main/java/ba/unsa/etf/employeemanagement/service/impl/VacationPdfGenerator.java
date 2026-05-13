package ba.unsa.etf.employeemanagement.service.impl;

import ba.unsa.etf.employeemanagement.dto.response.VacationReportDetailedResponse;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.properties.UnitValue;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.List;

@Component
public class VacationPdfGenerator {

    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd.MM.yyyy");

    public byte[] generateMonthlyReport(Integer month, Integer year, List<VacationReportDetailedResponse> details) {
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            PdfWriter writer = new PdfWriter(baos);
            PdfDocument pdfDocument = new PdfDocument(writer);
            Document document = new Document(pdfDocument);

            // Title
            String title = String.format("Monthly Vacation Report - %02d/%d", month, year);
            Paragraph titleParagraph = new Paragraph(title)
                    .setFontSize(18)
                    .setBold()
                    .setTextAlignment(TextAlignment.CENTER);
            document.add(titleParagraph);

            document.add(new Paragraph("\n"));

            // Summary
            long approvedCount = details.stream().filter(d -> "APPROVED".equals(d.getStatus())).count();
            long pendingCount = details.stream().filter(d -> "PENDING".equals(d.getStatus())).count();
            long rejectedCount = details.stream().filter(d -> "REJECTED".equals(d.getStatus())).count();
            int totalDaysUsed = details.stream()
                    .filter(d -> "APPROVED".equals(d.getStatus()))
                    .mapToInt(VacationReportDetailedResponse::getTotalDays)
                    .sum();

            document.add(new Paragraph(String.format("Total Vacation Requests: %d", details.size())));
            document.add(new Paragraph(String.format("Approved: %d | Pending: %d | Rejected: %d", approvedCount, pendingCount, rejectedCount)));
            document.add(new Paragraph(String.format("Total Approved Days: %d", totalDaysUsed)));

            document.add(new Paragraph("\n"));

            // Table
            float[] columnWidths = {1, 3, 3, 2, 2, 2, 2, 1};
            Table table = new Table(UnitValue.createPercentArray(columnWidths)).useAllAvailableWidth();

            // Header
            table.addHeaderCell(new Cell().add(new Paragraph("#").setBold()));
            table.addHeaderCell(new Cell().add(new Paragraph("First Name").setBold()));
            table.addHeaderCell(new Cell().add(new Paragraph("Last Name").setBold()));
            table.addHeaderCell(new Cell().add(new Paragraph("Start Date").setBold()));
            table.addHeaderCell(new Cell().add(new Paragraph("End Date").setBold()));
            table.addHeaderCell(new Cell().add(new Paragraph("Type").setBold()));
            table.addHeaderCell(new Cell().add(new Paragraph("Status").setBold()));
            table.addHeaderCell(new Cell().add(new Paragraph("Days").setBold()));

            int index = 1;
            for (VacationReportDetailedResponse detail : details) {
                table.addCell(new Cell().add(new Paragraph(String.valueOf(index++))));
                table.addCell(new Cell().add(new Paragraph(detail.getEmployeeFirstName())));
                table.addCell(new Cell().add(new Paragraph(detail.getEmployeeLastName())));
                table.addCell(new Cell().add(new Paragraph(DATE_FORMAT.format(detail.getStartDate()))));
                table.addCell(new Cell().add(new Paragraph(DATE_FORMAT.format(detail.getEndDate()))));
                table.addCell(new Cell().add(new Paragraph(detail.getVacationType())));
                table.addCell(new Cell().add(new Paragraph(detail.getStatus())));
                table.addCell(new Cell().add(new Paragraph(String.valueOf(detail.getTotalDays()))));
            }

            document.add(table);
            document.add(new Paragraph("\n"));
            document.add(new Paragraph(String.format("Report generated on: %s",
                    DATE_FORMAT.format(new java.util.Date())))
                    .setFontSize(9)
                    .setTextAlignment(TextAlignment.RIGHT));

            document.close();
            return baos.toByteArray();

        } catch (Exception e) {
            throw new RuntimeException("Failed to generate PDF report", e);
        }
    }
}
