package model.relis_categories;

import lombok.Data;
import model.Paper;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.ArrayList;

@Data
public abstract class Category implements Cloneable {


    private String categoryName;
    private String displayName;
    private boolean mandatory;
    private int numberofValue;

    private String identifier;
    private ArrayList<Category> subCategorys = new ArrayList<>();
    public void addSubCategory(Category c){
        if (subCategorys == null)
            subCategorys = new ArrayList<>();
        subCategorys.add(c);
    }


    public Object clone(){
        try {
            Category category = (Category) super.clone();
            category.subCategorys = new ArrayList<>();
            subCategorys.forEach( category1 -> category.addSubCategory((Category) category.clone()));
            return category;
        } catch (Exception e)
        {
            return null;
        }

    }


    public String toString(){
        String sub ="";

        if(subCategorys != null) for(Category c : subCategorys) sub += "\t"+c+"\n";
        if(subCategorys != null) sub ="{\n" +sub +"}";
        return categoryName +" " +identifier+" \"" +displayName +"\" ["+  numberofValue+"] " +(mandatory? "*":"")
                +sub;
    }


    public abstract void classifyData(Paper paper);
    public String displayDataContent(){
        return displayName;
    }

    public abstract void fillWebElementInput(WebDriver driver, WebElement input);

    public Category getSubCategoryByDisplayName(String displayName){
        return subCategorys.stream()
                .filter(p -> p != null)
                .filter(p -> p.getDisplayName().equals(displayName))
                .findFirst()
                .orElse(null);
    }

    public  boolean hasSubCategory(){

        return subCategorys.size() > 0;
    }

    public abstract String getContentValue();
}
