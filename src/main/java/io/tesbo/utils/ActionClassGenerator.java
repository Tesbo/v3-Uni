package io.tesbo.utils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.Scanner;

public class ActionClassGenerator {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // Prompt the user for the locator file path
        System.out.print("Enter the locator file path: ");
        String locatorsFilePath = scanner.nextLine();

        // Prompt the user for the preference (all elements or specific element)
        System.out.print("Enter the preference (all/specific): ");
        String preference = scanner.nextLine();

        if (preference.equalsIgnoreCase("all")) {
            generateActionClassForAllElements(locatorsFilePath);
        } else if (preference.equalsIgnoreCase("specific")) {
            // Prompt the user for the element name
            System.out.print("Enter the element name: ");
            String elementName = scanner.nextLine();
            generateActionClassForSpecificElement(locatorsFilePath, elementName);
        } else {
            System.out.println("Invalid preference. Please try again.");
        }
    }

    public static void generateActionClassForAllElements(String locatorsFilePath) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode locatorsNode = objectMapper.readTree(new File(locatorsFilePath));

            StringBuilder actionClassBuilder = new StringBuilder();
            actionClassBuilder.append("package io.tesbo.webTests.actions;\n");
            actionClassBuilder.append("\n");
            actionClassBuilder.append("import io.tesbo.Actions.Element;\n");
            actionClassBuilder.append("import org.openqa.selenium.WebDriver;\n");
            actionClassBuilder.append("\n");
            actionClassBuilder.append("public class PageActions {\n");
            actionClassBuilder.append("\n");
            actionClassBuilder.append("    private WebDriver driver;\n");
            actionClassBuilder.append("    private Element element;\n");
            actionClassBuilder.append("\n");
            actionClassBuilder.append("    public PageActions(WebDriver driver) {\n");
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

                actionClassBuilder.append(generateActionMethods(locatorName, locatorType));
            }

            actionClassBuilder.append("}\n");

            // Print the generated action class
            System.out.println(actionClassBuilder.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void generateActionClassForSpecificElement(String locatorsFilePath, String elementName) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode locatorsNode = objectMapper.readTree(new File(locatorsFilePath));

            StringBuilder actionClassBuilder = new StringBuilder();
            JsonNode locatorDetails = locatorsNode.get(elementName);
            if (locatorDetails != null) {
                String locatorType = locatorDetails.get("type").asText();
                actionClassBuilder.append(generateActionMethods(elementName, locatorType));
            } else {
                System.out.println("Element not found in the locators file.");
                return;
            }

            actionClassBuilder.append("}\n");

            // Print the generated action class
            System.out.println(actionClassBuilder.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    private static String generateActionMethods(String locatorName, String locatorType) {
        StringBuilder actionMethodsBuilder = new StringBuilder();

        String[] actions;
        if (locatorType.equals("button")) {
            actions = new String[]{"click", "getText", "isDisplayed", "isEnabled"};
        } else if (locatorType.equals("text box")) {
            actions = new String[]{"enterText", "getText", "isDisplayed", "isEnabled"};
        } else if (locatorType.equals("radio button")) {
            actions = new String[]{"select", "isSelected", "isDisplayed", "isEnabled"};
        } else if (locatorType.equals("check box")) {
            actions = new String[]{"check", "uncheck", "isSelected", "isDisplayed", "isEnabled"};
        } else if (locatorType.equals("select list")) {
            actions = new String[]{"selectByVisibleText", "selectByIndex", "selectByValue", "getSelectedOption", "isDisplayed", "isEnabled"};
        } else {
            return actionMethodsBuilder.toString();
        }

        for (String action : actions) {
            String methodName = getMethodName(locatorName, locatorType, action);
            String formattedMethodName = formatMethodName(methodName);

            if (action.equals("enterText")) {
                actionMethodsBuilder.append("    public void ").append(formattedMethodName).append("(String textToEnter) {\n");
                actionMethodsBuilder.append("        element.").append(action).append("(\"").append(locatorName).append("\", textToEnter);\n");
                actionMethodsBuilder.append("    }\n");
            } else {
                actionMethodsBuilder.append("    public void ").append(formattedMethodName).append("() {\n");
                actionMethodsBuilder.append("        element.").append(action).append("(\"").append(locatorName).append("\");\n");
                actionMethodsBuilder.append("    }\n");
            }

            actionMethodsBuilder.append("\n");
        }

        return actionMethodsBuilder.toString();
    }


    private static String getMethodName(String locatorName, String locatorType, String action) {
        StringBuilder methodNameBuilder = new StringBuilder();

        if (action.equals("click")) {
            methodNameBuilder.append("clickOn").append(capitalizeFirstLetter(locatorName)).append(capitalizeFirstLetter(locatorType));
        } else if (action.equals("getText")) {
            methodNameBuilder.append("getTextFrom").append(capitalizeFirstLetter(locatorName)).append(capitalizeFirstLetter(locatorType));
        } else if (action.equals("enterText")) {
            methodNameBuilder.append("enterTextInto").append(capitalizeFirstLetter(locatorName)).append(capitalizeFirstLetter(locatorType));
        } else if (action.equals("isDisplayed")) {
            methodNameBuilder.append("verify").append(capitalizeFirstLetter(locatorName)).append(capitalizeFirstLetter(locatorType)).append("IsDisplayed");
        } else if (action.equals("isEnabled")) {
            methodNameBuilder.append("verify").append(capitalizeFirstLetter(locatorName)).append(capitalizeFirstLetter(locatorType)).append("IsEnabled");
        } else if (action.equals("select"))
        {
            methodNameBuilder.append("verify").append(capitalizeFirstLetter(locatorName)).append(capitalizeFirstLetter(locatorType)).append("IsEnabled");
        } else if (action.equals("check")  )
        {
            methodNameBuilder.append("clickOn").append(capitalizeFirstLetter(locatorName)).append(capitalizeFirstLetter(locatorType)).append("ToCheck");
        }
        else if (action.equals("uncheck")  )
        {
            methodNameBuilder.append("clickOn").append(capitalizeFirstLetter(locatorName)).append(capitalizeFirstLetter(locatorType)).append("ToUnCheck");
        }
        else if (action.equals("selectByVisibleText")  )
        {
            methodNameBuilder.append("selectOptionUsingTextBy").append(capitalizeFirstLetter(locatorName)).append("Dropdown");

        }
        else if (action.equals("selectByIndex")  )
        {
            methodNameBuilder.append("selectOptionUsingIndexBy").append(capitalizeFirstLetter(locatorName)).append("Dropdown");

        }
        else if (action.equals("selectByValue")  )
        {
            methodNameBuilder.append("selectOptionUsingValueBy").append(capitalizeFirstLetter(locatorName)).append("Dropdown");

        }

        else if (action.equals("getSelectedOption")  )
        {
        }

        else if (action.equals("selectByIndex")  )
        {
        }

        return methodNameBuilder.toString();
    }

    private static String capitalizeFirstLetter(String text) {
        return text.substring(0, 1).toUpperCase() + text.substring(1);
    }

    private static String formatMethodName(String methodName) {
        return methodName.replaceAll("\\s+", "");
    }
}
