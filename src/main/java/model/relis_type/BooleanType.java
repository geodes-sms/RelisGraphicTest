package model.relis_type;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.Random;

import static utils.ClassificationUtils.CLASS_SWITCH_CKECKBOX;

public class BooleanType extends TypeOf {


    public Object getMockValue(){
        boolean nextBool = new Random().nextBoolean();
        return  (nextBool)? "Yes" : "No";
    }


    @Override
    public void sendValueToDOM(WebDriver driver,WebElement input,String value){
      if(value.equals("Yes"))
          input.findElement(By.className(CLASS_SWITCH_CKECKBOX)).click();
    }
}
