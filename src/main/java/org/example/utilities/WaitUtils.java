package org.example.utilities;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.*;

import java.time.Duration;

public class WaitUtils {

    private WebDriver driver;
    public WebDriverWait wait;

    public WaitUtils(WebDriver driver) {
        this.driver = driver;

        int timeout = Integer.parseInt(ConfigReader.get("explicitWait"));
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(timeout));
    }

    public WebElement waitForVisible(By locator) {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    public WebElement waitForClickable(By locator) {
        return wait.until(ExpectedConditions.elementToBeClickable(locator));
    }

    public boolean waitForInvisibility(By locator) {
        return wait.until(ExpectedConditions.invisibilityOfElementLocated(locator));
    }

    public WebElement waitForPresence(By locator) {
        return wait.until(ExpectedConditions.presenceOfElementLocated(locator));
    }
}