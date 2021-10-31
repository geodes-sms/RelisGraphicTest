package model;

import lombok.Data;
import lombok.ToString;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import view.ClassificationView;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Random;
@Data
@ToString
public class DependantDynamicCategory extends MultipleValue {

    public DependantDynamicCategory(){

        this.setCategoryName("DynamicList");
    }

    private MultipleValue dependent_on ;



    public void choiceSomeValues(){
        Random random =new Random();
        if(userChoices == null) userChoices = new ArrayList<>();
        if(dependent_on == null) return;
        ArrayList<String> values = dependent_on.getUserChoices();
        if(getNumberofValue() == 1 || getNumberofValue()  == 0) {
            String val = values.get(random.nextInt(0, values.size()));

            getUserChoices().add(val);
            return;
        }


        if(values.size() == 0) return;
        int numb = Math.min(getNumberofValue(), values.size());

        if(numb != 1) numb = random.nextInt(1,numb);
        ArrayList<String> copiedValues = new ArrayList<>(values);
        for (int i = 0; i < numb; i++) {

            getUserChoices().add(copiedValues.remove(random.nextInt(0,copiedValues.size())));
        }
    }


    public String toString(){

        return super.toString() +"\n depen_on=>[\t" + dependent_on+"\n\t]" ;
    }

    @Override
    public void classifyData(Paper paper) {
        choiceSomeValues();
    }

//    public void fillWebElementInput(WebDriver driver, WebElement input){
//        String labelCategory = input.findElement(By.tagName("label")).getText();
//
//        ClassificationView.chooseEntryOptions(driver,input, classificatedPaper.getCategoryValue(labelCategory));
//    }


    @Override
    public Object clone() {
        DependantDynamicCategory dependantDynamicCategory = new DependantDynamicCategory();
        dependantDynamicCategory.setDisplayName(getDisplayName());
        dependantDynamicCategory.setMandatory(isMandatory());
        dependantDynamicCategory.setIdentifier(getIdentifier());
        dependantDynamicCategory.setValues(new ArrayList<>(getValues()));
        ///dependantDynamicCategory.setUserChoices(new ArrayList<>(getUserChoices()));
        ArrayList<Category> sub = new ArrayList<>();
        getSubCategorys().forEach( category1 -> sub.add((Category) category1.clone()));
        dependantDynamicCategory.setSubCategorys(sub);
        dependantDynamicCategory.setDependent_on(dependent_on);

        return dependantDynamicCategory;
    }
}
