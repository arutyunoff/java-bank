package timeapi;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.time.Duration;
import java.time.Instant;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class IncidentParserTest {
    @Test
    void parsesValidLine() {
        Incident incident = IncidentParser.parse("INC-1;billing;2024-03-30T22:30:00Z;2024-03-31T01:00:00Z");

        assertEquals("INC-1", incident.id());
        assertEquals("billing", incident.service());
        assertEquals(Instant.parse("2024-03-30T22:30:00Z"), incident.startedAt());
        assertEquals(Instant.parse("2024-03-31T01:00:00Z"), incident.resolvedAt());
        assertEquals(Duration.ofMinutes(150), incident.duration());
    }

    @Test
    void rejectsInvalidTimestamp() {
        assertThrows(IllegalArgumentException.class,
                () -> IncidentParser.parse("INC-1;billing;2024-03-30T22:30:00;2024-03-31T01:00:00Z"));
    }

    @Test
    void rejectsEmptyId() {
        assertThrows(IllegalArgumentException.class,
                () -> IncidentParser.parse(";billing;2024-03-30T22:30:00Z;2024-03-31T01:00:00Z"));
    }

    @Test
    void rejectsEndBeforeStart() {
        assertThrows(IllegalArgumentException.class,
                () -> IncidentParser.parse("INC-1;billing;2024-03-31T01:00:00Z;2024-03-30T22:30:00Z"));
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "INC-1;billing;2024-03-30T22:30:00Z",
            "INC-1;billing;2024-03-30T22:30:00Z;2024-03-31T01:00:00Z;extra"
    })
    void rejectsWrongFieldCount(String line) {
        assertThrows(IllegalArgumentException.class, () -> IncidentParser.parse(line));
    }
}
