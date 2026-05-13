package ba.unsa.etf.employeemanagement.repository;

import ba.unsa.etf.employeemanagement.model.VacationReport;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class VacationReportRepository {

    private final JdbcTemplate jdbcTemplate;

    private final RowMapper<VacationReport> rowMapper = (rs, rowNum) -> {
        VacationReport report = new VacationReport();
        report.setId(rs.getLong("ID"));
        report.setReportMonth(rs.getInt("REPORT_MONTH"));
        report.setReportYear(rs.getInt("REPORT_YEAR"));
        report.setPdfContent(rs.getBytes("PDF_CONTENT"));
        report.setGeneratedAt(rs.getTimestamp("GENERATED_AT"));
        report.setGeneratedBy(rs.getLong("GENERATED_BY"));
        return report;
    };

    private final RowMapper<VacationReport> rowMapperWithoutBlob = (rs, rowNum) -> {
        VacationReport report = new VacationReport();
        report.setId(rs.getLong("ID"));
        report.setReportMonth(rs.getInt("REPORT_MONTH"));
        report.setReportYear(rs.getInt("REPORT_YEAR"));
        report.setGeneratedAt(rs.getTimestamp("GENERATED_AT"));
        report.setGeneratedBy(rs.getLong("GENERATED_BY"));
        return report;
    };

    public Long save(VacationReport report) {
        String sql = "INSERT INTO VACATION_REPORT (REPORT_MONTH, REPORT_YEAR, PDF_CONTENT, GENERATED_AT, GENERATED_BY) " +
                "VALUES (?, ?, ?, ?, ?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, new String[]{"ID"});
            ps.setInt(1, report.getReportMonth());
            ps.setInt(2, report.getReportYear());
            ps.setBytes(3, report.getPdfContent());
            ps.setTimestamp(4, new Timestamp(report.getGeneratedAt().getTime()));
            ps.setLong(5, report.getGeneratedBy());
            return ps;
        }, keyHolder);

        return keyHolder.getKey().longValue();
    }

    public Optional<VacationReport> findById(Long id) {
        String sql = "SELECT * FROM VACATION_REPORT WHERE ID = ?";
        List<VacationReport> results = jdbcTemplate.query(sql, rowMapper, id);
        return results.stream().findFirst();
    }

    public Optional<VacationReport> findByMonthAndYear(Integer month, Integer year) {
        String sql = "SELECT * FROM VACATION_REPORT WHERE REPORT_MONTH = ? AND REPORT_YEAR = ?";
        List<VacationReport> results = jdbcTemplate.query(sql, rowMapper, month, year);
        return results.stream().findFirst();
    }

    public List<VacationReport> findAll() {
        String sql = "SELECT ID, REPORT_MONTH, REPORT_YEAR, GENERATED_AT, GENERATED_BY FROM VACATION_REPORT ORDER BY REPORT_YEAR DESC, REPORT_MONTH DESC";
        return jdbcTemplate.query(sql, rowMapperWithoutBlob);
    }

    public void update(VacationReport report) {
        String sql = "UPDATE VACATION_REPORT SET PDF_CONTENT = ?, GENERATED_AT = ?, GENERATED_BY = ? " +
                "WHERE REPORT_MONTH = ? AND REPORT_YEAR = ?";
        jdbcTemplate.update(sql, report.getPdfContent(), new Timestamp(report.getGeneratedAt().getTime()),
                report.getGeneratedBy(), report.getReportMonth(), report.getReportYear());
    }

    public void deleteById(Long id) {
        jdbcTemplate.update("DELETE FROM VACATION_REPORT WHERE ID = ?", id);
    }
}
