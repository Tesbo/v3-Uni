package io.tesbo.utils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;
import java.util.Map;

public class ActionClassGenerator {

    public static void main(String[] args) {
        generateActionClass("HomePage", "src/test/java/io/tesbo/webTests/pages/HomePage.json", "src/test/java/io/tesbo/webTests/actions");
    }
    public static void generateActionClass(String pageName, String locatorsFilePath, String outputDirectory) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode locatorsNode = objectMapper.readTree(new File(locatorsFilePath));

            StringBuilder actionClassBuilder = new StringBuilder();
            actionClassBuilder.append("package io.tesbo.webTests.actions;\n");
            actionClassBuilder.append("\n");
            actionClassBuilder.append("import io.tesbo.Actions.Element;\n");
            actionClassBuilder.append("import org.openqa.selenium.WebDriver;\n");
            actionClassBuilder.append("\n");
            actionClassBuilder.append("public class ").append(pageName).append("Actions {\n");
            actionClassBuilder.append("\n");
            actionClassBuilder.append("    private WebDriver driver;\n");
            actionClassBuilder.append("    private Element element;\n");
            actionClassBuilder.append("\n");
            actionClassBuilder.append("    public ").append(pageName).append("Actions(WebDriver driver) {\n");
            actionClassBuilder.append("        this.driver = driver;\n");
            actionClassBuilder.append("        this.element = new Element(driver);\n");
            actionClassBuilder.append("    }\n");
            actionClassBuilder.append("\n");

            Iterator<Map.Entry<String, JsonNode>> fields = locatorsNode.fields();
            while (fields.hasNext()) {
                Map.Entry<String, JsonNode> field = fields.next();
                String locatorName = field.getKey();
                JsonNode locatorDetails = field.getValue();
                String locatorType = locatorDetails.get("type").asText();

                String methodName = getMethodName(locatorName, locatorType);
                String actionMethod = getActionMethod(locatorType);

                actionClassBuilder.append("    public void ").append(methodName).append("() {\n");
                actionClassBuilder.append("        element.").append(actionMethod).append("(\"").append(locatorName).append("\");\n");
                actionClassBuilder.append("    }\n");
                actionClassBuilder.append("\n");
            }

            actionClassBuilder.append("}\n");

            // Write the generated action class to the output directory
            String outputFilePath = outputDirectory + "/" + pageName + "Actions.java";
            FileUtils.writeStringToFile(new File(outputFilePath), actionClassBuilder.toString(), StandardCharsets.UTF_8);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String getMethodName(String locatorName, String locatorType) {
        String action = getActionFromLocatorType(locatorType);
        StringBuilder methodNameBuilder = new StringBuilder(action);
        String[] parts = locatorName.split("(?<=.)(?=\\p{Lu})");
        for (String part : parts) {
            methodNameBuilder.append(part.toLowerCase());
        }
        return methodNameBuilder.toString();
    }

    private static String getActionFromLocatorType(String locatorType) {
        switch (locatorType) {
            case "id":
            case "name":
            case "class":
            case "css":
            case "linkText":
            case "partialLinkText":
            case "tag":
            case "xpath":
                return "clickOn";
            case "text":
                return "enterTextOn";
            case "checkbox":
            case "radio":
                return "setCheckedOn";
            case "select":
                return "selectOptionOn";
            case "submit":
                return "submitOn";
            default:
                throw new IllegalArgumentException("Unsupported locator type: " + locatorType);
        }
    }

    private static String getActionMethod(String locatorType) {
        switch (locatorType) {
            case "id":
            case "name":
            case "class":
            case "css":
            case "linkText":
            case "partialLinkText":
            case "tag":
            case "xpath":
                return "click";
            case "text":
                return "enterText";
            case "checkbox":
            case "radio":
                return "setChecked";
            case "select":
                return "selectOption";
            case "submit":
                return "submit";
            default:
                throw new IllegalArgumentException("Unsupported locator type: " + locatorType);
        }
    }
}
