package com.testbases;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.ITestResult;
import org.testng.Reporter;
import org.testng.annotations.*;

import java.io.File;
import java.io.IOException;
import java.time.Duration;
import java.time.ZoneId;

public abstract class TestBase {

    protected WebDriver driver;
    protected WebDriverWait webDriverWait;
    private final String chromeDriverPath = System.getProperty("user.dir")+"\\src\\main\\resources\\chromedriver.exe";

    @BeforeClass
    protected void setupChromeWebdriver(){
        // Auto install last version of chromedriver
        WebDriverManager.chromedriver().setup();
    }
    @BeforeTest
    protected void setupTestBase(){
//        System.setProperty("webdriver.chrome.driver", chromeDriverPath);

        Reporter.log("Launching browser...");
        driver = new ChromeDriver();
        Reporter.log("browser launched.");
        webDriverWait = new WebDriverWait(driver, Duration.ofSeconds(10));
        driver.manage().window().maximize();
    }

    @AfterMethod
    protected void wrapUp(ITestResult result) throws IOException {
        if (result.getStatus() == ITestResult.FAILURE){
            takeScreenshot(this.driver, result.getName());
        }
    }

    @AfterTest
    protected void quitBrowser(){
        if (driver != null){
            Reporter.log("Closing Browser...");
            driver.quit();
            Reporter.log("Browser closed.");
        }
    }


    public static void takeScreenshot(WebDriver driver, String testName) throws IOException {
        File scrFile = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
        FileUtils.copyFile(scrFile,
                new File(
                        String.format("%s\\tmp\\%s-%s.png",
                                System.getProperty("user.dir"),
                                java.time.LocalDateTime.now().atZone(ZoneId.systemDefault()).toEpochSecond(),
                                testName)
                ));
    }
}
