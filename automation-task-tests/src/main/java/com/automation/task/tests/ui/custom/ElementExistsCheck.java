package com.automation.task.tests.ui.custom;

import java.util.List;
import lombok.experimental.UtilityClass;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;

@UtilityClass
public class ElementExistsCheck {

    public static boolean isElementExistingOnPage(WebElement element) {
        try {
            element.getText();
            return true;
        } catch(NoSuchElementException ex) {
            return false;
        }
    }

    public static boolean isElementExistingOnPage(WebElement mainElement, By selector) {
        try {
            mainElement.findElement(selector).getText();
            return true;
        } catch(NoSuchElementException ex) {
            return false;
        }
    }

    public static boolean isElementExistingOnPage(List<WebElement> element) {
        try {
            element.get(0).getText();
            return true;
        } catch(IndexOutOfBoundsException ex) {
            return false;
        }
    }
}
