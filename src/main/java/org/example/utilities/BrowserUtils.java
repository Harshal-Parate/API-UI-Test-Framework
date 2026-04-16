package org.example.utilities;

import org.example.drivermanager.DriverFactory;
import org.openqa.selenium.logging.LogEntries;
import org.openqa.selenium.logging.LogEntry;
import org.openqa.selenium.logging.LogType;

public class BrowserUtils {

    public static String getBrowserLogs() {
        StringBuilder sb = new StringBuilder();
        try {
            LogEntries logEntries = DriverFactory.getDriver().manage()
                    .logs()
                    .get(LogType.BROWSER);

            for (LogEntry logs : logEntries) {
                sb.append(logs.getLevel())
                        .append(" : ")
                        .append(logs.getMessage())
                        .append("/n");
            }
        } catch (Exception e) {
            sb.append("No browser logs available");
        }
        return sb.toString();
    }
}
