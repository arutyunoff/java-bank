package timeapi;

import java.time.Duration;
import java.time.Instant;
import java.util.Objects;

public record MaintenanceWindow(Instant start, Instant end) {
    public MaintenanceWindow {
        Objects.requireNonNull(start, "start is null");
        Objects.requireNonNull(end, "end is null");
        if (end.isBefore(start)) {
            throw new IllegalArgumentException("end is before start");
        }
    }

    public Duration duration() {
        return Duration.between(start, end);
    }
}
