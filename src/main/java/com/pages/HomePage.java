package com.pages;

import com.helpmethods.MyReporter;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;

public class HomePage extends PageBase{

    private final String url = baseUrl;

    @FindBy(how = How.NAME,using = "search")
    WebElement searchBox;

    @FindBy(how = How.XPATH, using = "//*[@id=\"search\"]/span/button")
    WebElement searchButton;

    public HomePage(WebDriver driver){
        super(driver);
        PageFactory.initElements(driver,this);
    }

    public void passSearchText(String keyword){
        MyReporter.log(String.format("Passing '%s' to search box...",keyword));
        webDriverWait.until(ExpectedConditions.visibilityOf(searchBox));
        searchBox.sendKeys(keyword);
        webDriverWait.until(ExpectedConditions.attributeToBe(searchBox,"value",keyword));
        MyReporter.log(String.format("'%s' word passed to search box",keyword));
    }

    public void clickSearchButton(){
        MyReporter.log(String.format("Clicking search button..."));
        webDriverWait.until(ExpectedConditions.elementToBeClickable(searchButton)).click();
        MyReporter.log("Search button clicked");
    }

    public void search(String keyword){
        passSearchText(keyword);
        clickSearchButton();
    }

    @Override
    public void navigate() {
        driver.get(url);
    }

}
