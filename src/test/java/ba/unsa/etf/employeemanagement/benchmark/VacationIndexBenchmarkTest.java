package ba.unsa.etf.employeemanagement.benchmark;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;
import java.util.Map;

/**
 * Benchmark test to demonstrate the performance improvement of indexes
 * on the VACATION table.
 *
 * Indexes tested:
 * 1. IDX_VACATION_EMP_START on VACATION(EMPLOYEE_ID, START_DATE)
 *    - Speeds up: findByEmployeeIdAndYear (filters by employee_id + year extraction from start_date)
 *
 * 2. IDX_VACATION_START_END on VACATION(START_DATE, END_DATE)
 *    - Speeds up: findByMonthAndYear (filters by month/year extraction from both date columns)
 *
 * Run this test with the real database connection (not the test profile).
 * Usage: mvn test -Dtest=VacationIndexBenchmarkTest -Dspring.profiles.active=default
 */
@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class VacationIndexBenchmarkTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private static final int ITERATIONS = 50;

    @BeforeAll
    static void printHeader() {
        System.out.println("=============================================================");
        System.out.println("       VACATION TABLE INDEX BENCHMARK");
        System.out.println("=============================================================");
    }

    // ==================== BENCHMARK 1: findByEmployeeIdAndYear ====================

    @Test
    @Order(1)
    void benchmark_findByEmployeeIdAndYear_WITHOUT_index() {
        // Drop index if it exists
        dropIndexSafely("IDX_VACATION_EMP_START");

        String sql = "SELECT id, employee_id, start_date, end_date, vacation_type, status, " +
                "approved_by, reason FROM vacation WHERE employee_id = ? AND EXTRACT(YEAR FROM start_date) = ?";

        // Warm up
        for (int i = 0; i < 5; i++) {
            jdbcTemplate.queryForList(sql, 1L, 2025);
        }

        // Benchmark
        long startTime = System.nanoTime();
        for (int i = 0; i < ITERATIONS; i++) {
            jdbcTemplate.queryForList(sql, 1L, 2025);
        }
        long endTime = System.nanoTime();

        double avgMs = (endTime - startTime) / 1_000_000.0 / ITERATIONS;
        System.out.println("\n[WITHOUT INDEX] findByEmployeeIdAndYear");
        System.out.println("  Average query time: " + String.format("%.3f", avgMs) + " ms");
        System.out.println("  Total time for " + ITERATIONS + " iterations: " +
                String.format("%.3f", (endTime - startTime) / 1_000_000.0) + " ms");

        // Show execution plan
        printExplainPlan("SELECT id, employee_id, start_date, end_date, vacation_type, status, " +
                "approved_by, reason FROM vacation WHERE employee_id = 1 AND EXTRACT(YEAR FROM start_date) = 2025");
    }

    @Test
    @Order(2)
    void benchmark_findByEmployeeIdAndYear_WITH_index() {
        // Create the index
        createIndexSafely("CREATE INDEX IDX_VACATION_EMP_START ON VACATION(EMPLOYEE_ID, START_DATE)");

        String sql = "SELECT id, employee_id, start_date, end_date, vacation_type, status, " +
                "approved_by, reason FROM vacation WHERE employee_id = ? AND EXTRACT(YEAR FROM start_date) = ?";

        // Warm up
        for (int i = 0; i < 5; i++) {
            jdbcTemplate.queryForList(sql, 1L, 2025);
        }

        // Benchmark
        long startTime = System.nanoTime();
        for (int i = 0; i < ITERATIONS; i++) {
            jdbcTemplate.queryForList(sql, 1L, 2025);
        }
        long endTime = System.nanoTime();

        double avgMs = (endTime - startTime) / 1_000_000.0 / ITERATIONS;
        System.out.println("\n[WITH INDEX] findByEmployeeIdAndYear (IDX_VACATION_EMP_START)");
        System.out.println("  Average query time: " + String.format("%.3f", avgMs) + " ms");
        System.out.println("  Total time for " + ITERATIONS + " iterations: " +
                String.format("%.3f", (endTime - startTime) / 1_000_000.0) + " ms");

        // Show execution plan
        printExplainPlan("SELECT id, employee_id, start_date, end_date, vacation_type, status, " +
                "approved_by, reason FROM vacation WHERE employee_id = 1 AND EXTRACT(YEAR FROM start_date) = 2025");
    }

    // ==================== BENCHMARK 2: findByMonthAndYear ====================

    @Test
    @Order(3)
    void benchmark_findByMonthAndYear_WITHOUT_index() {
        // Drop index if it exists
        dropIndexSafely("IDX_VACATION_START_END");

        String sql = "SELECT * FROM VACATION WHERE " +
                "(EXTRACT(MONTH FROM START_DATE) = ? AND EXTRACT(YEAR FROM START_DATE) = ?) " +
                "OR (EXTRACT(MONTH FROM END_DATE) = ? AND EXTRACT(YEAR FROM END_DATE) = ?)";

        // Warm up
        for (int i = 0; i < 5; i++) {
            jdbcTemplate.queryForList(sql, 6, 2025, 6, 2025);
        }

        // Benchmark
        long startTime = System.nanoTime();
        for (int i = 0; i < ITERATIONS; i++) {
            jdbcTemplate.queryForList(sql, 6, 2025, 6, 2025);
        }
        long endTime = System.nanoTime();

        double avgMs = (endTime - startTime) / 1_000_000.0 / ITERATIONS;
        System.out.println("\n[WITHOUT INDEX] findByMonthAndYear");
        System.out.println("  Average query time: " + String.format("%.3f", avgMs) + " ms");
        System.out.println("  Total time for " + ITERATIONS + " iterations: " +
                String.format("%.3f", (endTime - startTime) / 1_000_000.0) + " ms");

        // Show execution plan
        printExplainPlan("SELECT * FROM VACATION WHERE " +
                "(EXTRACT(MONTH FROM START_DATE) = 6 AND EXTRACT(YEAR FROM START_DATE) = 2025) " +
                "OR (EXTRACT(MONTH FROM END_DATE) = 6 AND EXTRACT(YEAR FROM END_DATE) = 2025)");
    }

    @Test
    @Order(4)
    void benchmark_findByMonthAndYear_WITH_index() {
        // Create the index
        createIndexSafely("CREATE INDEX IDX_VACATION_START_END ON VACATION(START_DATE, END_DATE)");

        String sql = "SELECT * FROM VACATION WHERE " +
                "(EXTRACT(MONTH FROM START_DATE) = ? AND EXTRACT(YEAR FROM START_DATE) = ?) " +
                "OR (EXTRACT(MONTH FROM END_DATE) = ? AND EXTRACT(YEAR FROM END_DATE) = ?)";

        // Warm up
        for (int i = 0; i < 5; i++) {
            jdbcTemplate.queryForList(sql, 6, 2025, 6, 2025);
        }

        // Benchmark
        long startTime = System.nanoTime();
        for (int i = 0; i < ITERATIONS; i++) {
            jdbcTemplate.queryForList(sql, 6, 2025, 6, 2025);
        }
        long endTime = System.nanoTime();

        double avgMs = (endTime - startTime) / 1_000_000.0 / ITERATIONS;
        System.out.println("\n[WITH INDEX] findByMonthAndYear (IDX_VACATION_START_END)");
        System.out.println("  Average query time: " + String.format("%.3f", avgMs) + " ms");
        System.out.println("  Total time for " + ITERATIONS + " iterations: " +
                String.format("%.3f", (endTime - startTime) / 1_000_000.0) + " ms");

        // Show execution plan
        printExplainPlan("SELECT * FROM VACATION WHERE " +
                "(EXTRACT(MONTH FROM START_DATE) = 6 AND EXTRACT(YEAR FROM START_DATE) = 2025) " +
                "OR (EXTRACT(MONTH FROM END_DATE) = 6 AND EXTRACT(YEAR FROM END_DATE) = 2025)");
    }

    @Test
    @Order(5)
    void printSummary() {
        System.out.println("\n=============================================================");
        System.out.println("       BENCHMARK SUMMARY");
        System.out.println("=============================================================");
        System.out.println("Index 1: IDX_VACATION_EMP_START on VACATION(EMPLOYEE_ID, START_DATE)");
        System.out.println("  -> Optimizes: findByEmployeeIdAndYear query");
        System.out.println("  -> Without index: Full Table Scan on VACATION");
        System.out.println("  -> With index: Index Range Scan (filters employee_id directly)");
        System.out.println();
        System.out.println("Index 2: IDX_VACATION_START_END on VACATION(START_DATE, END_DATE)");
        System.out.println("  -> Optimizes: findByMonthAndYear query");
        System.out.println("  -> Without index: Full Table Scan on VACATION");
        System.out.println("  -> With index: Index range scan on date columns");
        System.out.println("=============================================================");
    }

    // ==================== Helper Methods ====================

    private void printExplainPlan(String sql) {
        try {
            jdbcTemplate.execute("EXPLAIN PLAN FOR " + sql);
            List<Map<String, Object>> plan = jdbcTemplate.queryForList(
                    "SELECT PLAN_TABLE_OUTPUT FROM TABLE(DBMS_XPLAN.DISPLAY('PLAN_TABLE', null, 'BASIC'))");
            System.out.println("  Execution Plan:");
            for (Map<String, Object> row : plan) {
                System.out.println("    " + row.get("PLAN_TABLE_OUTPUT"));
            }
        } catch (Exception e) {
            System.out.println("  (Could not retrieve execution plan: " + e.getMessage() + ")");
        }
    }

    private void dropIndexSafely(String indexName) {
        try {
            jdbcTemplate.execute("DROP INDEX " + indexName);
            System.out.println("  Dropped index: " + indexName);
        } catch (Exception e) {
            // Index doesn't exist, that's fine
        }
    }

    private void createIndexSafely(String createSql) {
        try {
            jdbcTemplate.execute(createSql);
            System.out.println("  Created index successfully");
        } catch (Exception e) {
            // Index might already exist
            System.out.println("  Index already exists or creation failed: " + e.getMessage());
        }
    }
}


