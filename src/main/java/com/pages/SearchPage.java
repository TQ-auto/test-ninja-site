package com.pages;

import com.helpclasses.Product;
import com.helpmethods.MyReporter;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;

import java.util.ArrayList;
import java.util.List;

public class SearchPage extends PageBase{

    final private String url = baseUrl + "index.php?route=product/search";

    @FindBy(how = How.XPATH, using = "//*[@id=\"content\"]/h1")
    WebElement pageContentTitle;

    @FindBy(how = How.ID, using = "list-view")
    WebElement listButton;

    @FindBy(how = How.ID, using = "input-sort")
    WebElement sortDropdown;

    public SearchPage(WebDriver driver){
        super(driver);
        PageFactory.initElements(driver,this);
    }

    public String getPageContentTitle() {
        MyReporter.log("Getting page content title...");
        return webDriverWait.until(ExpectedConditions.visibilityOf(pageContentTitle)).getText();
    }

    public void clickListButton(){
        MyReporter.log("Clicking list button...");
        webDriverWait.until(ExpectedConditions.elementToBeClickable(listButton)).click();
        webDriverWait.until(ExpectedConditions.attributeContains(listButton,"class","active"));
        MyReporter.log("List button was clicked.");
    }

    public List<WebElement> getProductsElements(){
        List<WebElement> productsElements =
                driver.findElements(By.xpath("//*[@id=\"content\"]/div[3]/div"));
        return productsElements;
    }

    public boolean isProductsListView(){
        MyReporter.log("Getting products as elements...");
        List<WebElement> productsElements = getProductsElements();
        for(WebElement e: productsElements)
            if(!e.getAttribute("class").contains("product-list"))
                return false;
        return true;
    }

    public void sortProductsByName(){
        Select dropdown = new Select(webDriverWait.until(ExpectedConditions.visibilityOf(sortDropdown)));
        dropdown.selectByVisibleText("Name (A - Z)");
    }

    public boolean isSortedByName(){
        sortProductsByName();
        List<WebElement> productsElements = getProductsElements();
        String temp = "";
        for(WebElement e : productsElements){
            String txtToBeCompared = e.findElement(By.xpath("//div[1]/h4/a")).getText();
            if(txtToBeCompared.compareTo(temp) < 0 )
                return false;
            temp = txtToBeCompared;
        }
        return true;
    }

    public List<Product> getProducts(){
        List<WebElement> productsElements = getProductsElements();
        List<Product> productsList = new ArrayList<>();
        for (WebElement productElement : productsElements){
            String name = productElement.findElement(By.xpath("//div[1]/h4/a"))
                    .getText().replace("iPod","");
            String imageUrl = productElement.findElement(By.xpath("//div[1]/a")).getAttribute("href");
            String description = productElement.findElement(By.xpath("//div[1]/p[1]")).getText();
            String spanInsidePrice = productElement.findElement(By.xpath("//div[1]/p[2]/span"))
                    .getAttribute("innerHTML");
            MyReporter.log("spanInsidePrice = "+ spanInsidePrice);
            double price = Double.parseDouble(
                    productElement.findElement(
                            By.xpath("//div[1]/p[2]")).getText().replace(spanInsidePrice,"")
                            .replace("$","")
                            );
            productsList.add(new Product(name,imageUrl,description,price));
        }
        return productsList;
    }

    @Override
    protected void navigate() {

    }
}
