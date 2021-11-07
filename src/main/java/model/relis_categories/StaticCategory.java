package model.relis_categories;

import lombok.Data;
import lombok.ToString;
import model.Paper;

import java.util.ArrayList;

@Data
@ToString
public class StaticCategory extends MultipleValue {



    public StaticCategory(){
        this.setCategoryName("List");
    }

    public void addValue(String value){
        getValues().add(value);
    }

    public void removeValue(String va){
        getValues().remove(va);
    }

    public void showValues(){

        getValues().forEach(System.out::println);
    }


//    public void choiceSomeValues(){
//        Random random =new Random();
//        int numb = random.nextInt(0,values.size());
//        System.out.println("num choiced " + numb +", val=" +values);
//        ArrayList<String> copiedValues =new ArrayList<>(values);
//        for (int i = 0; i < numb; i++) {
//
//            userChoices.add(copiedValues.remove(random.nextInt(0,copiedValues.size())));
//        }
//    }


    public String toString(){

        String base = super.toString();
        return  base +"= " + getValues();
    }

    @Override
    public void classifyData(Paper paper) {
        choiceSomeValues();
    }

//    public String displayDataContent(){
//        return super.displayDataContent() +" =>" + userChoices;
//    }

    public Object clone(){
        StaticCategory staticCategory = (StaticCategory) super.clone();
        ArrayList<Category> sub = new ArrayList<>();
        getSubCategorys().forEach( category1 -> sub.add((Category) category1.clone()));
        staticCategory.setSubCategorys(sub);
        ArrayList<String> copiedUserChoices;
        ArrayList<String> copiedValues = new ArrayList<>();
        copiedUserChoices = new ArrayList<>();
//        getUserChoices().forEach(userVal->copiedUserChoices.add(userVal));
        getValues().forEach(val->copiedValues.add(val));
        staticCategory.setValues(copiedValues);
       // staticCategory.setUserChoices(copiedUserChoices);

        return staticCategory;

    }


}
