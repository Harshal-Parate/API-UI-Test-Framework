package org.example.utilities;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class ConfigReader {

    private ConfigReader() {}

    private static Properties properties = new Properties();

    static {
        loadProperties();
    }

    private static void loadProperties() {
        try {
            String path = System.getProperty("config.path", "src/test/resources/properties/qa.properties");
            FileInputStream fs = new FileInputStream(path);
            properties.load(fs);
        } catch (IOException e) {
            throw new RuntimeException("Fail to load the properties file", e);
        }
    }

    public static String get(String key) {
        return properties.getProperty(key).trim();
    }
}
