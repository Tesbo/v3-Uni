package io.tesbo.framework;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class PageLocatorReader {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    private Map<String, Map<String, String>> pageLocators = new HashMap<>();

    public PageLocatorReader(String directoryPath) throws IOException {
        File directory = new File(directoryPath);
        if (directory.exists() && directory.isDirectory()) {
            for (File file : directory.listFiles()) {
                if (file.isFile() && file.getName().endsWith(".json")) {
                    String pageName = file.getName().substring(0, file.getName().lastIndexOf('.'));
                    JsonNode node = objectMapper.readTree(file);
                    Map<String, String> locators = new HashMap<>();
                    Iterator<Map.Entry<String, JsonNode>> fields = node.fields();
                    while (fields.hasNext()) {
                        Map.Entry<String, JsonNode> field = fields.next();
                        locators.put(field.getKey(), field.getValue().asText());
                    }
                    pageLocators.put(pageName, locators);
                }
            }
        } else {
            throw new IllegalArgumentException("Invalid directory path: " + directoryPath);
        }
    }

    public String getLocatorValue(String locatorKey) {
        if (locatorKey.contains(".")) {
            String[] parts = locatorKey.split("\\.", 2);
            return getLocatorValue(parts[0], parts[1]);
        } else {
            for (Map<String, String> locators : pageLocators.values()) {
                if (locators.containsKey(locatorKey)) {
                    return locators.get(locatorKey);
                }
            }
            throw new IllegalArgumentException("No locator found with name '" + locatorKey + "' in any page");
        }
    }

    private String getLocatorValue(String pageName, String locatorName) {
        if (pageLocators.containsKey(pageName) && pageLocators.get(pageName).containsKey(locatorName)) {
            return pageLocators.get(pageName).get(locatorName);
        } else {
            throw new IllegalArgumentException("No locator found with name '" + locatorName + "' in page '" + pageName + "'");
        }
    }
}
