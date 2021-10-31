package model;

import databases.DataBase;
import lombok.Data;
import lombok.ToString;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import view.ClassificationView;

import java.util.Random;

import static utils.ClassificationUtils.CLASS_SWITCH_CKECKBOX;
import static utils.ClassificationUtils.CSS_INPUT_TEXT;

@Data
@ToString
public class FreeCategory  extends Category{

    private Value value;

    public FreeCategory(){
        this.setCategoryName("Simple");
    }
    public String toString(){

        String base = super.toString();
        base += " " + value;
        return base;
    }

    @Override
    public void classifyData(Paper paper) {

        value.setMockValue();;
    }

    @Override
    public String displayDataContent(){
        return super.displayDataContent() +" =>" + value;
    }

    public void fillWebElementInput(WebDriver driver, WebElement input){
        String labelCategory = input.findElement(By.tagName("label")).getText();
        String text  = getDisplayName() + (isMandatory() ? " *":"");
        System.out.println("comparaision(" + labelCategory +" , VS , " + text+")");
        assert  labelCategory.equals(text);
        value.sendValueToDOM(driver,input);
    }
}
