package model;

import lombok.AllArgsConstructor;
import lombok.Data;
import model.relis_categories.Category;
import model.relis_categories.DependantDynamicCategory;
import model.relis_categories.IndependantDynamicCategory;
import model.relis_categories.MultipleValue;
import model.relis_parser.RelisParser;
import utils.MainTest;
import utils.Utility;

import java.util.ArrayList;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

@Data

@AllArgsConstructor
public class Classification {
    // all the papers for the classification phase
    private ArrayList<Paper> papersToClassify = new ArrayList<>();
    // the classificators for the classification phase
    private ArrayList<RelisUser> classificators = new ArrayList<>();
    private ArrayList<RelisUser> validators = new ArrayList<>();
    private int number_of_classifier = 1; // number of classificator
    private int number_of_validator = 1; // number of validator
    // all the categories for the classification phase
    private  ArrayList<Category> allCategories = new ArrayList<>();


    private ArrayList<String> categories_displayed_names;

    private ArrayList<ClassificatedPaper> classificatedPapers;


    private String test_message = "";

    public String toString(){

        StringBuilder papers= new StringBuilder("\n");
        StringBuilder users= new StringBuilder("\n");
        StringBuilder validator_user = new StringBuilder("\n");
        if(classificators != null) for(RelisUser u : classificators)
            users.append("\t").append(u).append(" ,\n");
        else users = new StringBuilder(" ");
        if( validators != null) for(RelisUser u : validators)
            validator_user.append("\t").append(u).append(" ,\n");
        else users = new StringBuilder(" ");
        if(papersToClassify != null)
            for(Paper p : papersToClassify)
                papers.append("\t").append(p).append(" ,\n");
        else papers = new StringBuilder(" ");
         allCategories.stream().filter(Objects::nonNull).forEach(cat-> System.out.println( cat.displayDataContent()));
        return "[Papers =>[" + papers +"\n], \n" + " classificators => \n["+ users + "]\n]"
                +"\n Validators => \t [" + validator_user +"\n \t ]\n ]";
    }


    public Paper getPaper(String  key){
        System.out.println("key search =" +key);
        return Utility.getPaperByKey(papersToClassify,key);
    }

    public Classification(){
        //MainTest.setCategories(this);
    }



    public ClassificatedPaper getClassifiedPaperByKey(String key){

        return classificatedPapers.stream()
                .filter(p-> p.getPaper().getKey().equals(key))
                .findFirst()
                .orElse(null);
    }


    /**
     * this method classify all the papers so we can make it
     * ready for filling at the classification phase
     */
    public void classifyAllPapers(){

        // iterate through all the paper to classify
            papersToClassify.forEach(paper->{
            ClassificatedPaper classificatedPaper = new ClassificatedPaper(paper);
        // get a ccopy for all the categories
            allCategories.stream()
                    .filter(Objects::nonNull)
                    .forEach(cat-> classificatedPaper.addCategory((Category) cat.clone()));
            // set the relation between the cloned categories
            RelisParser.setLinKObject(classificatedPaper.getCategories());
            // classify all the cateogties by choosing a value
            classificatedPaper.classifyData();

            if(classificatedPapers == null){
                classificatedPapers = new ArrayList<>();
            }
            // add to the list of classificated papers
            classificatedPapers.add(classificatedPaper);
        });



    }
    /**
     * 
     * @return the lenght of papers to classify
     */
    public int getClassifierLength() {
        return classificators.size();
    }
    public int getPapersToClassifyLength(){
        return papersToClassify.size();
    }


    public void addCategory(Category x){
        allCategories.add(x);
    }
    public Category getCategoryById(String id){
        return allCategories.stream()
                .filter(Objects::nonNull)
                .filter(cat -> cat.getIdentifier().equals(id))
                .findFirst()
                .orElse(null);
    }
    public Category getCategoryByDisplayName(String key,String displayName){

        ClassificatedPaper classificatedPaper = classificatedPapers.stream()
                .filter( papersToClassify-> papersToClassify.getPaper().getKey().equals(key))
                .findFirst().orElse(null);
        if(classificatedPaper != null){
            Category c = classificatedPaper.getCategoryByDisplayName(displayName);
            return c;
        }
        return null;
    }

    public void showCategories() {
        allCategories.forEach( p -> System.out.println("*************************************\n"+p));
    }

    /**
     * this method return a category  that has a reference name like the paran ref_name
     * @param allCategories all the categories
     * @param ref_name the reference name
     * @return a category if exists otherwise null
     */
    public static Category getCategoryByReference(ArrayList<Category> allCategories,String ref_name) {
        return
        allCategories.stream()
                .filter(cat -> ( cat instanceof IndependantDynamicCategory))
                .filter(category -> ((IndependantDynamicCategory) category).getReference_to() == null)
                .filter(category -> ((IndependantDynamicCategory) category).getReference_id()
                        .equals(ref_name))
                .findFirst()
                .orElse(null);
    }




    public void addPapers(Paper p){

        if(papersToClassify == null)
            papersToClassify = new ArrayList<>();
        papersToClassify.add(p);
    }

    public void addCategoryDisplayedName(String name) {
        if(categories_displayed_names == null)
            categories_displayed_names = new ArrayList<>();
        categories_displayed_names.add(name);
    }


    public void append_test_message(String s) {
        test_message += s;
    }
}
