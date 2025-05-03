package com.pages;

import com.enums.OrderingBy;
import com.helpclasses.Product;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.testng.Reporter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class SearchPage extends PageBase{

    final private String url = baseUrl + "index.php?route=product/search";

    @FindBy(how = How.XPATH, using = "//*[@id=\"content\"]/h1")
    WebElement pageContentTitle;

    @FindBy(how = How.ID, using = "list-view")
    WebElement listButton;

    @FindBy(how = How.ID, using = "input-sort")
    WebElement sortDropdown;

    By productDiv = By.xpath("//*[@id='content']/div[3]/div");

    public SearchPage(WebDriver driver){
        super(driver);
        PageFactory.initElements(driver,this);
    }

    public String getPageContentTitle() {
        Reporter.log("Getting page content title...");
        return webDriverWait.until(ExpectedConditions.visibilityOf(pageContentTitle)).getText();
    }

    public void clickListButton(){
        Reporter.log("Clicking list button...");
        webDriverWait.until(ExpectedConditions.elementToBeClickable(listButton)).click();
        webDriverWait.until(ExpectedConditions.attributeContains(listButton,"class","active"));
        Reporter.log("List button was clicked.");
    }

    public List<WebElement> getProductsElements(){
        return driver.findElements(productDiv);
    }

    public boolean isProductsListView(){
        Reporter.log("Getting products as elements...");
        List<WebElement> productsElements = getProductsElements();
        for(WebElement e: productsElements){
            if(!e.getAttribute("class").contains("product-list"))
                return false;
        }
        return true;
    }

    public void sortProductsByName(OrderingBy orderType){
        Select dropdown = new Select(webDriverWait.until(ExpectedConditions.visibilityOf(sortDropdown)));
        if(orderType.equals(OrderingBy.ASC)){
            dropdown.selectByVisibleText("Name (A - Z)");
            webDriverWait.until(ExpectedConditions.urlContains("order=ASC"));
        } else if (orderType.equals(OrderingBy.DESC)) {
            dropdown.selectByVisibleText("Name (Z - A)");
            webDriverWait.until(ExpectedConditions.urlContains("order=DESC"));
        }
    }


    public boolean isSortedByNameAscending(OrderingBy orderType){
        List<WebElement> productsElements = getProductsElements();
        if (productsElements.isEmpty() || productsElements.size() == 1){
            return true;
        }
        Iterator<WebElement> iter = productsElements.iterator();
        WebElement current, previous= iter.next();
        int nthChild=2;// number of div tag child
        while (iter.hasNext()){
            current = iter.next();
            // The tested site doesn't have test-ids or regular ids to use instead of xpath, full xpaths used instead.
            // Get name of current product on the list
            String currentProductNameXpath = "//*[@id='content']/div[3]/div["+nthChild+"]//div[1]/h4/a";
            String currentName = current.findElement(By.xpath(currentProductNameXpath)).getText();
            // Get name of previous product on the list
            String previousProductNameXpath = "//*[@id='content']/div[3]/div["+(nthChild-1)+"]//div[1]/h4/a";
            String previousName = previous.findElement(By.xpath(previousProductNameXpath)).getText();
            // Checks if current name is smaller (alphabetically) than the previous name on the list
            if(orderType.equals(OrderingBy.ASC)){
                if(previousName.compareTo(currentName) > 0){
                    return false;
                }
            }
            else // else if order type is descending.(Z to A)
                if(previousName.compareTo(currentName) < 0){
                    return false;
                }
            previous = current;
            nthChild++;
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
            double price = Double.parseDouble(
                    productElement.findElement(
                            By.xpath("//div[1]/p[2]")).getText().replace(spanInsidePrice,"")
                            .replace("$","")
                            );
            productsList.add(new Product(name,imageUrl,description,price));
        }
        return productsList;
    }

    public String getHighestStorageProduct(){
        List<Product> productsList = getProducts();
        double max = productsList.get(0).getPrice();
        Product maxProduct = productsList.get(0);
        for(Product p: productsList){
            if(p.getPrice() > max) {
                max = p.getPrice();
                maxProduct = p;
            }
        }
        return maxProduct.toString();
    }

    public String getLowestStorageProduct(){
        List<Product> productsList = getProducts();
        double min = productsList.get(0).getPrice();
        Product minProduct = productsList.get(0);
        for(Product p: productsList){
            if(p.getPrice() < min){
                min = p.getPrice();
                minProduct = p;
            }
        }
        return minProduct.toString();
    }

    @Override
    protected void navigate() {
        driver.get(url);
        webDriverWait.until(ExpectedConditions.visibilityOf(pageContentTitle));
    }
}
