package model;

import lombok.Data;
import lombok.ToString;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Objects;
import java.util.Random;

@Data

public class IndependantDynamicCategory extends MultipleValue{

    private String reference_id;
    private String reference_to;

    private MultipleValue reference;
    public void showValues(){
        getValues().forEach(System.out::println);
    }
    public IndependantDynamicCategory(){
        this.setCategoryName("DynamicList");
    }

    public String toString(){

        String base = super.toString();
        return base +" values=" +getValues()+", reference={" + reference_to+"}";
    }

    public void choiceSomeValues(){

        if(reference != null){
             values = new ArrayList<>(reference.getValues());

        }

        super.choiceSomeValues();


    }
    @Override
    public void classifyData(Paper paper) {
        choiceSomeValues();
        ArrayList<Category> sub = getSubCategorys();
        if(sub == null) return;
        sub.stream().filter(Objects::nonNull)
                .forEach(category -> category.classifyData(paper));
    }
    @Override
    public Object clone(){

        IndependantDynamicCategory c = (IndependantDynamicCategory) super.clone();
        final  ArrayList<String> copiedUserChoices= new ArrayList<>();
        ArrayList<String> copiedValues = new ArrayList<>();
       // copiedUserChoices = new ArrayList<>();
        copiedValues.addAll(getValues());
        c.setValues(copiedValues);
        //c.setUserChoices(copiedUserChoices);
        ArrayList<Category> sub = new ArrayList<>();
        getSubCategorys().forEach( category1 -> sub.add((Category) category1.clone()));
        c.setSubCategorys(sub);
        return c;
    }



}

