package your.pkg;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

/** Small adaptive waits helper used by Main. */
public class SmartWait {

    /** First tries presence (5s), then escalates to visible (10s). */
    public static WebElement untilPresent(WebDriver driver, By locator) {
        try {
            return new WebDriverWait(driver, Duration.ofSeconds(5))
                    .until(ExpectedConditions.presenceOfElementLocated(locator));
        } catch (TimeoutException e) {
            if (driver instanceof SelfHealingWebDriver) {
                ((SelfHealingWebDriver) driver).log("wait-escalate", "presence→visible", null);
            }
            return new WebDriverWait(driver, Duration.ofSeconds(10))
                    .until(ExpectedConditions.visibilityOfElementLocated(locator));
        }
    }

    /** First tries clickable (5s), then escalates to visible (10s). */
    public static WebElement untilClickable(WebDriver driver, By locator) {
        try {
            return new WebDriverWait(driver, Duration.ofSeconds(5))
                    .until(ExpectedConditions.elementToBeClickable(locator));
        } catch (TimeoutException e) {
            if (driver instanceof SelfHealingWebDriver) {
                ((SelfHealingWebDriver) driver).log("wait-escalate", "clickable→visible", null);
            }
            return new WebDriverWait(driver, Duration.ofSeconds(10))
                    .until(ExpectedConditions.visibilityOfElementLocated(locator));
        }
    }
}
