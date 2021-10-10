package utils;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;

public interface FourthParamsFunctions extends Functions {

    Integer apply(WebDriver driver, List<WebElement> l1, String user,String user_role);

}
