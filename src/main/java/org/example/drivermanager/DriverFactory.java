package org.example.drivermanager;

import org.example.utilities.ConfigReader;
import org.example.utilities.LoggerUtil;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.net.URL;
import java.time.Duration;

public class DriverFactory {

    private static final ThreadLocal<WebDriver> driver = new ThreadLocal<>();

    public static WebDriver initDriver() {

        String browser = getConfig("browser", "chrome");
        String execution = getConfig("execution", "local");
        boolean headless = Boolean.parseBoolean(getConfig("headless", "false"));
        String gridUrl = getConfig("gridUrl", "http://localhost:4444/wd/hub");

        LoggerUtil.info("Execution Mode: " + execution);
        LoggerUtil.info("Browser: " + browser);
        LoggerUtil.info("Headless: " + headless);

        WebDriver webDriver;

        if ("remote".equalsIgnoreCase(execution)) {
            webDriver = createRemoteDriver(browser, headless, gridUrl);
        } else {
            webDriver = createLocalDriver(browser, headless);
        }

        driver.set(webDriver);
        applyTimeouts(webDriver);
        return getDriver();
    }

    private static String getConfig(String key, String defaultValue) {
        return System.getProperty(key, safeGetFromConfig(key, defaultValue));
    }

    private static String safeGetFromConfig(String key, String defaultValue) {
        try {
            String value = ConfigReader.get(key);
            return value.isEmpty() ? defaultValue : value;
        } catch (Exception e) {
            return defaultValue;
        }
    }

    private static WebDriver createLocalDriver(String browser, boolean headless) {
        switch (browser.toLowerCase()) {
            case "chrome":
                return new ChromeDriver(getChromeOptions(headless));
            case "firefox":
                return new FirefoxDriver(getFirefoxOptions(headless));
            default:
                throw new RuntimeException("Unsupported browser: " + browser);
        }
    }

    private static WebDriver createRemoteDriver(String browser, boolean headless, String gridUrl) {
        try {
            return switch (browser.toLowerCase()) {
                case "chrome" -> new RemoteWebDriver(new URL(gridUrl), getChromeOptions(headless)
                );
                case "firefox" -> new RemoteWebDriver(new URL(gridUrl), getFirefoxOptions(headless)
                );
                default -> throw new RuntimeException("Unsupported remote browser: " + browser);
            };
        } catch (Exception e) {
            throw new RuntimeException("Failed to connect to Selenium Grid: " + gridUrl, e);
        }
    }

    private static ChromeOptions getChromeOptions(boolean headless) {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--disable-notifications");
        options.addArguments("--start-maximized");
        if (headless) {
            options.addArguments("--headless=new");
        }
        return options;
    }

    private static FirefoxOptions getFirefoxOptions(boolean headless) {
        FirefoxOptions options = new FirefoxOptions();
        if (headless) {
            options.addArguments("-headless");
        }
        return options;
    }

    private static void applyTimeouts(WebDriver driver) {
        try {
            int implicitWait = Integer.parseInt(getConfig("implicitWait", "3"));
            int pageLoadTimeout = Integer.parseInt(getConfig("pageLoadTimeout", "20"));
            int scriptTimeout = Integer.parseInt(getConfig("scriptTimeout", "10"));

            driver.manage().window().maximize();

            driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(implicitWait));
            driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(pageLoadTimeout));
            driver.manage().timeouts().scriptTimeout(Duration.ofSeconds(scriptTimeout));

            LoggerUtil.info("Timeouts configured successfully");
        } catch (NumberFormatException e) {
            LoggerUtil.error("Invalid timeout configuration: " + e.getMessage());
            throw new RuntimeException("Failed to configure timeouts", e);
        }
    }

    public static WebDriver getDriver() {
        return driver.get();
    }

    public static void quitDriver() {
        if (driver.get() != null) {
            try {
                driver.get().quit();
                LoggerUtil.info("Driver quit successfully");
            } catch (Exception e) {
                LoggerUtil.error("Error while quitting driver: " + e.getMessage());
            } finally {
                driver.remove();
            }
        }
    }
}