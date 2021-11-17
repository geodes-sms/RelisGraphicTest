package model.relis_type;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import utils.Utility;

import static utils.ClassificationUtils.CSS_INPUT_TEXT;

public abstract class TypeOf implements Cloneable {


    public abstract Object getMockValue();

    public  void sendValueToDOM(WebDriver driver,WebElement input, String value){
        try {
            input.findElement(By.cssSelector(CSS_INPUT_TEXT)).clear();
            input.findElement(By.cssSelector(CSS_INPUT_TEXT)).sendKeys(value);

        } catch (Exception e){
            input.findElement(By.tagName("textarea")).sendKeys(value);
        }
    }

    @Override
    public TypeOf clone() {
        try {
            return (TypeOf) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }
}
