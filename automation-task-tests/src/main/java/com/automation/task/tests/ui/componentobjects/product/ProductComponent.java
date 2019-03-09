package com.automation.task.tests.ui.componentobjects.product;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class ProductComponent {

    private WebDriver driver;

    @FindBy(id = "productTitle")
    private WebElement productTitle;

    @FindBy(id = "priceblock_ourprice")
    private WebElement productPrice;

    public ProductComponent(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    public String getProductPrice() {
        return productPrice.getText();
    }

    public String getProductTitle() {
        return productTitle.getText();
    }
}
