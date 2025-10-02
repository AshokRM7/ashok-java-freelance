package your.pkg;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.util.List;

/** Dismisses common popups and retries the original action once. */
public class PopupGuard {

    private final SelfHealingWebDriver d;

    // add/remove nuisance selectors here for your app
    private final By[] nuisances = new By[]{
            By.cssSelector("button#accept, .cookie-accept, .cookie-consent-accept"),
            By.cssSelector(".modal .close, .popup-close, [aria-label='Close']")
    };

    public PopupGuard(SelfHealingWebDriver d) { this.d = d; }

    public <T> T withGuard(Action<T> action) {
        try {
            return action.run();
        } catch (Exception first) {
            if (sweep()) {
                d.log("popup-retry", "Retrying original action after dismiss", null);
                return action.run();
            }
            throw first;
        }
    }

    private boolean sweep() {
        boolean acted = false;
        for (By sel : nuisances) {
            List<WebElement> els = d.findElements(sel);
            for (WebElement e : els) {
                if (e.isDisplayed()) {
                    try { e.click(); acted = true; d.log("popup-dismiss", "Dismissed " + sel, null); }
                    catch (Exception ignore) {}
                }
            }
        }
        return acted;
    }

    @FunctionalInterface
    public interface Action<T> { T run(); }
}
