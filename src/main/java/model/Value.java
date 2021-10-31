package model;


import lombok.Data;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

@Data
public class Value  implements Cloneable{

    TypeOf type;

    private String value;

    public void setMockValue(){
       value =  type.getMockValue().toString();
    }
    public void sendValueToDOM(WebDriver driver,WebElement input){
        type.sendValueToDOM(driver,input,value);
    }


    public Object clone() throws CloneNotSupportedException {
        return (Value) super.clone();
    }


}
