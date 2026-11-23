package timeapi;

import java.time.Duration;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.TreeMap;

public final class IncidentAnalytics {
    private IncidentAnalytics() {
    }

    public static Duration totalDowntime(List<Incident> incidents) {
        Objects.requireNonNull(incidents, "incidents is null");
        Duration total = Duration.ZERO;
        for (Incident incident : incidents) {
            total = total.plus(incident.duration());
        }
        return total;
    }

    public static Duration averageDowntime(List<Incident> incidents) {
        Objects.requireNonNull(incidents, "incidents is null");
        if (incidents.isEmpty()) {
            return Duration.ZERO;
        }
        return totalDowntime(incidents).dividedBy(incidents.size());
    }

    public static Optional<Incident> longestIncident(List<Incident> incidents) {
        Objects.requireNonNull(incidents, "incidents is null");
        Incident res = null;
        for (Incident incident : incidents) {
            if (res == null || isLonger(incident, res)) {
                res = incident;
            }
        }
        return Optional.ofNullable(res);
    }

    public static Map<LocalDate, List<Incident>> groupByStartDate(List<Incident> incidents, ZoneId zone) {
        Objects.requireNonNull(incidents, "incidents is null");
        Objects.requireNonNull(zone, "zone is null");
        List<Incident> sorted = new ArrayList<>(incidents);
        sorted.sort(Comparator.comparing(Incident::startedAt).thenComparing(Incident::id));
        Map<LocalDate, List<Incident>> res = new TreeMap<>();
        for (Incident incident : sorted) {
            LocalDate date = LocalDate.ofInstant(incident.startedAt(), zone);
            if (!res.containsKey(date)) {
                res.put(date, new ArrayList<>());
            }
            res.get(date).add(incident);
        }
        return res;
    }

    private static boolean isLonger(Incident a, Incident b) {
        int cmp = a.duration().compareTo(b.duration());
        if (cmp != 0) {
            return cmp > 0;
        }
        cmp = a.startedAt().compareTo(b.startedAt());
        if (cmp != 0) {
            return cmp < 0;
        }
        return a.id().compareTo(b.id()) < 0;
    }
}
