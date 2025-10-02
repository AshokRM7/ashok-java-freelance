package your.pkg;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

/** Entry point you run from IntelliJ. Set VM options as shared. */
public class Main {
    public static void main(String[] args) {
        // read VM options
        String driverPath = System.getProperty("webdriver.chrome.driver");
        String baseUrl   = System.getProperty("demo.url",
                "https://opensource-demo.orangehrmlive.com/web/index.php/auth/login");
        String user = System.getProperty("demo.user", "Admin");
        String pass = System.getProperty("demo.pass", "admin123");

        if (driverPath == null || driverPath.isBlank()) {
            throw new IllegalStateException("Set -Dwebdriver.chrome.driver=./chromedriver.exe in VM options.");
        }
        System.setProperty("webdriver.chrome.driver", driverPath);

        // raw driver + wrapper
        ChromeDriver raw = new ChromeDriver();
        HealingReporter reporter = new HealingReporter();
        SelfHealingWebDriver driver = new SelfHealingWebDriver(raw, reporter);

        try {
            driver.get(baseUrl);

            // --- self-healing locators
//            By username = HealingBy.key("login.username", By.name("username-broken"))
            By username = HealingBy.key("username", By.name("username-broken"))
                    .alts(By.cssSelector("input[name='username-broken']"),
                            By.xpath("//input[contains(@placeholder,'Username-broken')]")).threshold(0.60);

            By password = HealingBy.key("login.password", By.name("password"))
                    .alts(By.cssSelector("input[type='password']"),
                            By.xpath("//input[contains(@placeholder,'Password')]"));

            By loginBtn = HealingBy.key("login.submit", By.cssSelector("button[type='submit-broken']"))
                    .alts(By.xpath("//button[contains(.,'Login')]"),
                            By.xpath("//button[contains(.,'Sign in')]"));

            // --- actions with smart waits
            WebElement userEl = SmartWait.untilPresent(driver, username);
            userEl.sendKeys(user);

            WebElement passEl = SmartWait.untilPresent(driver, password);
            passEl.sendKeys(pass);

            WebElement btnEl = SmartWait.untilClickable(driver, loginBtn);
            btnEl.click();

            Thread.sleep(1500);  // small pause just for the demo
            driver.log("verify", "Page title after login: " + driver.getTitle(), null);

        } catch (Exception e) {
            driver.log("error", "Exception during run: " + e.getMessage(),
                    java.util.Map.of("ex", e.getClass().getSimpleName()));
            e.printStackTrace();
        } finally {
            driver.quit(); // also flushes the JSON report
        }
    }
}
