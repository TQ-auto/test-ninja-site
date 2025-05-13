package com.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public abstract class PageBase {

    protected WebDriver driver;
    protected WebDriverWait webDriverWait;
    protected final String baseUrl = "https://www.saucedemo.com/";

    protected PageBase(WebDriver driver){
        this.driver = driver;
        webDriverWait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    protected abstract void navigate();

}
