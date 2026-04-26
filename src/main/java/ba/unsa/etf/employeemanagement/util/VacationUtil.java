package ba.unsa.etf.employeemanagement.util;

import java.util.Calendar;

public class VacationUtil {


    public static int getYear(java.util.Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal.get(Calendar.YEAR);
    }

    public static int daysBetween(java.util.Date start, java.util.Date end) {
        long diff = end.getTime() - start.getTime();
        return (int) (diff / (1000 * 60 * 60 * 24));
    }
}
