package io.tesbo.framework.dataProvider;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.tesbo.framework.ConfigurationReader;

import java.io.File;
import java.io.IOException;

public class DataSource {
    private JsonNode jsonFile;
    public ConfigurationReader configurationReader;
    public DataSource(String fileName){
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            this.jsonFile = objectMapper.readTree(new File("src/main/resources/dataprovider/"+fileName));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public String getValue(String name){
        return String.valueOf(this.jsonFile.get(name));
    }
    public JsonNode get(String objectName){
        return this.jsonFile.get(objectName);
    }
    public JsonNode getDataUsingJsonPath(String objectName){
        return this.jsonFile.findValue(objectName);
    }

}