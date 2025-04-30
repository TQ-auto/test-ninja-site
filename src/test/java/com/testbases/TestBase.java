package com.testbases;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.ITestResult;
import org.testng.Reporter;
import org.testng.annotations.*;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public abstract class TestBase {

    protected WebDriver driver;
    protected WebDriverWait webDriverWait;
    // Used to when running locally
    protected String url = "http://localhost:4444/wd/hub";
    // To run tests locally, turn this flag on (True = running locally)
    final boolean DEBUG_FLAG = false;

    @BeforeClass
    protected void SetUpAutoSetupChrome(){
        // If DEBUG mode is on, run this
        if(DEBUG_FLAG)
            // Webdrivermanager should be run on a @BeforeClass for the driver to work on the next step.
            WebDriverManager.chromedriver().setup();
    }

    @BeforeTest
    protected void setUpLocalTestBase(){
        // If DEBUG mode is on, run this
        if(DEBUG_FLAG){
            ChromeOptions chromeOptions = new ChromeOptions();
            Reporter.log("Launching browser...");
            driver = new ChromeDriver(chromeOptions);
            Reporter.log("browser launched.");
            webDriverWait = new WebDriverWait(driver, Duration.ofSeconds(10));
            driver.manage().window().maximize();
        }
    }

    @BeforeTest
    @Parameters("browser")
    protected void setupTestBase(@Optional("chrome")String browser) {
        if(!DEBUG_FLAG)
            try{
                if (browser.equalsIgnoreCase("chrome")){
                    driver = new RemoteWebDriver(new URL(url),new ChromeOptions());
                }
                else if(browser.equalsIgnoreCase("firefox")){
                    driver = new RemoteWebDriver(new URL(url),new FirefoxOptions());
                } else if(browser.equalsIgnoreCase("edge")){
                    driver = new RemoteWebDriver(new URL(url),new EdgeOptions());
                }
                webDriverWait = new WebDriverWait(driver, Duration.ofSeconds(10));
            } catch (MalformedURLException urlException){
                System.out.println("Malformed url, please check url.");
            }
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
            driver.quit();
        }
    }


    public static void takeScreenshot(WebDriver driver, String testName) throws IOException {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy,HH-mm-ss-SSS");
        File scrFile = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
        FileUtils.copyFile(scrFile,
                new File(
                        String.format("%s\\screenshots\\%s-%s.png",
                                System.getProperty("user.dir"),
                                java.time.LocalDateTime.now().atZone(ZoneId.systemDefault()).format(dateTimeFormatter).toString(),
                                testName)
                ));
    }
}
