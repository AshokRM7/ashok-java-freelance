package your.pkg;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.Map;
import java.util.Set;

/** Light wrapper that keeps itself as the SearchContext (important for HealingBy). */
public class SelfHealingWebDriver implements WebDriver, JavascriptExecutor {

    private final WebDriver delegate;
    private final HealingReporter reporter;

    public SelfHealingWebDriver(WebDriver raw, HealingReporter reporter) {
        this.delegate = raw;
        this.reporter = reporter;
    }

    public void log(String type, String message, Map<String, Object> extra) {
        reporter.log(type, message, extra);
    }

    public HealingReporter reporter() { return reporter; }

    // --- delegate, but make sure By.findElement receives *this* as SearchContext:
    @Override

    public WebElement findElement(By by) {
        if (by instanceof HealingBy) {
            // let HealingBy drive the healing process, using this wrapper
            return ((HealingBy) by).findElement(this);
        }
        // normal case: just delegate to raw driver
        return delegate.findElement(by);
    }





    @Override
    public java.util.List<WebElement> findElements(By by) {
        if (by instanceof HealingBy) {
            return ((HealingBy) by).findElements(this);
        }
        return delegate.findElements(by);
    }

    @Override public void get(String url) { delegate.get(url); }
    @Override public String getCurrentUrl() { return delegate.getCurrentUrl(); }
    @Override public String getTitle() { return delegate.getTitle(); }
    @Override public String getPageSource() { return delegate.getPageSource(); }
    @Override public void close() { delegate.close(); }

    @Override
    public void quit() {
        try { reporter.flush(); }
        finally { delegate.quit(); }
    }

    @Override public Set<String> getWindowHandles() { return delegate.getWindowHandles(); }
    @Override public String getWindowHandle() { return delegate.getWindowHandle(); }
    @Override public TargetLocator switchTo() { return delegate.switchTo(); }
    @Override public Navigation navigate() { return delegate.navigate(); }
    @Override public Options manage() { return delegate.manage(); }

    @Override
    public Object executeScript(String script, Object... args) {
        return ((JavascriptExecutor) delegate).executeScript(script, args);
    }

    @Override
    public Object executeAsyncScript(String script, Object... args) {
        return ((JavascriptExecutor) delegate).executeAsyncScript(script, args);
    }
}
