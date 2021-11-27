package utils;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;

public class Initialiazer {

    // VARIABLES
    private static final String RELIS_URL = "http://relis.iro.umontreal.ca/auth.html";
    private static final String LOCAL_URL = "http://localhost:8083/auth.html";
    private WebDriver webDriver;

    // initializing the web page
    public void init(){
        // setup for using firefox as web navigator
        WebDriverManager.chromedriver().setup();
        ChromeOptions ChromeOptions = new ChromeOptions();
       // ChromeOptions.addArguments("--headless", "window-size=1024,768", "--no-sandbox");
         webDriver = new ChromeDriver(ChromeOptions);
        // go to the website
        webDriver.get(LOCAL_URL);
      // webDriver.manage().window().maximize();


    }

    // return the current web driver
    public WebDriver getWebDriver(){
        return webDriver;
    }


}
