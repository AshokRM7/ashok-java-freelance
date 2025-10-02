# Self-Healing Selenium Prototype (Java + Maven)

This is a **drop-in, runnable** prototype that demonstrates:
- **Locator self-healing** (fallback + fuzzy matching)
- **Smart waits** (presence→visible→clickable with logging)
- **Popup guard** (auto-dismiss nuisances and retry once)
- **JSON report** of healing events (written with Jackson)
- Pure `main()` runner — **no TestNG/JUnit/WebDriverManager** needed

## 1) Prerequisites
- Java 17+
- Maven 3.8+
- A matching **ChromeDriver** for your Chrome version
  - Put it at the project root (recommended for demo): `./chromedriver` (Linux/macOS) or `./chromedriver.exe` (Windows)
  - Make it executable on Linux/macOS: `chmod +x ./chromedriver`

## 2) Run
### Option A — put chromedriver in project root and run:
```bash
# Linux/macOS
mvn -Dwebdriver.chrome.driver=./chromedriver     -Ddemo.url="https://opensource-demo.orangehrmlive.com/web/index.php/auth/login"     -Ddemo.user="Admin" -Ddemo.pass="admin123"     -DskipTests exec:java

# Windows (PowerShell)
mvn -Dwebdriver.chrome.driver=./chromedriver.exe `
    -Ddemo.url="https://opensource-demo.orangehrmlive.com/web/index.php/auth/login" `
    -Ddemo.user="Admin" -Ddemo.pass="admin123" `
    -DskipTests exec:java
```

### Option B — Use environment variable
```bash
# Windows
setx WEBDRIVER_CHROME_DRIVER "C:\path\to\chromedriver.exe"
# Linux/macOS
export WEBDRIVER_CHROME_DRIVER=/opt/drivers/chromedriver
```

Then run without the property:
```bash
mvn -DskipTests exec:java
```

## 3) What to look for
- Console logs like `locator-fallback`, `locator-autoheal`, `wait-escalate`, `popup-dismiss`, `popup-retry`.
- JSON report at: `target/selfheal/report-<timestamp>.json`

## 4) How to demo healing
1. Run once to see a successful login.
2. Manually **break** a locator in `Main.java` (e.g., change the primary `By` to an invalid one).
3. Run again and watch the framework use **fallbacks** or **fuzzy auto-heal** to find the element anyway.

## 5) Where to extend
- Add more nuisance selectors in `PopupGuard`.
- Add stricter/looser fuzzy threshold in `HealingBy.threshold(...)`.
- Wire into your framework's driver factory and step methods.
