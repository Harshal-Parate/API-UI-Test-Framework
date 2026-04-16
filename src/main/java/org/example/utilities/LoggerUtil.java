package org.example.utilities;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LoggerUtil {

    private static final Logger log = LoggerFactory.getLogger(LoggerUtil.class);

    public static void info(String msg) {
        log.info(msg);
    }

    public static void warn(String msg) {
        log.warn(msg);
    }

    public static void error(String msg) {
        log.error(msg);
    }
}