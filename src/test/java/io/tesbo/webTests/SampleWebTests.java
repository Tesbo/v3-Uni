package io.tesbo.webTests;

import io.tesbo.framework.BaseTest;
import org.testng.Assert;
import org.testng.annotations.Test;

public class SampleWebTests extends BaseTest {

    @Test
    public void sampleWebTest() {
        String url = "https://www.google.com/";
        webDriver.get(url);

        Assert.assertEquals(webDriver.getCurrentUrl(), url, "Failed to load the correct URL");
    }
}