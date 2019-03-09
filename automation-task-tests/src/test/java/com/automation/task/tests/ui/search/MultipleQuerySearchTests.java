package com.automation.task.tests.ui.search;

import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.automation.task.tests.ui.base.BaseUiTest;
import com.automation.task.tests.ui.componentobjects.search.SearchComponent;
import com.automation.task.tests.ui.componentobjects.search.SearchResultsComponent;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;

public class MultipleQuerySearchTests extends BaseUiTest {

    private static SearchComponent search;
    private static SearchResultsComponent searchResults;

    private static final String CATEGORY_QUERY_BOOK = "book";
    private static final String BOOK_KW_1 = "haruki";
    private static final String BOOK_KW_2 = "murakami";

    private static final String BOOK_RESULT_KW_1 = "paperback";
    private static final String BOOK_RESULT_KW_2 = "hardcover";

    @BeforeEach
    void beforeMethod() {
        search = new SearchComponent(driver);
        searchResults = new SearchResultsComponent(driver);
    }

    @ParameterizedTest
    @CsvSource({
            "nike, air, shoes",
            "Stephan, King, paperback",
            "little, big, planet",
            "coffee, cup, warmer"
    }) // Task notes: not a good solution, as long as not compared to pure search engine results
    void shouldDisplayRelatedSearchResults(String keyword1, String keyword2, String keyword3) {
        search.search(combineKeywords(keyword1, keyword2, keyword3));

        Optional<List<String>> searchResultsList = searchResults.getSearchResults();

        assertTrue(searchResultsList.isPresent());
        searchResultsList.get().forEach(result ->
                assertTrue(contains(result, keyword1)
                        || contains(result, keyword2)
                        || contains(result, keyword3)));
    }

    @Disabled("Task notes: Time Consuming - disabled for demo")
    @ParameterizedTest
    @MethodSource("queryWithDifferentCaseCombinations")
    void shouldDisplayResultsForDifferentCaseCombinations(String query) {
        search.search(query);

        Optional<List<String>> searchResultsList = searchResults.getSearchResults();

        assertTrue(searchResultsList.isPresent());
        assertAll(
                () -> searchResultsList.get().forEach(result ->
                        assertTrue(contains(result, BOOK_KW_1)
                                && contains(result, BOOK_KW_2))),
                () -> searchResultsList.get().forEach(result ->
                        assertTrue(contains(result, BOOK_RESULT_KW_1)
                                || contains(result, BOOK_RESULT_KW_2)
                                || contains(result, CATEGORY_QUERY_BOOK))));
    }

    @Test // Task notes: has three different designs, kept as a general result
    void shouldAutoCorrectAllKeywordsByDefault() {
        String keywordWithTypo1 = "haruk";
        String keywordWithTypo2 = "mrakami";
        String keywordWithTypo3 = "zook";

        search.search(combineKeywords(keywordWithTypo1, keywordWithTypo2, keywordWithTypo3));
        assertTrue(searchResults.pageContainsText(combineKeywords(BOOK_KW_1, BOOK_KW_2, CATEGORY_QUERY_BOOK).toLowerCase()));
    }

    @Test
    void shouldDisplayNoResults() {
        String keyword = randomAlphabetic(10);
        String query = combineKeywords(keyword, keyword, keyword);

        search.search(query);
        assertTrue(searchResults.pageContainsText("did not match any products") || searchResults
                .pageContainsText("No results for "));
    }

    private static String combineKeywords(String keyword1, String keyword2, String keyword3) {
        StringBuilder sb = new StringBuilder();
        sb
                .append(keyword1)
                .append(" ")
                .append(keyword2)
                .append(" ")
                .append(keyword3);
        return sb.toString();
    }

    private static Stream<Arguments> queryWithDifferentCaseCombinations() {
        return Stream.of(
                Arguments.of(combineKeywords(BOOK_KW_1, BOOK_KW_2, CATEGORY_QUERY_BOOK).toLowerCase()),
                Arguments.of(combineKeywords(BOOK_KW_1, BOOK_KW_2, CATEGORY_QUERY_BOOK).toUpperCase()),
                Arguments
                        .of(combineKeywords(BOOK_KW_1.toLowerCase(), BOOK_KW_2.toUpperCase(),
                                CATEGORY_QUERY_BOOK.toLowerCase())));
    }

    private static boolean contains(String text, String keyword) {
        return text.toLowerCase().contains(keyword.toLowerCase());
    }
}
