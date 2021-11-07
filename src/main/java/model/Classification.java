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

    private ArrayList<Paper> papersToClassify = new ArrayList<>();

    private ArrayList<RelisUser> classificators = new ArrayList<>();
    private ArrayList<RelisUser> validators = new ArrayList<>();
    private int number_of_classifier = 1;
    private int number_of_validator = 1;

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


    public void classifyAllPapers(){

            papersToClassify.forEach(paper->{
            ClassificatedPaper classificatedPaper = new ClassificatedPaper(paper);

            allCategories.stream()
                    .filter(Objects::nonNull)
                    .forEach(cat-> classificatedPaper.addCategory((Category) cat.clone()));

            RelisParser.setLinKObject(classificatedPaper.getCategories());
            classificatedPaper.classifyData();



            if(classificatedPapers == null){
                classificatedPapers = new ArrayList<>();
            }

            classificatedPapers.add(classificatedPaper);
        });



    }

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


    public void test(){
       AtomicInteger i= new AtomicInteger();
       AtomicInteger j = new AtomicInteger();

       allCategories.stream().filter( p -> (p instanceof MultipleValue))
               .forEach(cat1->{
                   allCategories.stream().filter( p -> (p instanceof MultipleValue)).forEach( cat2->{
                       if(!i.equals(j)){
                          if( ((MultipleValue) cat1).getUserChoices() == ((MultipleValue) cat2).getUserChoices())
                              System.out.println("MEME CATEGORIES  &&**()");
                       }

                       j.getAndIncrement();
                   });


                               i.getAndIncrement();
               });
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
