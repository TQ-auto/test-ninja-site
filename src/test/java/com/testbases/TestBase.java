package com.testbases;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.ITestResult;
import org.testng.annotations.*;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.time.Duration;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public abstract class TestBase {
    // Ensures thread safety
    private static ThreadLocal<WebDriver> driver = new ThreadLocal<>();
    protected WebDriverWait webDriverWait;
    // Url for selenium grid hub
    private static final String URL = "http://localhost:4444/wd/hub";
    // To run tests locally, turn this flag on (true = running locally / false = remotely)
    private static final boolean DEBUG_LOCALLY_FLAG = false;

    // For local runs only
    @BeforeClass
    @Parameters("browser")
    protected void autoSetupDriver(@Optional("chrome")String browser) throws Exception {
        // If DEBUG mode is on, run this
        if(DEBUG_LOCALLY_FLAG) {
            switch (browser) {
                case "chrome":
                    WebDriverManager.chromedriver().setup();
                    break;
                case "firefox":
                    WebDriverManager.firefoxdriver().setup();
                    break;
                case "edge":
                    WebDriverManager.edgedriver().setup();
                    break;
                default:
                    throw new Exception(String.format("Auto setup for %s browser not supported",browser));
            }
        }
    }

    @BeforeTest
    @Parameters("browser")
    protected void setupTestBase(@Optional("chrome")String browser) throws Exception {
        if(DEBUG_LOCALLY_FLAG){
            driver.set(getLocalDriverObject(browser));
            getDriver().manage().window().maximize();
        }
        if(!DEBUG_LOCALLY_FLAG){
            driver.set(getRemoteDriverObject(browser));
        }
        if(getDriver() == null){
            throw new Exception("Driver was not initialized");
        }
        webDriverWait = new WebDriverWait(getDriver(), Duration.ofSeconds(10));
    }

    @AfterMethod
    protected void takeScreenshotsOfFailedMethod(ITestResult result) throws IOException {
        if (result.getStatus() == ITestResult.FAILURE){
            takeScreenshot(getDriver(), result.getName());
        }
    }

    @AfterClass
    protected void wrapUp(){
        driver.get().close();
        driver.remove();
    }

    public WebDriver getDriver(){
        return driver.get();
    }

    private static void takeScreenshot(WebDriver driver, String testName) throws IOException {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy,HH-mm-ss-SSS");
        File scrFile = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
        FileUtils.copyFile(scrFile,
                new File(
                        String.format("%s\\screenshots\\%s-%s.png",
                                System.getProperty("user.dir"),
                                java.time.LocalDateTime.now().atZone(ZoneId.systemDefault()).format(dateTimeFormatter),
                                testName)
                ));
    }

    private static WebDriver getLocalDriverObject(String browser) throws Exception {
        return switch (browser) {
            case "chrome" -> {
                ChromeOptions chromeOptions = new ChromeOptions();
                yield new ChromeDriver(chromeOptions);
            }
            case "firefox" -> {
                FirefoxOptions firefoxOptions = new FirefoxOptions();
                yield new FirefoxDriver(firefoxOptions);
            }
            case "edge" -> {
                EdgeOptions edgeOptions = new EdgeOptions();
                yield new EdgeDriver(edgeOptions);
            }
            default -> throw new Exception("Browser not supported for local run.");
        };
    }

    private static WebDriver getRemoteDriverObject(String browser) throws Exception {
        return switch (browser) {
            case "chrome" -> new RemoteWebDriver(new URL(URL), new ChromeOptions().addArguments("start-maximized")
            .addArguments("ignore-certificate-errors")
            .addArguments("--disable-dev-shm-usage")
            .addArguments("--no-sandbox")
            .addArguments("--disable-popup-blocking"));
            case "firefox" -> new RemoteWebDriver(new URL(URL), new FirefoxOptions());
            case "edge" -> new RemoteWebDriver(new URL(URL), new EdgeOptions());
            default -> throw new Exception("Browser not supported for remote run.");
        };
    }

}
