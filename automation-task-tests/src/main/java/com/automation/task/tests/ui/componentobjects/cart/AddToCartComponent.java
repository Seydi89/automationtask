package com.automation.task.tests.ui.componentobjects.cart;

import static com.automation.task.tests.ui.custom.ElementExistsCheck.isElementExistingOnPage;
import static org.awaitility.Awaitility.await;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.Matchers.emptyString;
import static org.hamcrest.Matchers.is;

import com.automation.task.tests.ui.custom.CustomWait;
import lombok.extern.java.Log;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.Select;

@Log
public class AddToCartComponent {

    private final WebDriver driver;

    @FindBy(id = "add-to-cart-button")
    private WebElement addToCartButton;

    @FindBy(id = "huc-v2-order-row-container")
    private WebElement orderResultContainer;

    @FindBy(id = "ccxssHeaderContent")
    private WebElement orderResultAlternativeContainer;

    @FindBy(css = "select[name='quantity']")
    private WebElement quantitySelectBox;

    @FindBy(id = "buybox-see-all-buying-choices-announce")
    private WebElement seeBuyingOptions;

    @FindBy(name = "dropdown_selected_size_name")
    private WebElement sizeSelect;

    @FindBy(id = "olpOfferListColumn")
    private WebElement offerList;

    public AddToCartComponent(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    public void selectQuantity(int quantity) {
        selectFromBuyingOptionsIfExists();

        CustomWait.waitUntilElementIsClickable(quantitySelectBox, driver);
        Select quantitySelect = new Select(quantitySelectBox);

        try {
            quantitySelect.selectByValue(String.valueOf(quantity));
        } catch (NoSuchElementException ex) {
            log.severe("Data Error: Product does not have as much quantity available! Asked for " + quantity
                    + " items, but only "
                    + quantitySelect.getOptions().get(quantitySelect.getOptions().size() - 1) + " items available.");
        }
    }

    public void clickAddToCartButton() {
        addToCartButton.click();
    }

    public void addToCartWithQuantity(int quantity) {
        selectQuantity(quantity);
        clickAddToCartButton();
    }

    public boolean isAddToCartSuccessfulMessageDisplayed() {
        return getOrderResultSummary().contains("Added to your Cart") || getOrderResultSummary()
                .contains("Added to Cart");
    }

    private String getOrderResultSummary() {
        try {
            await().until(orderResultContainer::getText, is(not(emptyString())));
            return orderResultContainer.getText();
        } catch (NoSuchElementException ex) {
            await().until(orderResultAlternativeContainer::getText, is(not(emptyString())));
            return orderResultAlternativeContainer.getText();
        }
    }

    private void selectFromBuyingOptionsIfExists() {
        if (isElementExistingOnPage(seeBuyingOptions)) {
            seeBuyingOptions.click();
            // possibly not fully implemented
            log.info("Offer list content: " + offerList.getText());
        }

        if (isElementExistingOnPage(sizeSelect)) {
            Select sizeSelectBox = new Select(sizeSelect);
            sizeSelectBox.selectByIndex(1); // TaskNote: Since it is not running on pre-set data, just left at first item selection
        }
    }
}
