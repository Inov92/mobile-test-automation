package com.epam.appium.tests;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Properties;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileElement;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.remote.MobileCapabilityType;
import org.apache.log4j.Logger;
import org.apache.log4j.LogManager;


public class SmokeTests {

    public static final String WELCOME_TEXT = "Hello";
    public static final String USER_NAME = "User";
    public static final String ALERT_TEXT = "Please enter a name!";

    private AndroidDriver<?> driver;

    private static final Logger log = LogManager.getLogger( SmokeTests.class);

    @BeforeMethod(description = "Setting up capabilities and device for application testing")
    public final void nativeSetup() throws IOException, InterruptedException {

        //This url will contain the Appium servers availability.
        URL url;

        //Set the properties file to be loaded into a Properties object, which will contain the capabilities by key, value pairs.
        InputStream input = null;
        Properties prop = new Properties();
        input = new FileInputStream("./src/test/resources/native_config.properties");
        prop.load(input);

        //Create the DesiredCapabilities object
        DesiredCapabilities capabilities = new DesiredCapabilities();

        //Set the application file
        File appDir = new File(System.getProperty("user.dir"));
        File app = new File(appDir, prop.getProperty("app"));

        //Set the actual capabilities
        //Set android deviceName desired capability and the udid as well
        capabilities.setCapability(MobileCapabilityType.DEVICE_NAME, prop.getProperty("deviceName"));
        capabilities.setCapability(MobileCapabilityType.UDID, prop.getProperty("udid"));

        //Set the platformName and platformVersion desired capability
        capabilities.setCapability(MobileCapabilityType.PLATFORM_NAME, prop.getProperty("platformName"));
        capabilities.setCapability(MobileCapabilityType.PLATFORM_VERSION, prop.getProperty("platformVersion"));

        //Set the app desired capability
        capabilities.setCapability(MobileCapabilityType.APP, app.getAbsolutePath());

        // Set android appPackage desired capability.
        capabilities.setCapability("appPackage", prop.getProperty("appPackage"));

        // Set how long (in seconds) Appium should wait for a new command from the client before assuming the client quit and ending the session
        capabilities.setCapability(MobileCapabilityType.NEW_COMMAND_TIMEOUT, prop.getProperty("newCommandTimeout"));

        //Set full-reset capability. It will reset app state by uninstalling app instead of clearing app data. On Android, this will also remove the app after the session is complete.
        capabilities.setCapability(MobileCapabilityType.FULL_RESET, "true");

        //Set the avd capability by checking if we want to run our test in an emulator. It will start the emulator if it's not started yet.
        if (prop.getProperty("useAvd").equals("true")) {
            capabilities.setCapability("avd", prop.getProperty("avd"));
        }
        try {
            //Set the Appium server url
            url = new URL(prop.getProperty("driverURL"));

            //Give the desired capabilities to the Appium server, starts the test running.
            driver = new AndroidDriver(url, capabilities);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    @Test(description = "Write @USER_NAME into text field and check welcome message")
    public final void nativeTest() {

        log.info("Wait for editText element to load");
        final WebDriverWait wait = new WebDriverWait(driver, 30);
        wait.until(ExpectedConditions.presenceOfElementLocated(
                By.id("com.epam.hello:id/editText")));

        final WebElement editText =
                driver.findElement(By.id("com.epam.hello:id/editText"));
        final WebElement okButton =
                driver.findElement(By.id("com.epam.hello:id/okButton"));
        final WebElement helloText =
                driver.findElement(By.id("com.epam.hello:id/helloText"));

        log.info("Write " + USER_NAME + " into editText");
        editText.click();
        editText.sendKeys(USER_NAME);

        log.info("Tap on the OK button");
        okButton.click();

        log.info("Check the welcome text: " + WELCOME_TEXT + " " + USER_NAME + "!");
        Assert.assertEquals(helloText.getText().toLowerCase(),
                (WELCOME_TEXT + " " + USER_NAME + "!").toLowerCase());

        log.info("Clear the editText field");
        editText.clear();

        log.info("Tap on the OK button");
        okButton.click();

        final WebElement alertText = driver.findElement(By.id("android:id/message"));

        log.info("Check if the alertText equals: " + ALERT_TEXT);
        Assert.assertEquals(alertText.getText().toLowerCase(), ALERT_TEXT.toLowerCase());
    }

    /** Tear down method for appium driver session
     *
     */
    @AfterTest(description = "Tear down after testing", alwaysRun = true)
    public final void tearDown() {
        log.info("Shutting down session");
        driver.quit();
    }
}
