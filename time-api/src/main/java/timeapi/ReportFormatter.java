package timeapi;

import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.Objects;

public final class ReportFormatter {
    private static final String PATTERN = "dd.MM.uuuu HH:mm XXX (VV)";

    private ReportFormatter() {
    }

    public static String formatWindow(MaintenanceWindow window, ZoneId zone, Locale locale) {
        Objects.requireNonNull(window, "window is null");
        Objects.requireNonNull(zone, "zone is null");
        Objects.requireNonNull(locale, "locale is null");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(PATTERN, locale);
        String start = formatter.format(window.start().atZone(zone));
        String end = formatter.format(window.end().atZone(zone));
        return start + " – " + end;
    }
}
