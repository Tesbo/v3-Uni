
package io.tesbo.Actions;

import org.openqa.selenium.Alert;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.WebDriver;

import java.util.Set;

public class BrowserActions {

    private WebDriver driver;

    public BrowserActions(WebDriver driver) {
        this.driver = driver;
    }

    public void navigateTo(String url) {
        driver.get(url);
    }

    public void refresh() {
        driver.navigate().refresh();
    }

    public void back() {
        driver.navigate().back();
    }

    public void forward() {
        driver.navigate().forward();
    }

    public void addCookie(Cookie cookie) {
        driver.manage().addCookie(cookie);
    }

    public Set<Cookie> getCookies() {
        return driver.manage().getCookies();
    }

    public void deleteCookie(Cookie cookie) {
        driver.manage().deleteCookie(cookie);
    }

    public void deleteAllCookies() {
        driver.manage().deleteAllCookies();
    }

    public void switchToFrame(String frameName) {
        driver.switchTo().frame(frameName);
    }

    public void switchToParentFrame() {
        driver.switchTo().parentFrame();
    }

    public void switchToTab(int index) {
        String handle = (String) driver.getWindowHandles().toArray()[index];
        driver.switchTo().window(handle);
    }

    public void closeTab(int index) {
        String handle = (String) driver.getWindowHandles().toArray()[index];
        driver.switchTo().window(handle);
        driver.close();
    }

    public Alert getAlert() {
        return driver.switchTo().alert();
    }

    public void acceptAlert() {
        getAlert().accept();
    }

    public void dismissAlert() {
        getAlert().dismiss();
    }

    public void enterTextInAlert(String text) {
        getAlert().sendKeys(text);
    }

    public String getTextFromAlert() {
        return getAlert().getText();
    }
}
