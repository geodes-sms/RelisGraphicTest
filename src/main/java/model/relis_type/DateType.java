package model.relis_type;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class DateType extends TypeOf {
    @Override
    public Object getMockValue() {
        return "2021-10-22";
    }

    //date_1
    public  void sendValueToDOM(WebDriver driver,WebElement input, String value){
        ((JavascriptExecutor)driver).executeScript
                ("document.getElementById('date_1').removeAttribute('readonly',0);"); // Enables the from date box

        input.findElement(By.id("date_1")).sendKeys(value);
    }
}

