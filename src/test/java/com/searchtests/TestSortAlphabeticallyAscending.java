package com.searchtests;

import com.helpclasses.Product;
import com.helpmethods.MyReporter;
import com.pages.HomePage;
import com.pages.SearchPage;
import com.testbases.TestBase;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.util.List;

public class TestSortAlphabeticallyAscending extends TestBase {

    HomePage homePage;
    SearchPage searchPage;
    String keyword = "iPod";

    @BeforeTest
    public void setUpTest(){
        homePage = new HomePage(driver);
    }

    @Test(description = "Search for 'iPod' product",priority = 1)
    public void searchForIPodProduct(){
        homePage.navigate();
        homePage.search(keyword);
        searchPage = new SearchPage(driver);
        Assert.assertTrue(searchPage.getPageContentTitle().contains(keyword),
                String.format("Failed to search for %s",keyword));
    }

    @Test(description = "List 'iPod' products", priority = 2)
    public void listIPodProducts(){
        searchPage.clickListButton();
        Assert.assertTrue(searchPage.isProductsListView(),"Products list wasn't changed to list view.");
    }

    @Test(description = "Sort 'iPod' list by name",priority = 3)
    public void sortIPodListByName(){
        Assert.assertTrue(searchPage.isSortedByName(),"Products not sorted by name.");
    }

    @Test(description = "Ipod storage", priority = 4)
    public void ipodStorage(){
        List<Product> productsList = searchPage.getProducts();
        double max = productsList.get(0).getPrice();
        double min = productsList.get(0).getPrice();
        Product minProduct = productsList.get(0);
        Product maxProduct = productsList.get(0);
        for(Product p: productsList){
            if(p.getPrice() > max) {
                max = p.getPrice();
                maxProduct = p;
            }
            if(p.getPrice() < min){
                min = p.getPrice();
                minProduct = p;
            }
        }
        MyReporter.log("Max ipod price: " + maxProduct.toString());
        MyReporter.log("Min ipod price: " + minProduct.toString());
    }
}
