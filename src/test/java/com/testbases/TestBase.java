package com.testbases;

import com.helpmethods.MyReporter;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;

import java.io.IOException;
import java.time.Duration;

public abstract class TestBase {

    protected WebDriver driver;
    protected WebDriverWait webDriverWait;
    private final String chromeDriverPath = System.getProperty("user.dir")+"\\src\\main\\resources\\chromedriver.exe";

    @BeforeTest
    protected void setupTestBase(){
        System.setProperty("webdriver.chrome.driver", chromeDriverPath);
        MyReporter.log("Launching browser...");
        driver = new ChromeDriver();
        MyReporter.log("browser launched.");
        webDriverWait = new WebDriverWait(driver, Duration.ofSeconds(10));
        driver.manage().window().maximize();
    }

    @AfterMethod
    protected void wrapUp(ITestResult result) throws IOException {
        if (result.getStatus() == ITestResult.FAILURE){
            MyReporter.takeScreenshot(this.driver, result.getName());
        }
    }

    @AfterTest
    protected void quitBrowser(){
        if (driver != null){
            MyReporter.log("Closing Browser...");
            driver.quit();
            MyReporter.log("Closed Browser.");
        }
    }
}
