package com.automation.task.tests.ui.componentobjects.search;

import static org.awaitility.Awaitility.await;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;

import com.automation.task.tests.ui.base.UiPropertyReader;
import com.automation.task.tests.ui.custom.CustomClick;
import com.automation.task.tests.ui.custom.CustomWait;
import com.automation.task.tests.ui.custom.ElementExistsCheck;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;
import lombok.extern.java.Log;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

@Log
public class SearchResultsComponent {

    private final WebDriver driver;

    private static Optional<List<WebElement>> searchElement;
    private static int sponsoredItemCount;

    private static final String SPONSORED_HEADER_1_CSS = "[data-a-modal*='sponsored-products']";
    private static final String SPONSORED_HEADER_2_CSS = "[data-component-props*='sponsored-products']";

    @FindBy(css = "li[id^='result_']")
    private List<WebElement> suggestedSearchResults;

    @FindBy(id = "s-result-count")
    private WebElement searchResultCounter;

    @FindBy(css = "[data-component-type='s-result-info-bar']")
    private WebElement searchResultCounterBar;

    @FindBy(css = "[data-cel-widget^='search_result_']")
    private List<WebElement> searchResults;

    @FindBy(id = "leftNavContainer")
    private WebElement leftNavMenu;

    @FindBy(id = "noResultsTitle")
    private WebElement noResultsTitle;

    public SearchResultsComponent(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    public Optional<List<String>> getSearchResults() {
        waitUntilSearchResultsAreLoaded();
        List<String> searchResultsList = new ArrayList<>();

        if (searchElement.isPresent()) {
            sponsoredItemCount = 0;
            List<WebElement> search = searchElement.get();

            for (WebElement element : search) {
                // only adds to search results if element is not a sponsored element
                if (!ElementExistsCheck.isElementExistingOnPage(element, By.cssSelector(SPONSORED_HEADER_1_CSS))
                        &&
                        (!ElementExistsCheck.isElementExistingOnPage(element, By.cssSelector(SPONSORED_HEADER_2_CSS)))) {
                    searchResultsList.add(element.getText());
                } else {
                    sponsoredItemCount++;
                }
            }
        }
        return Optional.of(searchResultsList);
    }

    public boolean pageContainsText(String text) {
        return driver.findElements(By.xpath("//*[text()[contains(.,'" + text + "')]]")).size() > 0;
    }

    public void clickOnSearchResultByIndex(int index) {
        waitUntilSearchResultsAreLoaded();
        searchElement
                .ifPresent(webElements -> CustomClick
                        .jsClick(webElements.get(index - 1).findElement(By.tagName("img")), driver));
    }

    private void setSearchResultType() {
        if (ElementExistsCheck.isElementExistingOnPage(suggestedSearchResults)) {
            CustomWait.waitUntilElementIsClickable(suggestedSearchResults.get(0), driver);
            searchElement = Optional.of(suggestedSearchResults);
        } else if (ElementExistsCheck.isElementExistingOnPage(searchResults)) {
            CustomWait.waitUntilElementIsClickable(searchResults.get(0), driver);
            searchElement = Optional.of(searchResults);
        } else {
            log.info("Both search types do not exist on page!");
        }
    }

    private void waitUntilSearchResultsAreLoaded() {
        if (getExpectedNumberOfSearchResults().isPresent()) {
            await().atMost(UiPropertyReader.LOAD_TIMEOUT_IN_SECONDS, TimeUnit.SECONDS).until(searchTypeIsSet());

            await().atMost(UiPropertyReader.LOAD_TIMEOUT_IN_SECONDS, TimeUnit.SECONDS)
                    .until(searchElement.get()::size, greaterThanOrEqualTo(getExpectedNumberOfSearchResults().get()));
        } else {
            log.severe("Could not perform wait, since Expected Number Of Results is empty!");
        }
    }

    private Callable<Boolean> searchTypeIsSet() {
        return () -> {
            setSearchResultType();
            return searchElement.isPresent();
        };
    }

    private Optional<String> getSearchResultsCounterText() {
        return suggestedSearchResults.size() > 0 ? Optional.of(searchResultCounter.getText())
                : Optional.of(searchResultCounterBar.getText());
    }

    private Optional<Integer> getExpectedNumberOfSearchResults() {
        String splitterType1 = "1-";
        String splitterType2 = " ";

        if (getSearchResultsCounterText().isPresent()) {
            String resultCounterMessage = getSearchResultsCounterText().get();
            return resultCounterMessage.split(splitterType1).length == 1 ?
                    Optional.of(Integer.parseInt(resultCounterMessage.split(splitterType2)[0])) :
                    Optional.of(Integer.parseInt(resultCounterMessage.split(splitterType1)[1].split(splitterType2)[0]));
        }
        log.severe("Could not get expected number of search results!");
        return Optional.empty();
    }
}
