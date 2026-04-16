package org.example.actionmanagement;

import org.example.locatormanagement.BaseLocators;
import org.example.utilities.*;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.*;

import io.qameta.allure.Allure;

import java.io.ByteArrayInputStream;
import java.time.Duration;

public class SeleniumWrapper {

    private final WebDriver driver;
    private final WaitUtils waitUtils;

    public SeleniumWrapper(WebDriver driver) {
        this.driver = driver;
        this.waitUtils = new WaitUtils(driver);
    }

    private By resolve(BaseLocators locator, Object... args) {
        String raw = locator.get(args);
        LoggerUtil.info("Locator → " + raw);
        return LocatorUtils.getBy(raw);
    }

    private WebElement wait(By by, WaitType type) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        switch (type) {
            case CLICKABLE:
                return wait.until(ExpectedConditions.elementToBeClickable(by));

            case VISIBLE:
                return wait.until(ExpectedConditions.visibilityOfElementLocated(by));

            case PRESENCE:
                return wait.until(ExpectedConditions.presenceOfElementLocated(by));

            default:
                throw new RuntimeException("Invalid wait type");
        }
    }

    enum WaitType {
        CLICKABLE, VISIBLE, PRESENCE
    }

    public void click(BaseLocators locator, Object... args) {
        By by = resolve(locator, args);
        try {
            wait(by, WaitType.CLICKABLE).click();
            LoggerUtil.info("Click (normal)");
            return;

        } catch (Exception e1) {
            LoggerUtil.warn("Normal click failed → JS");

            try {
                WebElement el = wait(by, WaitType.PRESENCE);
                ((JavascriptExecutor) driver)
                        .executeScript("arguments[0].click();", el);

                LoggerUtil.info("Click (JS)");
                return;

            } catch (Exception e2) {
                LoggerUtil.warn("failed → Actions");

                try {
                    WebElement el = wait(by, WaitType.VISIBLE);
                    new Actions(driver).moveToElement(el).click().perform();

                    LoggerUtil.info("Click (Actions)");
                    return;

                } catch (Exception e3) {
                    fail(locator, "Click failed");
                }
            }
        }
    }

    public void sendKeys(BaseLocators locator, String value, Object... args) {
        By by = resolve(locator, args);
        try {
            WebElement el = wait(by, WaitType.VISIBLE);
            el.clear();
            el.sendKeys(value);
            LoggerUtil.info("Typed: " + value);
        } catch (Exception e) {
            fail(locator, "SendKeys failed");
        }
    }

    public String getText(BaseLocators locator, Object... args) {
        By by = resolve(locator, args);
        try {
            return wait(by, WaitType.VISIBLE).getText();
        } catch (Exception e) {
            fail(locator, "GetText failed");
            return null;
        }
    }

    public String getAttribute(BaseLocators locator, String attr, Object... args) {
        By by = resolve(locator, args);
        try {
            return wait(by, WaitType.PRESENCE).getAttribute(attr);
        } catch (Exception e) {
            fail(locator, "GetAttribute failed");
            return null;
        }
    }

    public boolean isDisplayed(BaseLocators locator, Object... args) {
        By by = resolve(locator, args);
        try {
            return wait(by, WaitType.VISIBLE).isDisplayed();
        } catch (Exception e) {
            LoggerUtil.warn("Element not displayed");
            return false;
        }
    }

    public boolean waitForText(BaseLocators locator, String text, Object... args) {
        By by = resolve(locator, args);
        try {
            return new WebDriverWait(driver, Duration.ofSeconds(10))
                    .until(ExpectedConditions.textToBePresentInElementLocated(by, text));
        } catch (Exception e) {
            return false;
        }
    }

    public void selectByVisibleText(BaseLocators locator, String text, Object... args) {
        By by = resolve(locator, args);
        try {
            new Select(wait(by, WaitType.VISIBLE)).selectByVisibleText(text);

        } catch (Exception e) {
            fail(locator, "Dropdown failed");
        }
    }

    public void hover(BaseLocators locator, Object... args) {
        By by = resolve(locator, args);
        try {
            new Actions(driver)
                    .moveToElement(wait(by, WaitType.VISIBLE))
                    .perform();
        } catch (Exception e) {
            fail(locator, "Hover failed");
        }
    }

    public void doubleClick(BaseLocators locator, Object... args) {
        By by = resolve(locator, args);
        try {
            new Actions(driver)
                    .doubleClick(wait(by, WaitType.VISIBLE))
                    .perform();
        } catch (Exception e) {
            fail(locator, "Double click failed");
        }
    }

    public void rightClick(BaseLocators locator, Object... args) {
        By by = resolve(locator, args);
        try {
            new Actions(driver)
                    .contextClick(wait(by, WaitType.VISIBLE))
                    .perform();
        } catch (Exception e) {
            fail(locator, "Right click failed");
        }
    }

    public void scrollToElement(BaseLocators locator, Object... args) {
        By by = resolve(locator, args);
        try {
            WebElement el = wait(by, WaitType.PRESENCE);
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", el);
        } catch (Exception e) {
            fail(locator, "Scroll failed");
        }
    }

    private void fail(BaseLocators locator, String message) {
        LoggerUtil.error(" " + message + " → " + locator.getKey());
        Allure.addAttachment("Failure Screenshot", new ByteArrayInputStream(ScreenshotUtil.capture()));
        throw new RuntimeException(message + " → " + locator.getKey());
    }
}