package com.automation.task.tests.ui.cart;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.automation.task.tests.ui.base.BaseUiTest;
import com.automation.task.tests.ui.componentobjects.cart.AddToCartComponent;
import com.automation.task.tests.ui.componentobjects.cart.CartComponent;
import com.automation.task.tests.ui.componentobjects.login.LoginComponent;
import com.automation.task.tests.ui.componentobjects.product.ProductComponent;
import com.automation.task.tests.ui.componentobjects.search.SearchComponent;
import com.automation.task.tests.ui.componentobjects.search.SearchResultsComponent;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class RemoveFromCartComponentTests extends BaseUiTest {

    private static LoginComponent login;
    private static SearchComponent search;
    private static SearchResultsComponent searchResults;
    private static ProductComponent product;
    private static AddToCartComponent addToCart;
    private static CartComponent cart;

    @BeforeEach
    void beforeMethod() {
        login = new LoginComponent(driver);
        search = new SearchComponent(driver);
        searchResults = new SearchResultsComponent(driver);
        product = new ProductComponent(driver);
        addToCart = new AddToCartComponent(driver);
        cart = new CartComponent(driver);

        login.login();
        cart.emptyCart();
    }

    @AfterEach
    void afterMethod() {
        cart.emptyCart();
    }

    @Test
    void shouldRemoveProductFromCart() {
        String productTitle = addRandomItemToCart("vase", 1);

        cart.goToCart();
        cart.deleteProductFromCartByTitle(productTitle);
        assertTrue(cart.isProductDeleted(productTitle));
    }

    @Test
    void shouldRemoveMultipleProductsAndQuantitiesFromCart() {
        String productTitle1 = addRandomItemToCart("Flowerpot Treeman Baby Groot Succulent Planter", 1);
        String productTitle2 = addRandomItemToCart("coffee mugs", 3);

        cart.goToCart();
        cart.deleteProductFromCartByTitle(productTitle1);
        cart.deleteProductFromCartByTitle(productTitle2);

        driver.navigate().refresh();
        assertEquals(0, cart.getNumberOfProductsInCart());
    }

    private static String addRandomItemToCart(String query, int quantity) {
        search.search(query);
        searchResults.clickOnSearchResultByIndex(1);

        String productTitle = product.getProductTitle();

        addToCart.selectQuantity(quantity);
        addToCart.clickAddToCartButton();
        assertTrue(addToCart.isAddToCartSuccessfulMessageDisplayed());
        return productTitle;
    }
}
