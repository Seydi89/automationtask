package com.automation.task.tests.ui.custom;

import static com.automation.task.tests.ui.base.UiPropertyReader.EXPLICIT_TIMEOUT_IN_SECONDS;

import com.automation.task.tests.ui.custom.exceptions.ElementNotFound;
import lombok.experimental.UtilityClass;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

@UtilityClass
public class CustomWait {

    public static void waitUntilElementIsClickable(WebElement element, WebDriver driver) {
        try {
            new WebDriverWait(driver, EXPLICIT_TIMEOUT_IN_SECONDS).until(ExpectedConditions.elementToBeClickable(element));
        } catch (TimeoutException e) {
            throw new ElementNotFound(element, EXPLICIT_TIMEOUT_IN_SECONDS, "Element is not clickable.");
        }
    }

    public static void waitUntilTextIsPresentInElement(WebElement element, String text, WebDriver driver) {
        try {
            new WebDriverWait(driver, EXPLICIT_TIMEOUT_IN_SECONDS).until(ExpectedConditions.textToBePresentInElement(element, text));
        } catch (TimeoutException e) {
            throw new ElementNotFound(element, EXPLICIT_TIMEOUT_IN_SECONDS, "Text is not present in Element.");
        }
    }
}
