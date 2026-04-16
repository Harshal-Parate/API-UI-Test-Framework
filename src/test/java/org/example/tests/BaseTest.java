package org.example.tests;

import org.example.drivermanager.DriverFactory;
import org.example.utilities.ConfigReader;
import org.example.utilities.LoggerUtil;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

public class BaseTest {

    @BeforeMethod
    public void setup() {
        LoggerUtil.info("Starting Test Setup");
        DriverFactory.initDriver();
        String url = ConfigReader.get("baseUrl");
        DriverFactory.getDriver().get(url);
        LoggerUtil.info("Navigated to: " + url);
    }

    @AfterMethod
    public void tearDown() {
        LoggerUtil.info("Cleaning up test");
        DriverFactory.quitDriver();
    }
}