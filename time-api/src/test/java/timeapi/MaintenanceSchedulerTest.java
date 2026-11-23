package timeapi;

import org.junit.jupiter.api.Test;

import java.time.Clock;
import java.time.DateTimeException;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class MaintenanceSchedulerTest {
    private static final ZoneId BERLIN = ZoneId.of("Europe/Berlin");

    private final Clock clock = Clock.fixed(Instant.parse("2024-03-30T00:00:00Z"), ZoneOffset.UTC);
    private final MaintenanceScheduler scheduler = new MaintenanceScheduler(clock);

    @Test
    void rejectsTimeInGap() {
        assertThrows(DateTimeException.class, () -> scheduler.nextWindow(
                LocalDate.of(2024, 3, 31), LocalTime.of(2, 30), Duration.ofMinutes(30),
                BERLIN, OverlapPolicy.EARLIER));
    }

    @Test
    void overlapUsesPolicy() {
        LocalDate date = LocalDate.of(2024, 10, 27);
        LocalTime time = LocalTime.of(2, 30);

        MaintenanceWindow earlier = scheduler.nextWindow(date, time, Duration.ofMinutes(30),
                BERLIN, OverlapPolicy.EARLIER);
        MaintenanceWindow later = scheduler.nextWindow(date, time, Duration.ofMinutes(30),
                BERLIN, OverlapPolicy.LATER);

        assertEquals(time, earlier.start().atZone(BERLIN).toLocalTime());
        assertEquals(time, later.start().atZone(BERLIN).toLocalTime());
        assertEquals(ZoneOffset.ofHours(2), earlier.start().atZone(BERLIN).getOffset());
        assertEquals(ZoneOffset.ofHours(1), later.start().atZone(BERLIN).getOffset());
        assertEquals(Duration.ofHours(1), Duration.between(earlier.start(), later.start()));
    }

    @Test
    void dailyWindowsAcrossDstStart() {
        List<MaintenanceWindow> res = scheduler.upcomingWindows(2, LocalTime.of(3, 30),
                Duration.ofMinutes(30), BERLIN, OverlapPolicy.EARLIER);

        assertEquals(2, res.size());
        ZonedDateTime first = res.get(0).start().atZone(BERLIN);
        ZonedDateTime second = res.get(1).start().atZone(BERLIN);
        assertEquals(LocalDateTime.of(2024, 3, 30, 3, 30), first.toLocalDateTime());
        assertEquals(LocalDateTime.of(2024, 3, 31, 3, 30), second.toLocalDateTime());
        assertEquals(Duration.ofHours(23), Duration.between(res.get(0).start(), res.get(1).start()));
        assertEquals(Duration.ofMinutes(30), res.get(0).duration());

        assertEquals(res, scheduler.upcomingWindows(2, LocalTime.of(3, 30),
                Duration.ofMinutes(30), BERLIN, OverlapPolicy.EARLIER));
    }

    @Test
    void zeroAndNegativeDays() {
        assertEquals(List.of(), scheduler.upcomingWindows(0, LocalTime.of(3, 30),
                Duration.ofMinutes(30), BERLIN, OverlapPolicy.EARLIER));
        assertThrows(IllegalArgumentException.class, () -> scheduler.upcomingWindows(-1, LocalTime.of(3, 30),
                Duration.ofMinutes(30), BERLIN, OverlapPolicy.EARLIER));
    }

    @Test
    void rejectsNonPositiveDuration() {
        assertThrows(IllegalArgumentException.class, () -> scheduler.nextWindow(
                LocalDate.of(2024, 3, 30), LocalTime.of(3, 30), Duration.ZERO,
                BERLIN, OverlapPolicy.EARLIER));
    }
}
