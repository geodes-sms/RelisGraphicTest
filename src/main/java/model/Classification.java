package model;

import lombok.AllArgsConstructor;
import lombok.Data;
import utils.MainTest;
import utils.Utility;

import java.util.ArrayList;
import java.util.Map;
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

    private ArrayList<ClassificatedPaper> classificatedPapers;


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
         allCategories.forEach( cat-> System.out.println( cat.displayDataContent()));
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

    private void setLinKObject(ArrayList<Category> categories){
        categories.stream()
                .filter( cat-> (cat instanceof DependantDynamicCategory))
                .forEach( category -> {
                    MultipleValue c =  (MultipleValue) categories.stream().
                            filter( category1 -> {
                                String dependent_on_id= ((DependantDynamicCategory)category).
                                        getDependent_on().getIdentifier();

                              return dependent_on_id.equals(category1.getIdentifier());
                            })
                            .findFirst().orElse(null);
                    ( (DependantDynamicCategory) category).setDependent_on(c);


                });
        categories.forEach(cat->{
            cat.getSubCategorys().stream()
                    .filter( sub-> (sub instanceof DependantDynamicCategory))
                    .forEach(subCat->{
                        MultipleValue c =  (MultipleValue) categories.stream().
                                filter( category1 ->{
                                    String dependent_on_id= ((DependantDynamicCategory)subCat).
                                            getDependent_on().getIdentifier();

                                   return dependent_on_id.equals(category1.getIdentifier()) ;
                                } )
                                .findFirst().orElse(null);
                        ( (DependantDynamicCategory) subCat).setDependent_on(c);
                    });

        });
        MainTest.setReferences(categories);
    }
    public void classifyAllPapers(){

            papersToClassify.forEach(paper->{
            ClassificatedPaper classificatedPaper = new ClassificatedPaper(paper);

            allCategories.forEach(cat-> classificatedPaper.addCategory((Category) cat.clone()));

            setLinKObject(classificatedPaper.getCategories());
            classificatedPaper.classifyData();



            if(classificatedPapers == null){
                classificatedPapers = new ArrayList<>();
            }

            classificatedPapers.add(classificatedPaper);
        });
        classificatedPapers.forEach(cl->{
            cl.classifyData();
            System.out.println("classified \n\n");
        });
        test();

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
}
