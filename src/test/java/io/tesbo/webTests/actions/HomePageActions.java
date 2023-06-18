package io.tesbo.webTests.actions;

import io.tesbo.Actions.Element;
import org.openqa.selenium.WebDriver;

public class HomePageActions {

    private WebDriver driver;
    private Element element;

    public HomePageActions(WebDriver driver) {
        this.driver = driver;
        this.element = new Element(driver);
    }

    public void clickOnemail() {
        element.click("email");
    }

    public void clickOnpassword() {
        element.click("password");
    }

}
