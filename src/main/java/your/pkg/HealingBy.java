package your.pkg;

import org.apache.commons.text.similarity.JaroWinklerSimilarity;
import org.openqa.selenium.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Resilient locator:
 * 1) Try primary
 * 2) Try fallbacks
 * 3) Fuzzy auto-heal by scanning attributes/text
 */
public class HealingBy extends By {

    private final String key;
    private final By primary;
    private final List<By> fallbacks = new ArrayList<>();
    private double threshold = 0.82; // similarity cutoff

    private HealingBy(String key, By primary) {
        this.key = key;
        this.primary = primary;
    }

    public static HealingBy key(String logicalKey, By primary) {
        return new HealingBy(logicalKey, primary);
    }

    public HealingBy alts(By... alts) {
        for (By b : alts) fallbacks.add(b);
        return this;
    }

    public HealingBy threshold(double t) {
        this.threshold = t;
        return this;
    }

    @Override
    public WebElement findElement(SearchContext context) {
        // 1) primary
        try {
            return primary.findElement(context);
        } catch (NoSuchElementException ignore) {}

        // 2) fallbacks
        for (By fb : fallbacks) {
            try {
                WebElement el = fb.findElement(context);
                log(context, "locator-fallback",
                        key + " → " + fb, java.util.Map.of("primary", primary.toString()));
                return el;
            } catch (NoSuchElementException ignore) {}
        }

        // 3) fuzzy auto-heal
        List<WebElement> cands = context.findElements(By.cssSelector(
                "[id],[name],[data-testid],[aria-label],[placeholder],button,a,input,select,textarea"));
        var sim = new JaroWinklerSimilarity();
        WebElement best = null; double bestScore = -1.0; String bestSig = "";

        for (WebElement el : cands) {
            try {
                String sig = signature(el).toLowerCase();
                double s = sim.apply(key.toLowerCase(), sig);
                if (s > bestScore) { bestScore = s; best = el; bestSig = sig; }
            } catch (Exception ignored) {}
        }

        if (best != null && bestScore >= threshold) {
            log(context, "locator-autoheal",
                    key + " ~ " + shortSig(best),
                    java.util.Map.of("score", bestScore, "primary", primary.toString(), "sig", bestSig));
            return best;
        }
        throw new NoSuchElementException("Healing failed for key=" + key + " primary=" + primary);
    }

    @Override
    public List<WebElement> findElements(SearchContext context) {
        try {
            WebElement one = findElement(context);
            return java.util.List.of(one);
        } catch (NoSuchElementException e) {
            return java.util.List.of();
        }
    }

    // ---- helpers

    /** Log only when the SearchContext is our wrapper (it will be, thanks to SelfHealingWebDriver.findElement). */
    private static void log(SearchContext ctx, String type, String msg, java.util.Map<String, Object> extra) {
        if (ctx instanceof SelfHealingWebDriver) {
            ((SelfHealingWebDriver) ctx).log(type, msg, extra);
        }
        // else: no-op (we don't try to unwrap WebElement → driver)
    }

    private static String signature(WebElement el) {
        StringBuilder sb = new StringBuilder();
        try { sb.append(nz(el.getTagName())).append(" "); } catch (Exception ignore) {}
        String[] attrs = {"id","name","data-testid","aria-label","placeholder","class","type","role"};
        for (String a : attrs) {
            try {
                String v = el.getAttribute(a);
                if (v != null && !v.isEmpty()) sb.append(v).append(" ");
            } catch (Exception ignore) {}
        }
        try {
            String txt = el.getText();
            if (txt != null && !txt.isEmpty()) sb.append(txt);
        } catch (Exception ignore) {}
        return sb.toString().trim();
    }

    private static String shortSig(WebElement el) {
        try { return "tag=" + el.getTagName() + ", id=" + el.getAttribute("id"); }
        catch (Exception e) { return "element"; }
    }

    private static String nz(String s) { return s == null ? "" : s; }
}
