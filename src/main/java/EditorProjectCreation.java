import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class EditorProjectCreation extends CreateNewProject{



  protected static final String link_test_EditorOpenButton = "Open editor";


  @Override
  public void process(WebDriver driver) {
    super.process(driver);

    // open the editor interface
    driver.findElement(By.linkText(link_test_EditorOpenButton)).click();
    // switch to the editor iframe
    driver.switchTo( ).frame(0);


  }
}
