package org.example.locatormanagement;

import org.example.utilities.LoggerUtil;
import org.w3c.dom.*;
import javax.xml.parsers.*;
import java.io.File;
import java.util.Map;

import org.w3c.dom.*;
import javax.xml.parsers.*;
import java.util.concurrent.ConcurrentHashMap;

public class LocatorRepository {

    private static final String FILE_PATH = "src/test/resources/locators.xml";
    private static final Map<String, String> cache = new ConcurrentHashMap<>();
    private static volatile boolean isLoaded = false;

    private LocatorRepository() {}

    private static synchronized void loadLocators() {
        if (isLoaded) return;
        try {
            File file = new File(FILE_PATH);

            DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();

            Document doc = builder.parse(file);
            doc.getDocumentElement().normalize();

            NodeList pages = doc.getDocumentElement().getChildNodes();

            for (int i = 0; i < pages.getLength(); i++) {
                Node pageNode = pages.item(i);
                if (pageNode.getNodeType() == Node.ELEMENT_NODE) {
                    String pageName = pageNode.getNodeName();
                    NodeList elements = pageNode.getChildNodes();
                    for (int j = 0; j < elements.getLength(); j++) {
                        Node elementNode = elements.item(j);
                        if (elementNode.getNodeType() == Node.ELEMENT_NODE) {
                            String locatorName = elementNode.getNodeName();
                            String locatorValue = elementNode.getTextContent().trim();
                            String key = pageName + "." + locatorName;
                            cache.put(key, locatorValue);
                        }
                    }
                }
            }
            isLoaded = true;
            LoggerUtil.info("LocatorRepository loaded with " + cache.size() + " locators");
        } catch (Exception e) {
            throw new RuntimeException("Failed to load locator XML", e);
        }
    }

    public static String getLocator(String key) {
        if (!isLoaded) {
            loadLocators();
        }
        String locator = cache.get(key);
        if (locator == null) {
            throw new RuntimeException("Locator not found: " + key);
        }
        return locator;
    }

    public static synchronized void reload() {
        cache.clear();
        isLoaded = false;
        loadLocators();
        LoggerUtil.info("LocatorRepository reloaded");
    }
}