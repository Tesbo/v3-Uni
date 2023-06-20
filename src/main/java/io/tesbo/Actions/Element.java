package io.tesbo.Actions;

import io.tesbo.framework.BaseTest;
import io.tesbo.framework.PageLocatorReader;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
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

    private By getLocatorBy(String locatorName) {
        Map<String, String> locatorProperties = new PageLocatorReader(locatorDirectory).getLocatorProperties(locatorName);
        String locatorType = locatorProperties.get("type");
        String locatorValue = locatorProperties.get("value");

        switch (locatorType) {
            case "id":
                return By.id(locatorValue);
            case "name":
                return By.name(locatorValue);
            case "class":
                return By.className(locatorValue);
            case "css":
                return By.cssSelector(locatorValue);
            case "linkText":
                return By.linkText(locatorValue);
            case "partialLinkText":
                return By.partialLinkText(locatorValue);
            case "tag":
                return By.tagName(locatorValue);
            case "xpath":
                return By.xpath(locatorValue);
            default:
                throw new IllegalArgumentException("Unsupported locator type: " + locatorType);
        }
    }

    private WebElement getWebElement(String locatorName) {
        By locator = getLocatorBy(locatorName);
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30));
        return wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    // Add other action methods as per your requirement

    public void click(String locatorName) {
        getWebElement(locatorName).click();
    }

    public void enterText(String locatorName, String text) {
        WebElement element = getWebElement(locatorName);
        element.clear();
        element.sendKeys(text);
    }

    public void clearText(String locatorName) {
        WebElement element = getWebElement(locatorName);
        element.clear();
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

    public boolean isEnabled(String locatorName) {
        WebElement element = getWebElement(locatorName);

        return element.isEnabled();
    }
}
