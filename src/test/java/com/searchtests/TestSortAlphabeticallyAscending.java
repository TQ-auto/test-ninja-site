package com.searchtests;

import com.enums.OrderingBy;
import com.pages.HomePage;
import com.pages.SearchPage;
import com.testbases.TestBase;
import static org.testng.Assert.assertTrue;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;


public class TestSortAlphabeticallyAscending extends TestBase {

    HomePage homePage;
    SearchPage searchPage;

    @BeforeTest
    public void setUpTest(){
        homePage = new HomePage(driver);
    }

    @Parameters({"search-term"})
    @Test(description = "Search for a product",priority = 1)
    public void searchForProduct(@Optional("iPod") String searchTerm){
        homePage.navigate();
        homePage.search(searchTerm);
        searchPage = new SearchPage(driver);
        assertTrue(searchPage.getPageContentTitle().contains(searchTerm),
                String.format("Failed to search for %s",searchTerm));
    }

    @Test(description = "Test list view products for searched term", priority = 2)
    public void testListProductsView(){
        searchPage.clickListButton();
        assertTrue(searchPage.isProductsListView(),"Products list wasn't changed to list view.");
    }

    @Test(description = "Test sorted searched list by name ascending",priority = 3)
    public void testSortedListByNameAsc(){
        searchPage.sortProductsByName(OrderingBy.ASC);
        assertTrue(searchPage.isSortedByNameAscending(OrderingBy.ASC),"Products not sorted by name from A to Z.");
    }

    @Test(description = "Test sorted searched list by name descending",priority = 4)
    public void testSortedListByNameDesc(){
        searchPage.sortProductsByName(OrderingBy.DESC);
        assertTrue(searchPage.isSortedByNameAscending(OrderingBy.DESC),"Products not sorted by name from Z to A.");
    }

}
