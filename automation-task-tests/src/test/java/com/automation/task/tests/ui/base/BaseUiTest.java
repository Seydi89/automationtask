package com.automation.task.tests.ui.base;

import static com.automation.task.tests.api.base.UserCredentials.BASE_URL;
import static com.automation.task.tests.ui.base.UiPropertyReader.CHROME_DRIVER_LOCATION;
import static com.automation.task.tests.ui.base.UiPropertyReader.CHROME_DRIVER_LOCATION_PROPERTY;
import static com.automation.task.tests.ui.base.UiPropertyReader.IMPLICIT_TIMEOUT_IN_SECONDS;

import java.io.File;
import java.util.concurrent.TimeUnit;
import lombok.Getter;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

@ExtendWith(CustomTestWatcher.class)
public class BaseUiTest {

    protected WebDriver driver = null;

    @Getter
    private static File src;

    @BeforeAll
    public static void baseSetup() {
        System.setProperty(CHROME_DRIVER_LOCATION_PROPERTY, CHROME_DRIVER_LOCATION);
    }

    @BeforeEach
    public void baseTestSetup() {
        driver = new ChromeDriver();
        driver.manage().timeouts()
                .implicitlyWait(IMPLICIT_TIMEOUT_IN_SECONDS, TimeUnit.SECONDS);
        driver.get(BASE_URL);
    }

    @AfterEach
    public void baseTearDown() {
        src = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
        driver.quit();
    }
}
