package your.pkg;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

import java.io.IOException;
import java.nio.file.*;
import java.util.*;

/** Collects healing/guard/wait events and writes them to JSON at quit(). */
public class HealingReporter {
    private final List<Map<String, Object>> events = new ArrayList<>();
    private final Path out = Paths.get("target", "selfheal",
            "report-" + System.currentTimeMillis() + ".json");
    private final ObjectWriter writer = new ObjectMapper().writerWithDefaultPrettyPrinter();

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
