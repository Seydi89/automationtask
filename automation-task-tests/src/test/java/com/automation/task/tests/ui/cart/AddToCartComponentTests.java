package com.automation.task.tests.ui.cart;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.automation.task.tests.ui.base.BaseUiTest;
import com.automation.task.tests.ui.componentobjects.cart.AddToCartComponent;
import com.automation.task.tests.ui.componentobjects.cart.CartComponent;
import com.automation.task.tests.ui.componentobjects.product.ProductComponent;
import com.automation.task.tests.ui.componentobjects.search.SearchComponent;
import com.automation.task.tests.ui.componentobjects.search.SearchResultsComponent;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class AddToCartComponentTests extends BaseUiTest {

    private static SearchComponent search;
    private static SearchResultsComponent searchResults;
    private static ProductComponent product;
    private static AddToCartComponent addToCart;
    private static CartComponent cart;

    @BeforeEach
    void beforeMethod() {
        search = new SearchComponent(driver);
        searchResults = new SearchResultsComponent(driver);
        product = new ProductComponent(driver);
        addToCart = new AddToCartComponent(driver);
        cart = new CartComponent(driver);

        cart.emptyCart();
    }

    @AfterEach
    void afterMethod() {
        cart.emptyCart();
    }

    @Test
    void shouldAddToCartSuccessfullyWithDefaultQuantity() {
        searchQueryAndClickResultByIndex("toys", 1);
        addToCart.clickAddToCartButton();
        assertTrue(addToCart.isAddToCartSuccessfulMessageDisplayed());

        cart.goToCart();
        assertAll(
                () -> assertEquals(1, cart.getProductQuantityInCartByIndex(1))
        );
    }

    @Test
    void shouldBeAbleToAddToCartWithCorrectInformation() {
        searchQueryAndClickResultByIndex("toys", 3);

        String productTitle = product.getProductTitle();
        String productPrice = product.getProductPrice();
        int quantity = 3;

        addToCart.addToCartWithQuantity(quantity);
        assertTrue(addToCart.isAddToCartSuccessfulMessageDisplayed());

        cart.goToCart();
        assertAll(
                () -> assertEquals(productPrice, cart.getProductPriceInCartByIndex(1)),
                () -> assertEquals(quantity, cart.getProductQuantityInCartByIndex(1)),
                () -> assertTrue(cart.getProductTitleInCartByIndex(1).startsWith(productTitle))
        );
    }

    @Test
    void shouldAddMultipleItemsToCart() {
        searchQueryAndClickResultByIndex("toys", 1);
        addToCart.addToCartWithQuantity(3);
        assertTrue(addToCart.isAddToCartSuccessfulMessageDisplayed());

        searchQueryAndClickResultByIndex("dresses", 1);
        addToCart.addToCartWithQuantity(1);
        assertTrue(addToCart.isAddToCartSuccessfulMessageDisplayed());

        cart.goToCart();
        assertEquals(4, cart.getNumberOfProductsInCart());
    }

    private static void searchQueryAndClickResultByIndex(String query, int index) {
        search.search(query);
        searchResults.clickOnSearchResultByIndex(index);
    }
}
