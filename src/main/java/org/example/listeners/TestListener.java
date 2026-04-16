package org.example.listeners;

import io.qameta.allure.Allure;
import org.example.drivermanager.DevToolsManager;
import org.example.drivermanager.DriverFactory;
import org.example.utilities.BrowserUtils;
import org.example.utilities.LoggerUtil;
import org.example.utilities.ScreenshotUtil;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

import java.io.ByteArrayInputStream;
import java.util.Objects;

public class TestListener implements ITestListener {

    @Override
    public void onTestStart(ITestResult result) {
        LoggerUtil.info("Test Started: " + result.getName());
        DevToolsManager.clearLogs();
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        LoggerUtil.info("Test Passed: " + result.getName());
    }

    @Override
    public void onTestFailure(ITestResult result) {

        LoggerUtil.error("Test Failed: " + result.getName());
        LoggerUtil.error("Reason: " + result.getThrowable());

        Allure.addAttachment("Screenshot", new ByteArrayInputStream(ScreenshotUtil.capture()));

        String browserLogs = BrowserUtils.getBrowserLogs();
        Allure.addAttachment("Browser Logs", browserLogs);

        String networkLogs = DevToolsManager.getLogs();
        if (!networkLogs.isEmpty()) {
            Allure.addAttachment("Network Logs", networkLogs);
        }

        Allure.addAttachment("Page Source", Objects.requireNonNull(DriverFactory.getDriver().getPageSource()));
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        LoggerUtil.warn("Test Skipped: " + result.getName());
    }

    @Override
    public void onStart(ITestContext context) {
        LoggerUtil.info("Test Suite Started");
    }

    @Override
    public void onFinish(ITestContext context) {
        LoggerUtil.info("Test Suite Finished");
    }
}
