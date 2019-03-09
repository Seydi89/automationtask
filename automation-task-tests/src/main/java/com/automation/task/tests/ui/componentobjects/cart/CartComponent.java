package com.automation.task.tests.ui.componentobjects.cart;

import com.automation.task.tests.ui.custom.CustomWait;
import java.util.List;
import java.util.Optional;
import lombok.extern.java.Log;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.Select;

@Log
public class CartComponent {

    private final WebDriver driver;

    @FindBy(id = "nav-cart")
    private WebElement goToCartButtoon;

    @FindBy(css = "[data-action='delete']")
    private List<WebElement> deletedItem;

    @FindBy(css = "[data-name='Active Items']")
    private List<WebElement> productsInCart;

    @FindBy(id = "nav-cart-count")
    private WebElement numberOfItemsInCart;

    @FindBy(className = "sc-list-item-content")
    private List<WebElement> listItemContentDiv;

    @FindBy(css = "[data-name='Active Cart']")
    private WebElement activeCart;

    @FindBy(name = "quantity")
    private List<WebElement> quantitySelect;

    @FindBy(css = "input[value='Delete']")
    private List<WebElement> deleteLinks;

    public CartComponent(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    public void goToCart() {
        goToCartButtoon.click();
    }

    public void emptyCart() {
        if (Integer.parseInt(numberOfItemsInCart.getText()) > 0) {
            goToCart();
            for (int i = 0; i < productsInCart.size(); i++) {
                deleteProductFromCartByIndex(i + 1);
            }
        }
    }

    public int getNumberOfProductsInCart() {
        return Integer.parseInt(numberOfItemsInCart.getText());
    }

    public String getProductTitleInCartByIndex(int index) {
        return listItemContentDiv.get(index - 1).findElement(By.tagName("li")).getText().trim();
    }

    public String getProductPriceInCartByIndex(int index) {
        return listItemContentDiv.get(index - 1).findElement(By.cssSelector("[class*='price']")).getText().trim();
    }

    public int getProductQuantityInCartByIndex(int index) {
        Select select = new Select(quantitySelect.get(index - 1));
        return Integer.parseInt(select.getFirstSelectedOption().getText().trim());
    }

    public void deleteProductFromCartByTitle(String productTitle) {
        if (getProductIndexByTitle(productTitle).isPresent()) {
            deleteLinks.get(getProductIndexByTitle(productTitle).get()).click();
        } else {
            log.severe("No delete button found for product title!");
        }
    }

    public boolean isProductDeleted(String productTitle) {
        if (getProductIndexByTitle(productTitle).isPresent()) {
            try {
                int i = getProductIndexByTitle(productTitle).get();
                CustomWait.waitUntilTextIsPresentInElement(deletedItem.get(i), "was removed from Shopping Cart",
                        driver);
                return true;
            } catch (Exception ex) {
                try {
                    if (activeCart.getText().contains("Your Shopping Cart is empty.")) {
                        return true;
                    }
                } catch (Exception exc) {
                    return false;
                }
            }
        }
        return false;
    }

    private void deleteProductFromCartByIndex(int index) {
        deleteLinks.get(index - 1).click();
    }

    private Optional<Integer> getProductIndexByTitle(String productTitle) {
        for (int i = 0; i < listItemContentDiv.size(); i++) {
            if (listItemContentDiv.get(i).findElement(By.tagName("li")).getText().contains(productTitle)) {
                return Optional.of(i);
            }
        }
        log.severe("Cannot find the product in cart!");
        return Optional.empty();
    }
}
