package io.tesbo.framework;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.ios.IOSDriver;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.MutableCapabilities;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeDriverService;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.safari.SafariDriver;
import org.testng.ITestContext;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.TimeUnit;

public class BaseTest {

    protected WebDriver webDriver;
    protected AppiumDriver mobileDriver;
    protected ConfigurationReader configReader;


    @BeforeMethod
    public void setUp(ITestContext context) throws MalformedURLException {
        String configFilePath = System.getProperty("configFilePath");
        if (configFilePath == null) {
            configFilePath = "src/main/resources/webConfig.json";
        }
        configReader = new ConfigurationReader(new File(configFilePath).getAbsolutePath());
        String runLocation = configReader.getRunLocation().toLowerCase();
        MutableCapabilities capabilities = new MutableCapabilities(configReader.getCapabilities());

        if (configReader.getTestType().equalsIgnoreCase("web")) {
            switch (configReader.getBrowserName().toLowerCase()) {
                case "chrome":
                    if(runLocation.equals("local")) {
                        try {
                            WebDriverManager.chromedriver().setup();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        webDriver = new ChromeDriver();
                        ChromeDriverService service = new ChromeDriverService.Builder()
                                .withLogOutput(System.out)
                                .build();
                    } else {
                        webDriver = new RemoteWebDriver(new URL(configReader.getCloudPlatformUrl()), capabilities);
                    }
                    break;
                case "firefox":
                    if(runLocation.equals("local")) {
                        WebDriverManager.firefoxdriver().setup();
                        webDriver = new FirefoxDriver();
                    } else {
                        webDriver = new RemoteWebDriver(new URL(configReader.getCloudPlatformUrl()), capabilities);
                    }
                    break;
                case "safari":
                    if(runLocation.equals("local")) {
                        WebDriverManager.getInstance(SafariDriver.class).setup();
                        webDriver = new SafariDriver();
                    } else {
                        webDriver = new RemoteWebDriver(new URL(configReader.getCloudPlatformUrl()), capabilities);
                    }
                    break;
                // Add more browsers if needed
                default:
                    throw new IllegalArgumentException("Invalid browser type");
            }
            webDriver.get(configReader.getBaseUrl());
            webDriver.manage().window().maximize();
            webDriver.manage().timeouts().implicitlyWait(configReader.getTimeout(), TimeUnit.SECONDS);
        }

        else if (configReader.getTestType().equalsIgnoreCase("mobile")) {

            if (configReader.getPlatformName().equalsIgnoreCase("android")) {
                // Setup Android driver
                mobileDriver = new AndroidDriver(new URL("http://localhost:4723/wd/hub"), capabilities);
            } else if (configReader.getPlatformName().equalsIgnoreCase("ios")) {
                // Setup iOS driver
                mobileDriver = new IOSDriver(new URL("http://localhost:4723/wd/hub"), capabilities);
            } else {
                throw new IllegalArgumentException("Invalid platform type");
            }
        }

        // For API testing, you might not need a WebDriver/AppiumDriver instance.
        // Instead, you could initialize your API testing client here.
        else if (configReader.getTestType().equalsIgnoreCase("api")) {
            // Initialize API testing client
        }

        else {
            throw new IllegalArgumentException("Invalid test type");
        }
    }

    @AfterMethod
    public void tearDown(ITestResult result) {

        if (ITestResult.FAILURE == result.getStatus()) {
            try {
                // Casting the driver instance to TakesScreenshot
                TakesScreenshot screenshot = (TakesScreenshot) webDriver;
                // Getting the screenshot and storing it as a file format
                File src = screenshot.getScreenshotAs(OutputType.FILE);
                // Now copying the screenshot to desired location using copyFile method
                FileUtils.copyFile(src, new File("screenshots/" + result.getName() + ".png"));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if (webDriver != null) {
            webDriver.quit();
        }
        if (mobileDriver != null) {
            mobileDriver.quit();
        }
        // Add cleanup code for your API testing client if needed
    }
}
