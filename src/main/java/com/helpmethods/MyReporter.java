package com.helpmethods;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.testng.Reporter;

import java.io.File;
import java.io.IOException;
import java.time.ZoneId;

public class MyReporter {

    public static void log(String message){
        java.time.LocalDateTime.now();
        Reporter.log(String.format("[%s]: %s",java.time.LocalDateTime.now(),message),true);
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
