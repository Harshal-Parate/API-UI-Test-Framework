package org.example.utilities;

import org.example.drivermanager.DriverFactory;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;

public class ScreenshotUtil {

    public static byte[] capture() {
        return ((TakesScreenshot) DriverFactory.getDriver()).
                getScreenshotAs(OutputType.BYTES);
    }

}
