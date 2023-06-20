package io.tesbo.framework;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class PageLocatorReader {
    private Map<String, Map<String, String>> locatorMap;

    public PageLocatorReader(String locatorFilePath) {
        locatorMap = new HashMap<>();
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode locatorsNode = objectMapper.readTree(new File(locatorFilePath));
            Iterator<Map.Entry<String, JsonNode>> fields = locatorsNode.fields();
            while (fields.hasNext()) {
                Map.Entry<String, JsonNode> field = fields.next();
                String locatorName = field.getKey();
                JsonNode locatorDetails = field.getValue();
                String locatorType = locatorDetails.get("type").asText();
                String locatorValue = locatorDetails.get("value").asText();

                Map<String, String> locatorProperties = new HashMap<>();
                locatorProperties.put("type", locatorType);
                locatorProperties.put("value", locatorValue);
                locatorMap.put(locatorName, locatorProperties);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Map<String, String> getLocatorProperties(String locatorName) {
        return locatorMap.get(locatorName);
    }
}
