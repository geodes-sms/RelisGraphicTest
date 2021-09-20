import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class CreateNewProject  implements  ITest {


  protected static final String link_test_addNewProjectButton = "Add new project";
  @Override
  public void process(WebDriver driver) {


    driver.findElement(By.linkText(link_test_addNewProjectButton)).click();

  }
}
