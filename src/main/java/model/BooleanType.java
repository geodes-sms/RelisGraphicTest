package model;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.Random;

import static utils.ClassificationUtils.CLASS_SWITCH_CKECKBOX;

public class BooleanType extends TypeOf{


    public Object getMockValue(){

        return new Random().nextBoolean();
    }


    @Override
    public void sendValueToDOM(WebDriver driver,WebElement input,String value){
      if(value.equals("true"))
          input.findElement(By.className(CLASS_SWITCH_CKECKBOX)).click();
    }
}
