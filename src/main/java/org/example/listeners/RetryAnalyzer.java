package org.example.listeners;

import org.example.utilities.ConfigReader;
import org.example.utilities.LoggerUtil;
import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;

public class RetryAnalyzer implements IRetryAnalyzer {

    private int count = Integer.parseInt(ConfigReader.get("retryCount"));
    private static final int MAX_RETRY = Integer.parseInt(ConfigReader.get("maxRetries"));

    @Override
    public boolean retry(ITestResult iTestResult) {
        while (count < MAX_RETRY) {
            count++;
            LoggerUtil.info("Retrying Test: " + iTestResult.getName());
            return true;
        }
        return false;
    }
}
