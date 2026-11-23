package timeapi;

import java.time.Duration;
import java.time.Instant;
import java.util.Objects;

public record Incident(String id, String service, Instant startedAt, Instant resolvedAt) {
    public Incident {
        Objects.requireNonNull(id, "id is null");
        Objects.requireNonNull(service, "service is null");
        Objects.requireNonNull(startedAt, "startedAt is null");
        Objects.requireNonNull(resolvedAt, "resolvedAt is null");
        if (id.isBlank()) {
            throw new IllegalArgumentException("id is empty");
        }
        if (service.isBlank()) {
            throw new IllegalArgumentException("service is empty");
        }
        if (resolvedAt.isBefore(startedAt)) {
            throw new IllegalArgumentException("resolvedAt is before startedAt");
        }
    }

    public Duration duration() {
        return Duration.between(startedAt, resolvedAt);
    }
}
