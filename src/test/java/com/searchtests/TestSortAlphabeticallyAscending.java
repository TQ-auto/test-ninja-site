package com.searchtests;

import com.enums.OrderingBy;
import com.pages.HomePage;
import com.pages.SearchPage;
import com.testbases.TestBase;
import static org.testng.Assert.assertTrue;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;


public class TestSortAlphabeticallyAscending extends TestBase {

    HomePage homePage;
    SearchPage searchPage;

    @Parameters({"search-term"})
    @Test(description = "Search for a product")
    public void searchForProduct(@Optional("iPod") String searchTerm){
        homePage = new HomePage(getDriver());
        homePage.navigate();
        homePage.search(searchTerm);
        searchPage = new SearchPage(getDriver());
        assertTrue(searchPage.getPageContentTitle().contains(searchTerm),
                String.format("Failed to search for %s",searchTerm));
        searchPage.clickListButton();
        assertTrue(searchPage.isProductsListView(),"Products list wasn't changed to list view.");
        searchPage.sortProductsByName(OrderingBy.ASC);
        assertTrue(searchPage.isSortedByNameAscending(OrderingBy.ASC),"Products not sorted by name from A to Z.");
        searchPage.sortProductsByName(OrderingBy.DESC);
        assertTrue(searchPage.isSortedByNameAscending(OrderingBy.DESC),"Products not sorted by name from Z to A.");
    }

}
