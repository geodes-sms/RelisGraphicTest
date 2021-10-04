import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;

public class MainTest {





  static RelisUser getMockUser(){

      RelisUser user = new RelisUser();
      user.setUser_email("mahamatyoussoufi@yahoo.com");
      user.setUsername("john2020");
      user.setPassword("mondaymyday");
      user.setFull_name("John Teller");
      return user;
  }



  public static void main(String[] args) {



//
//    Initialiazer init = new Initialiazer();
//    init.init();
//    WebDriver webDriver = init.getWebDriver();
//   Connexion connexion = new Connexion();
//  // connexion.registerUser(webDriver,getMockUser());
//   connexion.connect(webDriver,getMockConnectUser());
//    ProjectManager projectManager = new ProjectManager();
//
//    projectManager.openProject(webDriver, mt);

  }


  public static void sleep(long sec){

    try {
      Thread.sleep(sec*1000);
    } catch (Exception e ){

      System.err.println("Erreur survenue dans la methode Sleep : message => " + e.getMessage());
    }
  }





}
