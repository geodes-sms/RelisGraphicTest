import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;

public class MainTest {



    public static  String mt_project  = "src/main/resources/project/mt.php";
    public static String mt = "Model transformation";
  static RelisUser getMockUser(){

      RelisUser user = new RelisUser();
      user.setUser_email("mahamatyoussoufi@yahoo.com");
      user.setUsername("john2020");
      user.setPassword("mondaymyday");
      user.setFull_name("John Teller");
      return user;
  }

  static RelisUser getMockConnectUser(){

    RelisUser user = new RelisUser();
    user.setUsername("admin");
    user.setPassword("123");
    return user;
  }

  public static void main(String[] args) {




//    Initialiazer init = new Initialiazer();
//    init.init();
//    WebDriver webDriver = init.getWebDriver();
//   Connexion connexion = new Connexion();
//   connexion.registerUser(webDriver,getMockUser());
//   // connexion.connect(webDriver,getMockConnectUser());
//    ProjectManager projectManager = new ProjectManager();
//
//    projectManager.openProject(webDriver, mt);
//   // projectManager.uploadFromBibTeXPaper(webDriver,"src/main/resources/bibtex/one.bib")
//      // projectManager.createProject(webDriver, mt_project);
//      // projectManager.importBibTexPapers(webDriver,"");
//    projectManager.addReviewer(webDriver, new RelisUser());

    Utility.showAllRelisUser();
  }


  public static void sleep(long sec){

    try {
      Thread.sleep(sec*1000);
    } catch (Exception e ){

      System.err.println("Erreur survenue dans la methode Sleep : message => " + e.getMessage());
    }
  }





}
