import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

public class MainTest {


  private static final String RELIS_URL = "http://relis.iro.umontreal.ca/auth.html";
  private static final String LOCAL_URL = "http://localhost:8083/auth.html";

  public static void main(String[] args) {


    WebDriverManager.firefoxdriver().setup();

    WebDriver webDriver = new FirefoxDriver();

    webDriver.get(RELIS_URL);


    ITest test = new LogInTest();

    test.process(webDriver);

    sleep(4);
    new  FileUploadedProject().process(webDriver);
    sleep(3);
    webDriver.quit();
  }


  public static void sleep(long sec){

    try {
      Thread.sleep(sec*1000);
    } catch (Exception e ){

      System.err.println("Erreur survenue dans la methode Sleep : message => " + e.getMessage());
    }
  }

  // getInfo() => { e -> print(e); }
}
