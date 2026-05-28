package ba.unsa.etf.employeemanagement.util;

import ba.unsa.etf.employeemanagement.exceptions.BadRequestException;
import org.springframework.dao.DataAccessException;

import java.sql.SQLException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Maps Oracle user-defined errors (ORA-20000..20999) from PL/SQL packages to API-friendly exceptions.
 */
public final class OraclePlsqlErrors {

    private static final Pattern ORA_USER_ERROR = Pattern.compile("ORA-20\\d{3}:\\s*([^\\r\\n]+)");

    private OraclePlsqlErrors() {
    }

    public static RuntimeException translate(DataAccessException ex) {
        SQLException plsqlError = findPlsqlError(ex);
        if (plsqlError != null) {
            return new BadRequestException(extractMessage(plsqlError));
        }
        return ex;
    }

    private static SQLException findPlsqlError(Throwable throwable) {
        Throwable current = throwable;
        while (current != null) {
            if (current instanceof SQLException sql) {
                if (isUserDefinedError(sql) || containsUserDefinedOraMessage(sql)) {
                    return sql;
                }
            }
            current = current.getCause();
        }
        return null;
    }

    static boolean isUserDefinedError(SQLException sql) {
        int code = Math.abs(sql.getErrorCode());
        return code >= 20000 && code <= 20999;
    }

    private static boolean containsUserDefinedOraMessage(SQLException sql) {
        String message = sql.getMessage();
        return message != null && ORA_USER_ERROR.matcher(message).find();
    }

    private static String extractMessage(SQLException sql) {
        String message = sql.getMessage();
        if (message == null) {
            return "Database package error";
        }

        Matcher matcher = ORA_USER_ERROR.matcher(message);
        if (matcher.find()) {
            return matcher.group(1).trim();
        }

        int idx = message.indexOf(": ");
        if (idx >= 0 && idx + 2 < message.length()) {
            return message.substring(idx + 2).trim();
        }
        return message.trim();
    }
}
