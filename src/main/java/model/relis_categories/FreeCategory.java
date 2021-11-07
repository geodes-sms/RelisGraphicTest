package model.relis_categories;

import lombok.Data;
import lombok.ToString;

import model.Paper;
import model.Value;
import model.relis_categories.Category;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

@Data
@ToString
public class FreeCategory  extends Category {

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

    @Override
    public String getContentValue() {
        return value.getValue();
    }
}
