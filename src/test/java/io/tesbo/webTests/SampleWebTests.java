package io.tesbo.webTests;

import io.tesbo.framework.BaseTest;
import io.tesbo.webTests.actions.HomePageActions;
import org.testng.Assert;
import org.testng.annotations.Test;

public class SampleWebTests extends BaseTest {

    @Test
    public void sampleWebTest() {

        HomePageActions homepage = new HomePageActions(webDriver);
        homepage.enterInEmail("hi@hi.com");
        homepage.enterInPassword("test");

    }
}