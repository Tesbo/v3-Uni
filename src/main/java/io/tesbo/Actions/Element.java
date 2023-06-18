package io.tesbo.Actions;

import io.tesbo.framework.BaseTest;
import io.tesbo.framework.PageLocatorReader;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.Map;

public class Element {

    private WebDriver driver;
    private String locatorDirectory;

    public Element(WebDriver driver) {
        this.driver = driver;
        if (BaseTest.configReader.getTestType().equals("web")) {
            locatorDirectory = "src/test/java/io/tesbo/webTests/pages";
        } else if (BaseTest.configReader.getTestType().equals("mobile")) {
            locatorDirectory = "src/test/java/io/tesbo/mobileTests/screens";
        }
    }

    private By getLocatorBy(String locatorKey) {
        Map<String, String> locator = new PageLocatorReader(locatorDirectory).getLocatorValue(locatorKey);
        Map.Entry<String, String> entry = locator.entrySet().iterator().next();
        String type = entry.getKey();
        String value = entry.getValue();

        switch (type) {
            case "id": return By.id(value);
            case "name": return By.name(value);
            case "class": return By.className(value);
            case "css": return By.cssSelector(value);
            case "linkText": return By.linkText(value);
            case "partialLinkText": return By.partialLinkText(value);
            case "tag": return By.tagName(value);
            case "xpath": return By.xpath(value);
            default: throw new IllegalArgumentException("Unsupported locator type: " + type);
        }
    }

    private WebElement getWebElement(String locatorName) {
        By locator = getLocatorBy(locatorName);
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30));
        return wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    public void click(String locatorName) {
        getWebElement(locatorName).click();
    }

    public void enterText(String locatorName, String text) {
        WebElement element = getWebElement(locatorName);
        element.clear();
        element.sendKeys(text);
    }

    public String getText(String locatorName) {
        return getWebElement(locatorName).getText();
    }

    public boolean isDisplayed(String locatorName) {
        try {
            return getWebElement(locatorName).isDisplayed();
        } catch (NoSuchElementException e) {
            return false;
        }
    }

    public void clear(String locatorName) {
        getWebElement(locatorName).clear();
    }

    public void hover(String locatorName) {
        Actions action = new Actions(driver);
        action.moveToElement(getWebElement(locatorName)).perform();
    }

    public void dragAndDrop(String sourceLocator, String targetLocator) {
        WebElement sourceElement = getWebElement(sourceLocator);
        WebElement targetElement = getWebElement(targetLocator);
        Actions action = new Actions(driver);
        action.dragAndDrop(sourceElement, targetElement).perform();
    }
}
