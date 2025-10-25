package your.pkg;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

import java.io.IOException;
import java.nio.file.*;
import java.text.SimpleDateFormat;
import java.util.*;

public class HealingReporter {

    private final List<Map<String, Object>> events = new ArrayList<>();
    private final Path out;
    private final ObjectWriter writer = new ObjectMapper().writerWithDefaultPrettyPrinter();

    public HealingReporter() {
        // 🕒 Create timestamp with year, month, day, hour, minute, second, and milliseconds
        String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss_SSS").format(new Date());

        // Example: report-20251014_155927_944.json
        this.out = Paths.get("target", "selfheal", "report-" + timestamp + ".json");
    }

    public synchronized void log(String type, String message, Map<String, Object> extra) {
        Map<String, Object> e = new LinkedHashMap<>();
        e.put("ts", System.currentTimeMillis());
        e.put("type", type);
        e.put("message", message);
        if (extra != null) e.putAll(extra);
        events.add(e);
        System.out.println("[HEAL] " + type + " :: " + message);
    }

    public synchronized void flush() {
        try {
            Files.createDirectories(out.getParent());
            writer.writeValue(out.toFile(), events);
            System.out.println("[HEAL] Report: " + out.toAbsolutePath());
        } catch (IOException e) {
            System.err.println("[HEAL] Failed to write report: " + e.getMessage());
        }
    }
}
