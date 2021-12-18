package model.relis_categories;

import lombok.Data;
import model.relis_categories.Category;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import utils.Utility;
import view.ClassificationView;

import java.util.ArrayList;
import java.util.Random;
import java.util.stream.Collectors;

@Data
public abstract class MultipleValue extends Category {

    protected ArrayList<String> values = new ArrayList<>();
    protected ArrayList<String> userChoices;
    public void addValue(String value){
        values.add(value);
    }

    public void choiceSomeValues(){
        Random random =new Random();
        if(userChoices == null) userChoices = new ArrayList<>();
        if(values.size() == 0)
            return;

        if(getNumberofValue() == 1 || getNumberofValue()  == 0) {

            String val = values.get(Utility.nextInt(0, values.size()));

            userChoices.add(val);
            return;

        }
        int numb = Math.min(getNumberofValue(), values.size());
        ArrayList<String> copiedValues =new ArrayList<>(getValues());
        if(numb != 1) numb = Utility.nextInt(1,numb+1);

        for (int i = 0; i < numb; i++) {

            userChoices.add(copiedValues.remove(Utility.nextInt(0,copiedValues.size())));

        }

    }


    @Override
    public String displayDataContent(){
        String sub= "";
        for(Category d : getSubCategorys())if(d != null) sub += d.displayDataContent()+"\n";
        if (!sub.equals("")) sub = "{\n"+sub+"\n}";
        return super.displayDataContent() +";=>" + getUserChoices() + sub;
    }

    public void fillWebElementInput(WebDriver driver, WebElement input){
        String labelCategory = input.findElement(By.tagName("label")).getText();
        assert  labelCategory.equals(getDisplayName() + (isMandatory() ? " *":""));
        userChoices.forEach( answer->
                ClassificationView.chooseEntryOptions(driver,input, answer));
    }

    public String toString(){
        return super.toString();
    }


    public String getContentValue(){

        String val="";
        int i=0;
        ArrayList<String> data = (ArrayList<String>) userChoices.stream()
                .sorted()
                .collect(Collectors.toList());
        if(data.size() == 2){
            val = data.get(0) +" | " + data.get(1);
        } else
        for(String response : data){
            if(i ==0 )
                val += response;
            else if((i == userChoices.size()-1))
                val += "| " + response;
           else val += " | "+response +" " ;
           i++;
        }

        return val;

    }

    public  String getRandomChoice(){

        int x = Utility.nextInt(0,values.size());

        if(values.size()> 0)
            return values.get(x);
        return null;
    }
}
