package timeapi;

import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

class IncidentAnalyticsTest {
    private static final Incident INC_1 = new Incident("INC-1", "billing",
            Instant.parse("2024-03-30T22:30:00Z"), Instant.parse("2024-03-31T01:00:00Z"));
    private static final Incident INC_2 = new Incident("INC-2", "gateway",
            Instant.parse("2024-03-30T23:30:00Z"), Instant.parse("2024-03-31T00:15:00Z"));

    @Test
    void totalAndAverageDowntime() {
        List<Incident> arr = List.of(INC_1, INC_2);

        assertEquals(Duration.ofMinutes(195), IncidentAnalytics.totalDowntime(arr));
        assertEquals(Duration.ofMinutes(97).plusSeconds(30), IncidentAnalytics.averageDowntime(arr));
    }

    @Test
    void averageRoundsDown() {
        Instant start = Instant.parse("2024-03-30T00:00:00Z");
        List<Incident> arr = List.of(
                new Incident("A", "billing", start, start.plusNanos(1)),
                new Incident("B", "billing", start, start.plusNanos(2)));

        assertEquals(Duration.ofNanos(3), IncidentAnalytics.totalDowntime(arr));
        assertEquals(Duration.ofNanos(1), IncidentAnalytics.averageDowntime(arr));
    }

    @Test
    void findsLongestIncident() {
        assertEquals(Optional.of(INC_1), IncidentAnalytics.longestIncident(List.of(INC_2, INC_1)));
    }

    @Test
    void longestIncidentWithEqualDuration() {
        Instant start = Instant.parse("2024-03-30T10:00:00Z");
        Incident early = new Incident("B", "billing", start, start.plusSeconds(60));
        Incident late = new Incident("A", "billing", start.plusSeconds(10), start.plusSeconds(70));
        Incident sameStart = new Incident("C", "billing", start, start.plusSeconds(60));

        assertEquals(Optional.of(early), IncidentAnalytics.longestIncident(List.of(late, early)));
        assertEquals(Optional.of(early), IncidentAnalytics.longestIncident(List.of(sameStart, early)));
    }

    @Test
    void emptyList() {
        List<Incident> arr = List.of();

        assertEquals(Duration.ZERO, IncidentAnalytics.totalDowntime(arr));
        assertEquals(Duration.ZERO, IncidentAnalytics.averageDowntime(arr));
        assertEquals(Optional.empty(), IncidentAnalytics.longestIncident(arr));
    }

    @Test
    void groupsByLocalDateInBerlin() {
        Incident inc3 = new Incident("INC-3", "auth",
                Instant.parse("2024-03-31T05:00:00Z"), Instant.parse("2024-03-31T06:00:00Z"));
        LocalDate first = LocalDate.of(2024, 3, 30);
        LocalDate second = LocalDate.of(2024, 3, 31);

        Map<LocalDate, List<Incident>> res =
                IncidentAnalytics.groupByStartDate(List.of(inc3, INC_2, INC_1), ZoneId.of("Europe/Berlin"));

        assertEquals(first, LocalDate.ofInstant(INC_2.startedAt(), ZoneOffset.UTC));
        assertEquals(List.of(first, second), List.copyOf(res.keySet()));
        assertEquals(List.of(INC_1), res.get(first));
        assertEquals(List.of(INC_2, inc3), res.get(second));
    }
}
