package timeapi;

import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.GregorianCalendar;

import static org.junit.jupiter.api.Assertions.assertEquals;

class LegacyDateAdapterTest {
    @Test
    void dateRoundTrip() {
        Instant instant = Instant.parse("2024-03-31T01:00:00.123Z");

        Date date = LegacyDateAdapter.toDate(instant);

        assertEquals(instant.toEpochMilli(), date.getTime());
        assertEquals(instant, LegacyDateAdapter.toInstant(date));
        assertEquals(date, LegacyDateAdapter.toDate(LegacyDateAdapter.toInstant(date)));
    }

    @Test
    void calendarRoundTrip() {
        ZonedDateTime dateTime = ZonedDateTime.of(2024, 10, 27, 2, 30, 0, 0, ZoneId.of("Europe/Berlin"))
                .withLaterOffsetAtOverlap();

        GregorianCalendar calendar = LegacyDateAdapter.toGregorianCalendar(dateTime);

        assertEquals(dateTime.toInstant().toEpochMilli(), calendar.getTimeInMillis());
        assertEquals("Europe/Berlin", calendar.getTimeZone().getID());
        assertEquals(dateTime, LegacyDateAdapter.toZonedDateTime(calendar));
    }
}
