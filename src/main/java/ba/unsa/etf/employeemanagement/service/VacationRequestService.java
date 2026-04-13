package ba.unsa.etf.employeemanagement.service;

import ba.unsa.etf.employeemanagement.model.*;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class VacationRequestService {
    private static final int MAX_VACATION_DAYS_PER_YEAR = 21;

    // These would be injected/retrieved from repositories in a real app
    private List<VacationRequest> vacationRequests = new ArrayList<>();
    private List<Holiday> holidays = new ArrayList<>();
    private Map<Long, UserCalendar> userCalendars = new HashMap<>();
    private Map<Long, Employee> employees = new HashMap<>();

    public VacationRequestService(List<VacationRequest> vacationRequests, List<Holiday> holidays, Map<Long, UserCalendar> userCalendars, Map<Long, Employee> employees) {
        this.vacationRequests = vacationRequests;
        this.holidays = holidays;
        this.userCalendars = userCalendars;
        this.employees = employees;
    }

    public VacationRequest requestVacation(Long userId, Date startDate, Date endDate) {
        Employee employee = employees.get(userId);
        if (employee == null) throw new IllegalArgumentException("User not found");
        UserCalendar calendar = userCalendars.get(userId);
        if (calendar == null) throw new IllegalArgumentException("User calendar not found");
        int requestedDays = calculateWorkingVacationDays(userId, startDate, endDate, employee, calendar);
        int usedDays = getUsedVacationDaysThisYear(userId, startDate);
        // Only count requests that are not PENDING or REJECTED for the limit
        if (usedDays + requestedDays > MAX_VACATION_DAYS_PER_YEAR) {
            throw new IllegalArgumentException("Vacation limit exceeded");
        }
        VacationRequest request = new VacationRequest((long) (vacationRequests.size() + 1), userId, startDate, endDate, VacationStatus.PENDING);
        vacationRequests.add(request);
        return request;
    }

    private int calculateWorkingVacationDays(Long userId, Date start, Date end, Employee employee, UserCalendar calendar) {
        Calendar c = Calendar.getInstance();
        c.setTime(start);
        int days = 0;
        while (!c.getTime().after(end)) {
            int dayOfWeek = c.get(Calendar.DAY_OF_WEEK);
            if (calendar.getWorkingDays().contains(dayOfWeek)
                && !isUserHoliday(employee, c.getTime())) {
                days++;
            }
            c.add(Calendar.DATE, 1);
        }
        return days;
    }

    private boolean isUserHoliday(Employee employee, Date date) {
        for (Holiday h : holidays) {
            if (h.getHolidayDate().equals(date)) {
                if ((h.getReligion() != null && h.getReligion().equals(employee.getMaritalStatus())) ||
                    (h.getCountry() != null && h.getCountry().equals(employee.getNationality()))) {
                    return true;
                }
            }
        }
        return false;
    }

    private int getUsedVacationDaysThisYear(Long userId, Date referenceDate) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(referenceDate);
        int year = cal.get(Calendar.YEAR);
        int used = 0;
        for (VacationRequest req : vacationRequests) {
            cal.setTime(req.getStartDate());
            int reqYear = cal.get(Calendar.YEAR);
            if (req.getUserId().equals(userId) && reqYear == year && req.getStatus() == VacationStatus.APPROVED) {
                used += calculateWorkingVacationDays(userId, req.getStartDate(), req.getEndDate(), employees.get(userId), userCalendars.get(userId));
            }
        }
        return used;
    }

    public List<VacationRequest> getRequestsForUser(Long userId) {
        List<VacationRequest> result = new ArrayList<>();
        for (VacationRequest req : vacationRequests) {
            if (req.getUserId().equals(userId)) {
                result.add(req);
            }
        }
        return result;
    }

    public VacationRequest updateRequestStatus(Long requestId, VacationStatus status) {
        for (VacationRequest req : vacationRequests) {
            if (req.getId() != null && req.getId().equals(requestId)) {
                req.setStatus(status);
                return req;
            }
        }
        throw new IllegalArgumentException("Request not found");
    }
}
