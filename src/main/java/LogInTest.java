/*

@author Mahamat Youssouf Issa
@version 1.0
 */

import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class LogInTest implements ITest{




  private static final String INIT_PATH = "fa-sign-in";

  private static final String name_userNameField = "user_username";
  private static final String name_userPassField = "user_password";
  private static final String class_LogInButton = "submit";


  private static final String MOCK_USER_NAME = "youssouf1";
 private static final String MOCK_USER_PASS = "azerty10";
// private static final String MOCK_USER_NAME = "admin";
// private static final String MOCK_USER_PASS = "123";

  @Override
  public void process(WebDriver driver) {

    // on navigue vers la page de login
    driver.findElement(By.className(INIT_PATH)).click();
    // on remplie le champs username
    driver.findElement(By.name(name_userNameField)).sendKeys(MOCK_USER_NAME);
    // remplir le champs du mot de passe
    driver.findElement(By.name(name_userPassField)).sendKeys(MOCK_USER_PASS);

     MainTest.sleep(4);

    // on se connecter
    driver.findElement(By.className(class_LogInButton)).click();

    // closing the current windows


    MainTest.sleep(3);



  }
}
