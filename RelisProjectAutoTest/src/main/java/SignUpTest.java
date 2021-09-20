import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class SignUpTest implements ITest {


  private static final String INIT_PATH = "fa-user";
  private static final String name_userName = "user_name";

  private static final String name_userpass = "user_password";

  private static final String name_validate_pass= "user_password_validate";

  private static final String name_userEmail = "user_mail";

  private static final String name_username_field  = "user_username";

  private static final String id_notARobot ="rc-anchor-container";

  private static final String class_submit_button = "submit";


  @Override
  public void process(WebDriver driver){

    driver.findElement(By.className(INIT_PATH)).click();

    driver.findElement(By.name(name_userName)).sendKeys("Mahamat Youssouf Issa");

  }



}
