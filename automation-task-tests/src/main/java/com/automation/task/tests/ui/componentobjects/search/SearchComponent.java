package com.automation.task.tests.ui.componentobjects.search;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class SearchComponent {

    private final WebDriver driver;

    @FindBy(id = "twotabsearchtextbox")
    private WebElement searchInput;

    @FindBy(css = "input[type='submit']")
    private WebElement searchSubmitButton;

    public SearchComponent(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    public void search(String query) {
        searchInput.clear();
        searchInput.sendKeys(query);
        searchSubmitButton.click();
    }
}
