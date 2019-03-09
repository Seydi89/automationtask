package com.automation.task.tests.ui.custom.exceptions;

import org.openqa.selenium.WebElement;

public class ElementNotFound extends RuntimeException {

    public ElementNotFound(WebElement element, int timeoutInSeconds, String reason) {
        super("Element not found: " + element + " in " + timeoutInSeconds + " seconds. " + reason);
    }
}
