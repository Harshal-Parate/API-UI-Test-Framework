package org.example.utilities;

import org.openqa.selenium.By;

public class LocatorUtils {

    public static By getBy(String locator) {

        if (locator.startsWith("id=")) {
            return By.id(locator.replace("id=", ""));
        } else if (locator.startsWith("xpath=")) {
            return By.xpath(locator.replace("xpath=", ""));
        } else if (locator.startsWith("css=")) {
            return By.cssSelector(locator.replace("css=", ""));
        } else if (locator.startsWith("name=")) {
            return By.name(locator.replace("name=", ""));
        }

        throw new RuntimeException("Invalid locator: " + locator);
    }
}