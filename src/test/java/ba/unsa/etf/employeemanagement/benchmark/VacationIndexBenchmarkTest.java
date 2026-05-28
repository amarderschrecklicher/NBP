package ba.unsa.etf.employeemanagement.benchmark;

import org.h2.jdbcx.JdbcDataSource;
import org.junit.jupiter.api.*;
import org.springframework.jdbc.core.JdbcTemplate;

import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * Benchmark test demonstrating the performance improvement of indexes on the VACATION table.
 *
 * Uses an H2 in-memory database seeded with 100 000 mock vacation records so
 * no real Oracle / MongoDB connection is required.
 *
 * Indexes tested:
 *  1. IDX_VACATION_EMP_START  on VACATION(EMPLOYEE_ID, START_DATE)
 *     -> Speeds up: findByEmployeeIdAndYear  (equality on employee_id + year filter)
 *
 *  2. IDX_VACATION_START_DATE on VACATION(START_DATE)
 *     -> Speeds up: finding vacations that start within a narrow date window
 *        (3 days out of 6 years ≈ 0.14 % selectivity → index scan vs full scan)
 *
 * Run: mvn test -Dtest="ba.unsa.etf.employeemanagement.benchmark.VacationIndexBenchmarkTest"
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class VacationIndexBenchmarkTest {

    private static JdbcTemplate jdbcTemplate;

    private static final int ITERATIONS     = 50;
    private static final int DATA_SIZE      = 100_000;
    private static final int EMPLOYEE_COUNT = 500;

    // result holders so the summary test can read them
    private static double q1WithoutAvg;
    private static double q1WithAvg;
    private static double q2WithoutAvg;
    private static double q2WithAvg;

    // ── Setup / Teardown ──────────────────────────────────────────────────

    @BeforeAll
    static void setup() {
        JdbcDataSource ds = new JdbcDataSource();
        // DB_CLOSE_DELAY=-1 keeps the in-memory DB alive for the whole test class
        ds.setURL("jdbc:h2:mem:benchmark;DB_CLOSE_DELAY=-1");
        ds.setUser("sa");
        ds.setPassword("");

        jdbcTemplate = new JdbcTemplate(ds);
        createVacationTable();
        insertMockData();
    }

    @AfterAll
    static void tearDown() {
        jdbcTemplate.execute("DROP TABLE IF EXISTS VACATION");
    }

    private static void createVacationTable() {
        jdbcTemplate.execute("DROP TABLE IF EXISTS VACATION");
        jdbcTemplate.execute("""
            CREATE TABLE VACATION (
                ID            BIGINT       PRIMARY KEY,
                EMPLOYEE_ID   BIGINT       NOT NULL,
                START_DATE    DATE         NOT NULL,
                END_DATE      DATE         NOT NULL,
                VACATION_TYPE VARCHAR(50),
                STATUS        VARCHAR(20),
                APPROVED_BY   BIGINT,
                REASON        VARCHAR(255)
            )
        """);
    }

    private static void insertMockData() {
        System.out.println("\nInserting " + DATA_SIZE + " mock vacation records into H2 in-memory DB...");

        Random rand = new Random(42);
        String[] types    = {"ANNUAL", "SICK", "MATERNITY", "UNPAID", "EMERGENCY"};
        String[] statuses = {"PENDING", "APPROVED", "REJECTED"};

        List<Object[]> batch = new ArrayList<>(DATA_SIZE);
        for (int i = 0; i < DATA_SIZE; i++) {
            long      empId = (i % EMPLOYEE_COUNT) + 1L;
            int       year  = 2020 + rand.nextInt(6);   // 2020-2025
            int       month = rand.nextInt(12) + 1;
            int       day   = rand.nextInt(28) + 1;
            LocalDate start = LocalDate.of(year, month, day);
            LocalDate end   = start.plusDays(rand.nextInt(14) + 1);

            batch.add(new Object[]{
                (long)(i + 1),
                empId,
                Date.valueOf(start),
                Date.valueOf(end),
                types[rand.nextInt(types.length)],
                statuses[rand.nextInt(statuses.length)],
                rand.nextBoolean() ? (long)(rand.nextInt(50) + 1) : null,
                "Reason " + i
            });
        }

        jdbcTemplate.batchUpdate(
            "INSERT INTO VACATION " +
            "(ID, EMPLOYEE_ID, START_DATE, END_DATE, VACATION_TYPE, STATUS, APPROVED_BY, REASON) " +
            "VALUES (?,?,?,?,?,?,?,?)",
            batch
        );
        System.out.println("Done — " + DATA_SIZE + " rows inserted.\n");
        separator();
    }

    // ── Benchmark 1: findByEmployeeIdAndYear ─────────────────────────────
    // Query : WHERE EMPLOYEE_ID = ? AND EXTRACT(YEAR FROM START_DATE) = ?
    // Index : IDX_VACATION_EMP_START on (EMPLOYEE_ID, START_DATE)
    // Why   : index narrows to ~200 rows for one employee, then filters by year

    @Test
    @Order(1)
    void benchmark1_findByEmployeeIdAndYear_WITHOUT_index() {
        dropIndexSafely("IDX_VACATION_EMP_START");

        String sql =
            "SELECT ID, EMPLOYEE_ID, START_DATE, END_DATE, VACATION_TYPE, STATUS, APPROVED_BY, REASON " +
            "FROM VACATION " +
            "WHERE EMPLOYEE_ID = ? AND EXTRACT(YEAR FROM START_DATE) = ?";

        warmup(sql, 1L, 2025);

        long t0 = System.nanoTime();
        for (int i = 0; i < ITERATIONS; i++) jdbcTemplate.queryForList(sql, 1L, 2025);
        long t1 = System.nanoTime();

        q1WithoutAvg = toMs(t1 - t0) / ITERATIONS;

        System.out.println("[BENCHMARK 1 — findByEmployeeIdAndYear]");
        System.out.println("  State        : WITHOUT index");
        System.out.printf ("  Avg per query: %.3f ms%n", q1WithoutAvg);
        System.out.printf ("  Total (%d x) : %.3f ms%n", ITERATIONS, toMs(t1 - t0));
        printExplainPlan(
            "SELECT ID, EMPLOYEE_ID, START_DATE FROM VACATION " +
            "WHERE EMPLOYEE_ID = 1 AND EXTRACT(YEAR FROM START_DATE) = 2025");
    }

    @Test
    @Order(2)
    void benchmark1_findByEmployeeIdAndYear_WITH_index() {
        createIndexSafely("CREATE INDEX IDX_VACATION_EMP_START ON VACATION(EMPLOYEE_ID, START_DATE)");

        String sql =
            "SELECT ID, EMPLOYEE_ID, START_DATE, END_DATE, VACATION_TYPE, STATUS, APPROVED_BY, REASON " +
            "FROM VACATION " +
            "WHERE EMPLOYEE_ID = ? AND EXTRACT(YEAR FROM START_DATE) = ?";

        warmup(sql, 1L, 2025);

        long t0 = System.nanoTime();
        for (int i = 0; i < ITERATIONS; i++) jdbcTemplate.queryForList(sql, 1L, 2025);
        long t1 = System.nanoTime();

        q1WithAvg = toMs(t1 - t0) / ITERATIONS;

        System.out.println("[BENCHMARK 1 — findByEmployeeIdAndYear]");
        System.out.println("  State        : WITH index IDX_VACATION_EMP_START(EMPLOYEE_ID, START_DATE)");
        System.out.printf ("  Avg per query: %.3f ms%n", q1WithAvg);
        System.out.printf ("  Total (%d x) : %.3f ms%n", ITERATIONS, toMs(t1 - t0));
        if (q1WithoutAvg > 0)
            System.out.printf("  Speedup      : %.1fx faster%n", q1WithoutAvg / q1WithAvg);
        printExplainPlan(
            "SELECT ID, EMPLOYEE_ID, START_DATE FROM VACATION " +
            "WHERE EMPLOYEE_ID = 1 AND EXTRACT(YEAR FROM START_DATE) = 2025");
    }

    // ── Benchmark 2: narrow date-range scan ──────────────────────────────
    // Query : WHERE START_DATE BETWEEN ? AND ?  (3 days ≈ 0.14 % of rows)
    // Index : IDX_VACATION_START_DATE on (START_DATE)
    // Without index: full table scan of 100,000 rows
    // With index   : index range scan, touches only ~137 rows

    @Test
    @Order(3)
    void benchmark2_dateRangeScan_WITHOUT_index() {
        dropIndexSafely("IDX_VACATION_START_DATE");

        Date from = Date.valueOf(LocalDate.of(2025, 6, 15));
        Date to   = Date.valueOf(LocalDate.of(2025, 6, 17));

        String sql = "SELECT * FROM VACATION WHERE START_DATE BETWEEN ? AND ?";

        warmup(sql, from, to);

        long t0 = System.nanoTime();
        for (int i = 0; i < ITERATIONS; i++) jdbcTemplate.queryForList(sql, from, to);
        long t1 = System.nanoTime();

        q2WithoutAvg = toMs(t1 - t0) / ITERATIONS;

        System.out.println("\n[BENCHMARK 2 — narrow date-range scan]");
        System.out.println("  State        : WITHOUT index  (full table scan)");
        System.out.printf ("  Query        : START_DATE BETWEEN %s AND %s%n", from, to);
        System.out.printf ("  Avg per query: %.3f ms%n", q2WithoutAvg);
        System.out.printf ("  Total (%d x) : %.3f ms%n", ITERATIONS, toMs(t1 - t0));
        printExplainPlan("SELECT * FROM VACATION WHERE START_DATE BETWEEN '2025-06-15' AND '2025-06-17'");
    }

    @Test
    @Order(4)
    void benchmark2_dateRangeScan_WITH_index() {
        createIndexSafely("CREATE INDEX IDX_VACATION_START_DATE ON VACATION(START_DATE)");

        Date from = Date.valueOf(LocalDate.of(2025, 6, 15));
        Date to   = Date.valueOf(LocalDate.of(2025, 6, 17));

        String sql = "SELECT * FROM VACATION WHERE START_DATE BETWEEN ? AND ?";

        warmup(sql, from, to);

        long t0 = System.nanoTime();
        for (int i = 0; i < ITERATIONS; i++) jdbcTemplate.queryForList(sql, from, to);
        long t1 = System.nanoTime();

        q2WithAvg = toMs(t1 - t0) / ITERATIONS;

        System.out.println("\n[BENCHMARK 2 — narrow date-range scan]");
        System.out.println("  State        : WITH index IDX_VACATION_START_DATE(START_DATE)");
        System.out.printf ("  Query        : START_DATE BETWEEN %s AND %s%n", from, to);
        System.out.printf ("  Avg per query: %.3f ms%n", q2WithAvg);
        System.out.printf ("  Total (%d x) : %.3f ms%n", ITERATIONS, toMs(t1 - t0));
        if (q2WithoutAvg > 0)
            System.out.printf("  Speedup      : %.1fx faster%n", q2WithoutAvg / q2WithAvg);
        printExplainPlan("SELECT * FROM VACATION WHERE START_DATE BETWEEN '2025-06-15' AND '2025-06-17'");
    }

    // ── Summary ───────────────────────────────────────────────────────────

    @Test
    @Order(5)
    void printSummary() {
        separator();
        System.out.println("  BENCHMARK SUMMARY");
        separator();
        System.out.printf("  Dataset    : %,d vacation rows | %d unique employees%n", DATA_SIZE, EMPLOYEE_COUNT);
        System.out.printf("  Iterations : %d per measurement%n%n", ITERATIONS);

        System.out.println("  Index 1: IDX_VACATION_EMP_START  on (EMPLOYEE_ID, START_DATE)");
        System.out.println("  Query  : WHERE EMPLOYEE_ID = ? AND EXTRACT(YEAR FROM START_DATE) = ?");
        System.out.printf ("  Without: %.3f ms / query%n", q1WithoutAvg);
        System.out.printf ("  With   : %.3f ms / query%n", q1WithAvg);
        if (q1WithAvg > 0)
            System.out.printf("  Speedup: %.1fx  (index narrows to ~%d rows before year filter)%n",
                q1WithoutAvg / q1WithAvg, DATA_SIZE / EMPLOYEE_COUNT);

        System.out.println();
        System.out.println("  Index 2: IDX_VACATION_START_DATE on (START_DATE)");
        System.out.println("  Query  : WHERE START_DATE BETWEEN '2025-06-15' AND '2025-06-17'  (~0.14% selectivity)");
        System.out.printf ("  Without: %.3f ms / query  (full table scan of %,d rows)%n", q2WithoutAvg, DATA_SIZE);
        System.out.printf ("  With   : %.3f ms / query  (index range scan of ~%d rows)%n", q2WithAvg, DATA_SIZE / 730);
        if (q2WithAvg > 0)
            System.out.printf("  Speedup: %.1fx  (engine skips ~99.9%% of the table via the B-tree)%n",
                q2WithoutAvg / q2WithAvg);
        separator();
    }

    // ── Helpers ───────────────────────────────────────────────────────────

    private void warmup(String sql, Object... args) {
        for (int i = 0; i < 5; i++) jdbcTemplate.queryForList(sql, args);
    }

    private static double toMs(long nanos) {
        return nanos / 1_000_000.0;
    }

    private void printExplainPlan(String sql) {
        try {
            List<Map<String, Object>> rows = jdbcTemplate.queryForList("EXPLAIN " + sql);
            System.out.println("  Execution Plan:");
            for (Map<String, Object> row : rows) {
                row.values().forEach(v -> System.out.println("    " + v));
            }
        } catch (Exception e) {
            System.out.println("  (Could not retrieve plan: " + e.getMessage() + ")");
        }
    }

    private void dropIndexSafely(String name) {
        try {
            jdbcTemplate.execute("DROP INDEX IF EXISTS " + name);
        } catch (Exception ignored) { }
    }

    private void createIndexSafely(String ddl) {
        try {
            jdbcTemplate.execute(ddl);
            System.out.println("  Index created.");
        } catch (Exception e) {
            System.out.println("  Index already exists / creation failed: " + e.getMessage());
        }
    }

    private static void separator() {
        System.out.println("=============================================================");
    }
}
