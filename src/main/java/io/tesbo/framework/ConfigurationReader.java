package io.tesbo.framework;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class ConfigurationReader {

    private JsonNode config;

    public ConfigurationReader(String filePath) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            this.config = objectMapper.readTree(new File(filePath));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getTestType() {
        return this.config.get("testType").asText();
    }

    public String getTestFramework() {
        return this.config.get("testFramework").asText();
    }

    public int getRetries() {
        return this.config.get("testConfiguration").get("retries").asInt();
    }

    public int getTimeout() {
        return this.config.get("testConfiguration").get("timeout").asInt();
    }

    public String getRunLocation() {
        return this.config.get("testConfiguration").get("runLocation").asText();
    }

    public String getBrowserName() {
        return this.config.get("browser").get("name").asText();
    }

    public String getBrowserVersion() {
        return this.config.get("browser").get("version").asText();
    }

    public boolean getBrowserHeadless() {
        return this.config.get("browser").get("headless").asBoolean();
    }

    public String getPlatformName() {
        return this.config.get("platform").get("name").asText();
    }

    public String getPlatformVersion() {
        return this.config.get("platform").get("version").asText();
    }

    public String getCloudPlatformName() {
        return this.config.get("cloudPlatform").get("name").asText();
    }

    public String getCloudPlatformUrl() {
        return this.config.get("cloudPlatform").get("url").asText();
    }

    public String getCloudPlatformUsername() {
        return this.config.get("cloudPlatform").get("username").asText();
    }

    public String getCloudPlatformAccessKey() {
        return this.config.get("cloudPlatform").get("accessKey").asText();
    }

    public String getDataSourceType() {
        return this.config.get("dataSource").get("type").asText();
    }

    public String getDataSourceLocation() {
        return this.config.get("dataSource").get("location").asText();
    }

    public Map<String, String> getCapabilities() {
        Map<String, String> capabilities = new HashMap<>();
        if (config.has("capabilities")) {
            ObjectNode capabilitiesNode = (ObjectNode) config.get("capabilities");
            Iterator<Map.Entry<String, JsonNode>> fields = capabilitiesNode.fields();
            while (fields.hasNext()) {
                Map.Entry<String, JsonNode> field = fields.next();
                capabilities.put(field.getKey(), field.getValue().asText());
            }
        }
        return capabilities;
    }

    public String getBaseUrl() {
        if (config.has("baseUrl")) {
            return config.get("baseUrl").asText();
        }
        return null;
    }
}
