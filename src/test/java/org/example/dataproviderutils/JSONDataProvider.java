package org.example.dataproviderutils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.testng.annotations.DataProvider;

import java.io.File;
import java.lang.reflect.Method;
import java.util.*;

public class JSONDataProvider {

    private static final ObjectMapper mapper = new ObjectMapper();
    private static JsonNode rootNode;

    static {
        try {
            String env = System.getProperty("env", "qa");
            String path = "src/test/resources/testdata/" + env + ".json";

            rootNode = mapper.readTree(new File(path));
            System.out.println("Loaded test data for env: " + env);

        } catch (Exception e) {
            throw new RuntimeException("Failed to load JSON data", e);
        }
    }

    @DataProvider(name = "jsonData")
    public static Object[][] getData(Method method) {

        if (!method.isAnnotationPresent(DataProviderConfig.class)) {
            throw new RuntimeException("Missing @DataProviderConfig annotation");
        }

        DataProviderConfig config = method.getAnnotation(DataProviderConfig.class);

        String key = config.key();
        String[] fields = config.fields().split(",");

        JsonNode node = rootNode.get(key);

        if (node == null) {
            throw new RuntimeException("Key not found in JSON: " + key);
        }

        Object[] row = new Object[fields.length];

        for (int i = 0; i < fields.length; i++) {
            row[i] = node.get(fields[i]).asText();
        }

        return new Object[][] { row };
    }
}