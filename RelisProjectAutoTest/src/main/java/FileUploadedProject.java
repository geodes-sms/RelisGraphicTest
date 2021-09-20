import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import java.io.File;

public class FileUploadedProject extends CreateNewProject{



  protected static final String linkTest_UploadMode= "Upload configuration file";

  protected static final String name_chooseFile = "install_config";

  protected File mt_file = new File("src/main/resources/mt.relis");


  @Override
  public void process(WebDriver driver) {
    super.process(driver);

    driver.findElement(By.linkText(linkTest_UploadMode)).click();

    driver.findElement(By.name(name_chooseFile)).sendKeys(mt_file.getAbsolutePath());

    driver.findElement(By.cssSelector("button[type='submit']")).click();



  }
}
