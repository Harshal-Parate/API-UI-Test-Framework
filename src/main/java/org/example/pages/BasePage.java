package org.example.pages;

import org.example.actionmanagement.SeleniumWrapper;
import org.example.drivermanager.DriverFactory;
import org.openqa.selenium.WebDriver;

public class BasePage {

    protected WebDriver driver;
    protected SeleniumWrapper wrapper;

    public BasePage() {
        this.driver = DriverFactory.getDriver();
        this.wrapper = new SeleniumWrapper(driver);
    }
}
