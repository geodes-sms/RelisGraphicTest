package model;


import lombok.Data;
import model.relis_type.TypeOf;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

@Data
public class Value  implements Cloneable{

    TypeOf type;

    private String value;

    public void setMockValue(){

        if(value == null || value.equals("")) value = type.getMockValue().toString();
        //System.out.println("value={" + value+"}");
    }
    public void sendValueToDOM(WebDriver driver,WebElement input){
        type.sendValueToDOM(driver,input,value);
    }


    public Object clone() throws CloneNotSupportedException {
        return (Value) super.clone();
    }


}
