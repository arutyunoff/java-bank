package timeapi;

import java.time.Instant;
import java.time.format.DateTimeParseException;
import java.util.Objects;

public final class IncidentParser {
    private IncidentParser() {
    }

    public static Incident parse(String line) {
        Objects.requireNonNull(line, "line is null");
        String[] parts = line.split(";", -1);
        if (parts.length != 4) {
            throw new IllegalArgumentException("expected 4 fields, got " + parts.length + ": " + line);
        }
        for (String part : parts) {
            if (part.isBlank()) {
                throw new IllegalArgumentException("empty field in line: " + line);
            }
        }
        Instant startedAt = parseInstant(parts[2], "startedAt");
        Instant resolvedAt = parseInstant(parts[3], "resolvedAt");
        return new Incident(parts[0], parts[1], startedAt, resolvedAt);
    }

    private static Instant parseInstant(String value, String name) {
        try {
            return Instant.parse(value);
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("invalid " + name + ": " + value, e);
        }
    }
}
