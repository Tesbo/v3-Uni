package io.tesbo.Actions;

import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class Element {

    private WebDriver driver;
    private String locatorValue;
    private By locator;

    public Element(WebDriver driver) {
        this.driver = driver;

        this.locator = getLocatorBy(locatorValue);
    }

    private By getLocatorBy(String locatorValue) {
        if (locatorValue.startsWith("//")) {
            return By.xpath(locatorValue);
        } else {
            return By.cssSelector(locatorValue);
        }
    }

    public WebElement getWebElement() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30));
        return wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    public void click() {
        getWebElement().click();
    }

    public void enterText(String text) {
        getWebElement().sendKeys(text);
    }

    public String getText() {
        return getWebElement().getText();
    }

    public boolean isDisplayed() {
        try {
            return getWebElement().isDisplayed();
        } catch (NoSuchElementException e) {
            return false;
        }
    }

    public void clear() {
        getWebElement().clear();
    }

    public void hover() {
        Actions action = new Actions(driver);
        action.moveToElement(getWebElement()).perform();
    }

    // Add more actions as needed...
}
