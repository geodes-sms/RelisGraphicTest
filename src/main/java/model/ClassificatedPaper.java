package model;

import com.google.common.collect.ArrayListMultimap;
import databases.DataBase;
import lombok.Data;
import lombok.NoArgsConstructor;
import utils.ClassificationUtils;
import utils.Utility;
import view.ClassificationView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import static utils.ClassificationUtils.*;

@Data
@NoArgsConstructor
public class ClassificatedPaper {


    private HashMap<String,String> fieldsCateogory;
    private String transFormationName;
    private boolean Industrial;
    private boolean Bidirectional;
    private int numberOfCitations;
    private Paper paper;
    private String year;

    private ArrayList<Category> categories;




    public ClassificatedPaper(Paper paper_key){
        this.paper = paper_key;
        fieldsCateogory =  new HashMap<>();

        year = Utility.extractYearFrom(paper.getKey());
        paper_key.setYear( year);
    }



    public void classify(){

        Random random = new Random();
        String domains = DataBase.getInstance().getNextClassificationCategory(DOMAINS_FIELD);
        String targetLanguage = DataBase.getInstance().getNextClassificationCategory(TARGET_LANG_FIELD);
        String sourceLanguage = DataBase.getInstance().getNextClassificationCategory(SOURCE_LANG_FIELD);
        String Scope = DataBase.getInstance().getNextClassificationCategory(SCOPE_FIELD);
        year = Utility.extractYearFrom(paper.getKey());
        Bidirectional =  random.nextBoolean();
        Industrial  = random.nextBoolean();
        numberOfCitations = random.nextInt(2,12);
        transFormationName = paper.getTitle();
        String transformationLanguage = DataBase.getInstance().getNextClassificationCategory(TRANSFORMATION_LANG);
        fieldsCateogory.put(DOMAINS_FIELD, domains);
        fieldsCateogory.put(TARGET_LANG_FIELD, targetLanguage);
        fieldsCateogory.put(SOURCE_LANG_FIELD,sourceLanguage);
        fieldsCateogory.put(SCOPE_FIELD,Scope);
        fieldsCateogory.put(TRANSFORMATION_LANG,transformationLanguage);
    }

//
//    public String toString(){
//        String domains="",targetLanguage="",sourceLanguage="",Scope="";
//        if(fieldsCateogory != null){
//            try {
//                domains = fieldsCateogory.get(DOMAINS_FIELD);
//                targetLanguage = fieldsCateogory.get(TARGET_LANG_FIELD);
//                sourceLanguage = fieldsCateogory.get(SOURCE_LANG_FIELD);
//                Scope = fieldsCateogory.get(SCOPE_FIELD);
//
//            } catch (Exception exception){};
//        }
//        return "[ domains => "+ domains+" , transFormationName =>" + transFormationName+"\n"
//                +"\t sourceLanguage  => "+sourceLanguage +", targetLanguage => " + targetLanguage +"\n"
//                +"\t Scope => " +Scope +", Bidirectionnal =>" + Bidirectional +", Indistrual =>" +Industrial+"\n"
//                +"\t numberOfCitation =>" + numberOfCitations +", year => " + year +"\n]";
//    }


    public String getCategoryValue(String category){


        if( fieldsCateogory != null)
            return fieldsCateogory.get(category);
        return "";
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


}
