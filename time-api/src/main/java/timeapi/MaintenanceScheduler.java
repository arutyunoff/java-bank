package timeapi;

import java.time.Clock;
import java.time.DateTimeException;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public final class MaintenanceScheduler {
    private final Clock clock;

    public MaintenanceScheduler(Clock clock) {
        this.clock = Objects.requireNonNull(clock, "clock is null");
    }

    public MaintenanceWindow nextWindow(LocalDate date, LocalTime localStart, Duration duration,
                                        ZoneId zone, OverlapPolicy policy) {
        Objects.requireNonNull(date, "date is null");
        checkArgs(localStart, duration, zone, policy);

        LocalDateTime local = LocalDateTime.of(date, localStart);
        if (zone.getRules().getValidOffsets(local).isEmpty()) {
            throw new DateTimeException("local time " + local + " does not exist in " + zone);
        }

        ZonedDateTime zoned = ZonedDateTime.of(local, zone);
        if (policy == OverlapPolicy.EARLIER) {
            zoned = zoned.withEarlierOffsetAtOverlap();
        } else {
            zoned = zoned.withLaterOffsetAtOverlap();
        }
        Instant start = zoned.toInstant();
        return new MaintenanceWindow(start, start.plus(duration));
    }

    public List<MaintenanceWindow> upcomingWindows(int days, LocalTime localStart, Duration duration,
                                                   ZoneId zone, OverlapPolicy policy) {
        if (days < 0) {
            throw new IllegalArgumentException("days must not be negative");
        }
        checkArgs(localStart, duration, zone, policy);

        LocalDate date = LocalDate.now(clock.withZone(zone));
        List<MaintenanceWindow> res = new ArrayList<>();
        for (int i = 0; i < days; i++) {
            res.add(nextWindow(date, localStart, duration, zone, policy));
            date = date.plusDays(1);
        }
        return res;
    }

    private static void checkArgs(LocalTime localStart, Duration duration, ZoneId zone, OverlapPolicy policy) {
        Objects.requireNonNull(localStart, "localStart is null");
        Objects.requireNonNull(duration, "duration is null");
        Objects.requireNonNull(zone, "zone is null");
        Objects.requireNonNull(policy, "policy is null");
        if (duration.isZero() || duration.isNegative()) {
            throw new IllegalArgumentException("duration must be positive");
        }
    }
}
