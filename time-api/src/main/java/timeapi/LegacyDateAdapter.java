package timeapi;

import java.time.Instant;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Objects;

public final class LegacyDateAdapter {
    private LegacyDateAdapter() {
    }

    public static Instant toInstant(Date date) {
        Objects.requireNonNull(date, "date is null");
        return date.toInstant();
    }

    public static Date toDate(Instant instant) {
        Objects.requireNonNull(instant, "instant is null");
        return Date.from(instant);
    }

    public static ZonedDateTime toZonedDateTime(GregorianCalendar calendar) {
        Objects.requireNonNull(calendar, "calendar is null");
        return calendar.toZonedDateTime();
    }

    public static GregorianCalendar toGregorianCalendar(ZonedDateTime dateTime) {
        Objects.requireNonNull(dateTime, "dateTime is null");
        return GregorianCalendar.from(dateTime);
    }
}
