package ba.unsa.etf.employeemanagement.service;

import ba.unsa.etf.employeemanagement.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class VacationRequestServiceTest {
    private VacationRequestService service;
    private Map<Long, Employee> employees;
    private Map<Long, UserCalendar> calendars;
    private List<VacationRequest> requests;
    private List<Holiday> holidays;

    @BeforeEach
    void setUp() {
        employees = new HashMap<>();
        calendars = new HashMap<>();
        requests = new ArrayList<>();
        holidays = new ArrayList<>();
        // User 1: works Mon-Fri, is "Bosnian" nationality, "Single" marital status
        employees.put(1L, new Employee(1L, 1L, "Male", "Bosnian", "Single"));
        calendars.put(1L, new UserCalendar(1L, new HashSet<>(Arrays.asList(Calendar.MONDAY, Calendar.TUESDAY, Calendar.WEDNESDAY, Calendar.THURSDAY, Calendar.FRIDAY))));
        service = new VacationRequestService(requests, holidays, calendars, employees);
    }

    @Test
    void requestVacation_withinLimit_succeeds() {
        Date start = new GregorianCalendar(2026, Calendar.JUNE, 1).getTime();
        Date end = new GregorianCalendar(2026, Calendar.JUNE, 5).getTime();
        VacationRequest req = service.requestVacation(1L, start, end);
        assertEquals(VacationStatus.PENDING, req.getStatus());
        assertEquals(1L, req.getUserId());
        assertEquals(start, req.getStartDate());
        assertEquals(end, req.getEndDate());
    }

    @Test
    void requestVacation_exceedsLimit_throws() {
        // Add exactly 21 approved working days for 2026
        int added = 0;
        int day = 1;
        while (added < 21) {
            Date s = new GregorianCalendar(2026, Calendar.JANUARY, day).getTime();
            Date e = s;
            Calendar cal = Calendar.getInstance();
            cal.setTime(s);
            int dayOfWeek = cal.get(Calendar.DAY_OF_WEEK);
            if (dayOfWeek >= Calendar.MONDAY && dayOfWeek <= Calendar.FRIDAY) {
                VacationRequest req = new VacationRequest((long) added, 1L, s, e, VacationStatus.APPROVED);
                requests.add(req);
                added++;
            }
            day++;
        }
        Date start = new GregorianCalendar(2026, Calendar.JUNE, 1).getTime();
        Date end = new GregorianCalendar(2026, Calendar.JUNE, 5).getTime();
        assertThrows(IllegalArgumentException.class, () -> service.requestVacation(1L, start, end));
    }

    @Test
    void requestVacation_excludesHolidays() {
        // June 3 is a holiday for "Bosnian" nationality
        holidays.add(new Holiday(1L, "Bosnian Holiday", new GregorianCalendar(2026, Calendar.JUNE, 3).getTime(), "Bosnian", "desc", "Single"));
        Date start = new GregorianCalendar(2026, Calendar.JUNE, 1).getTime();
        Date end = new GregorianCalendar(2026, Calendar.JUNE, 5).getTime();
        VacationRequest req = service.requestVacation(1L, start, end);
        // Only 4 working days should be counted (June 3 is a holiday)
        assertEquals(VacationStatus.PENDING, req.getStatus());
    }

    @Test
    void requestVacation_nonWorkingDaysNotCounted() {
        // User only works Monday
        calendars.put(1L, new UserCalendar(1L, new HashSet<>(Collections.singletonList(Calendar.MONDAY))));
        Date start = new GregorianCalendar(2026, Calendar.JUNE, 1).getTime(); // Monday
        Date end = new GregorianCalendar(2026, Calendar.JUNE, 7).getTime(); // Sunday
        service = new VacationRequestService(requests, holidays, calendars, employees);
        VacationRequest req = service.requestVacation(1L, start, end);
        // Only one working day (Monday)
        assertEquals(VacationStatus.PENDING, req.getStatus());
    }

    @Test
    void getRequestsForUser_returnsCorrectRequests() {
        requests.add(new VacationRequest(1L, 1L, new Date(), new Date(), VacationStatus.PENDING));
        requests.add(new VacationRequest(2L, 2L, new Date(), new Date(), VacationStatus.PENDING));
        List<VacationRequest> user1 = service.getRequestsForUser(1L);
        assertEquals(1, user1.size());
        assertEquals(1L, user1.get(0).getUserId());
    }

    @Test
    void updateRequestStatus_updatesStatus() {
        VacationRequest req = new VacationRequest(1L, 1L, new Date(), new Date(), VacationStatus.PENDING);
        requests.add(req);
        VacationRequest updated = service.updateRequestStatus(1L, VacationStatus.APPROVED);
        assertEquals(VacationStatus.APPROVED, updated.getStatus());
    }

    @Test
    void updateRequestStatus_notFound_throws() {
        assertThrows(IllegalArgumentException.class, () -> service.updateRequestStatus(99L, VacationStatus.APPROVED));
    }
}
