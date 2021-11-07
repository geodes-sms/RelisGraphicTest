package model;

import lombok.Data;
import lombok.NoArgsConstructor;
import model.relis_categories.Category;
import model.relis_categories.DependantDynamicCategory;
import utils.Utility;

import java.util.ArrayList;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
public class ClassificatedPaper {



    private Paper paper;
    private String year;

    private ArrayList<Category> categories;

    private String validated_msg;
    private String validation_note;



    public ClassificatedPaper(Paper paper_key){
        this.paper = paper_key;


        year = Utility.extractYearFrom(paper.getKey());
        paper_key.setYear( year);
    }




    public void classifyData(){

        categories.stream()

                .forEach(
                category -> {

                    category.classifyData(paper);

                }


        );

    }
    public Category getCategoryByDisplayName(String displayName){
        return categories.stream()
                .filter(p -> p.getDisplayName().equals(displayName))
                .findFirst()
                .orElse(null);
    }

    public void addCategory(Category category){
        if(categories == null)
            categories = new ArrayList<>();
        categories.add(category);
    }


    public boolean compareDisplayNameVal(String displayName, String dom_value){

        Category category = getCategoryByDisplayName(displayName);
        assert category != null;

        String value = category.getContentValue();
        String msg = dom_value+" VS " + value;
        System.out.print(" VS choosedValue={" + value+"}");
        // if the current category has sub categories so retur false
        // cause as of now relis doesn't consider the sub categories
        boolean result = value.equals(dom_value);
        if(category.hasSubCategory())
            return false;
        return result;

    }

    /**
     * this method return all dependedList
     * that the independent list  that they depend on have a sub categories
     *
     * @return list of categories
     */
    public ArrayList<Category> getDependedAndHasDependsSubCategories() {

      return (ArrayList<Category>) categories.stream()
                .filter(category -> category instanceof DependantDynamicCategory)
                .filter( category -> ((DependantDynamicCategory) category).getDependent_on() != null)
                .filter(category -> ((DependantDynamicCategory) category).getDependent_on().hasSubCategory())
                .collect(Collectors.toList());

    }


    public void addValidation(String correct, String not) {
        validated_msg = correct;
        validation_note = not;
    }

}
