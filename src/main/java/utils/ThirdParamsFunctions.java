package utils;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import utils.Functions;

import java.util.List;

public interface ThirdParamsFunctions extends Functions {

    Integer apply(WebDriver driver, List<WebElement> l1, String subject);


}
