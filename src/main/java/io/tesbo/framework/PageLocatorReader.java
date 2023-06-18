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
    private Map<String, Map<String, Map<String, String>>> pageLocators = new HashMap<String, Map<String, Map<String, String>>>();

    private boolean isInitialized = false;
    private String directoryPath;

    public PageLocatorReader(String directoryPath) {
        this.directoryPath = directoryPath;
    }

    public void initPageLocators(String directoryPath)  {
        File directory = new File(directoryPath);
        parseDirectory(directory);
    }

    private void parseDirectory(File directory)  {
        if (directory.exists() && directory.isDirectory()) {
            for (File file : directory.listFiles()) {
                if (file.isFile() && file.getName().endsWith(".json")) {
                    parseJsonFile(file);
                } else if (file.isDirectory()) {
                    parseDirectory(file); // Recursively parse subdirectories
                }
            }
        } else {
            throw new IllegalArgumentException("Invalid directory path: " + directory.getPath());
        }
    }

    private void parseJsonFile(File file){
        String pageName = file.getName().substring(0, file.getName().lastIndexOf('.'));
        JsonNode node = null;
        try {
            node = objectMapper.readTree(file);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Map<String, Map<String, String>> locators = new HashMap<>();
        Iterator<Map.Entry<String, JsonNode>> fields = node.fields();
        while (fields.hasNext()) {
            Map.Entry<String, JsonNode> field = fields.next();
            String locatorType = field.getValue().get("type").asText();
            String locatorValue = field.getValue().get("value").asText();
            Map<String, String> locator = new HashMap<>();
            locator.put(locatorType, locatorValue);
            locators.put(field.getKey(), locator);
        }
        pageLocators.put(pageName, locators);
    }


    public Map<String, String> getLocatorValue(String locatorKey) {
        if (!isInitialized) {
            initPageLocators(directoryPath);
            isInitialized = true;
        }

        if (locatorKey.contains(".")) {
            String[] parts = locatorKey.split("\\.", 2);
            return getLocatorValue(parts[0], parts[1]);
        } else {
            for (Map<String, Map<String, String>> locators : pageLocators.values()) {
                if (locators.containsKey(locatorKey)) {
                    return locators.get(locatorKey);
                }
            }
            throw new IllegalArgumentException("No locator found with name '" + locatorKey + "' in any page");
        }
    }

    private Map<String, String> getLocatorValue(String pageName, String locatorName) {
        if (pageLocators.containsKey(pageName) && pageLocators.get(pageName).containsKey(locatorName)) {
            return pageLocators.get(pageName).get(locatorName);
        } else {
            throw new IllegalArgumentException("No locator found with name '" + locatorName + "' in page '" + pageName + "'");
        }
    }
}
