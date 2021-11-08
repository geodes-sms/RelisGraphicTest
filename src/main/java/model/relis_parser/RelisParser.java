package model.relis_parser;

import model.*;
import model.relis_categories.*;
import model.relis_type.*;
import org.apache.commons.lang3.StringUtils;
import utils.FileUtils;

import utils.Utility;

import java.util.ArrayList;

import static model.relis_parser.RelisSyntax.*;

public class RelisParser {



    private static final String PROJECT_MT = "src/main/resources/mt.relis" ;
    public static void  extractProject(Project project, StringParser dataParse){

        String data = dataParse.getData();
        if(data.equals("")) return;

        String nextWord = dataParse.getNextWord();
        if(nextWord.equals(PROJECT)){

            String id = dataParse.getNextWord();
            String name = dataParse.gextNextQuotedWord();
            project.setProject_name(name);
            project.setProjectId(id);
            // System.out.println("projectName=" + name +" id={"+id+"}");

        } else if(nextWord.equals(SCREENING)){
            System.out.println("dans screening");
            Screening screening = new Screening();
            System.out.println("calling screening phase");
            extractScreening(screening,dataParse);
            //   System.out.println("nextWordsss={" + dataParse.getData()+"}" );
            project.setScreening(screening);

        } else if(nextWord.equals(CLASSIFICATION)){

            Classification classifi = new Classification();

            getClassificationsData(dataParse,classifi);
            setReferences(classifi.getAllCategories());
            project.setClassification(classifi);
        } else if(nextWord.equals(QualityAssurance)){

            System.out.println("DANS QA");
            QualityAssement qa = new QualityAssement();

            while (true){
                nextWord = dataParse.getNextWord();
                // System.out.println("next word QA={" + nextWord+"}");
                if(nextWord.equals(QUESTIONS)) {

                    String[] questions = getNextListElements(dataParse);
                    qa.setQuestionsForOA(questions);

                    //System.out.println("set question = true");
                }
                else if(nextWord.equals(RESPONSES)){


                    getResponseDataForQA(dataParse, qa);

                    //  System.out.println("set up answer ");

                }
                else if(nextWord.equals(MIN_QA_SCORES)){
                    nextWord = dataParse.getNextWord();
                    double min_score = Double.parseDouble(nextWord);
                    qa.setMin_score(min_score);
                    System.out.println("min score={"+nextWord+"}" );

                } else {
                    // System.out.println("WORD="+nextWord);
                    dataParse.back();
                    break;
                };

            }
            project.setQA(qa);



        }
        extractProject(project,dataParse);
    }


    private static void extractScreening(Screening screening, StringParser dataParser){

        String nextWord = dataParser.getNextWord();
        if(nextWord.equals(""))
            return;

        if(nextWord.equals("Reviews")){
            int n  = Integer.parseInt(dataParser.getNextWord());
            screening.setReviewers_per_paper(n);
        } else if(nextWord.equals("Conflict")){

            String on = dataParser.getNextWord();
            assert on.equals("on");
            on = dataParser.getNextWord();
            screening.setConflict_type(on);
            on = dataParser.getNextWord();
            assert on.equals("resolved_by");
            on = dataParser.getNextWord();
            screening.setConflit_resolution_by(on);
//            System.out.println("conflict type={" + screening.getConflict_type()
//            +"}, reviewer n={" + screening.getReviewers_per_paper() +"} " +
//                    "resolve by {" + screening.getConflit_resolution_by()+"}");
        } else if(nextWord.equals("Criteria")){
            String[] array  = getNextListElements(dataParser);
            for (String c : array)  screening.addCriteria(new Criteria(c));

        } else if(nextWord.equals("Sources")){
            String[] array = getNextListElements(dataParser);

            for (String c : array) {
                screening.addSource(c);

            }


        } else if(nextWord.equals("Strategies")){

            String[] array = getNextListElements(dataParser);
            for (String c : array)
                screening.addStrategy(c);




        } else if(nextWord.equals("Validation")){
            nextWord = dataParser.getNextWord();
            screening.setValidationPercentage(nextWord);
            nextWord = dataParser.getNextWord();
            screening.setValidation_type(nextWord);
            // System.out.println("validation{percent="+screening.getValidationPercentage()+
            //         ",type="+screening.getValidation_type()+"}");
        } else if(nextWord.equals("Phases")){

            char punctuation = ' ';
            do{
                String phaseName = "Screening : "+dataParser.gextNextQuotedWord();
                ScreeningPhase phase = new ScreeningPhase(phaseName);
                nextWord = dataParser.gextNextQuotedWord();
                phase.setDescription(nextWord);
                nextWord= dataParser.getNextWordSeparedBySpace();
                phase.setFields(nextWord);
                screening.addPhase(phase);
                //System.out.println(phase);

                punctuation= Utility.getNextPunctuation(dataParser);
            }while (punctuation == ',');

            return;
        } else return;

        extractScreening(screening,dataParser);

    }



    private static void getClassificationsData(StringParser dataParse, Classification classifi) {
        Category superCategory;
        String nextWord;
        do{
            superCategory = extractClassificationData(dataParse, classifi);
            nextWord = dataParse.getNextWord();
            classifi.addCategory(superCategory);
            if(nextWord.equals("{")){
                Category subCategory;

                do {
                    subCategory = extractClassificationData(dataParse,classifi);

                    assert superCategory != null;
                    superCategory.addSubCategory(subCategory);
                }while (subCategory != null);
                dataParse.skipCurrentWord(); // skip the '}' symbol
            } else dataParse.back();
        } while (superCategory != null);

    }

    private static Category extractClassificationData(StringParser dataParse, Classification classification) {

        String nextWord;
        nextWord  = dataParse.getNextWord();
        if(nextWord.equals("no_override"))
            nextWord = dataParse.getNextWord();
        String type = nextWord, id, displayName;
        boolean mandatory = false;

        if(nextWord.equals(SIMPLE) || nextWord.equals(DYNAMIC_LIST) || nextWord.equals(LIST)){

            id  = dataParse.getNextWord();
            displayName = dataParse.gextNextQuotedWord();

            nextWord =dataParse.getNextWord();
            if(nextWord.equals("*")){
                mandatory = true;
                nextWord = dataParse.getNextWord();
            }

            if(type.equals(SIMPLE)){
                FreeCategory freeCategory  = new FreeCategory();
                freeCategory.setMandatory(mandatory);
                freeCategory.setIdentifier(id);
                freeCategory.setDisplayName(displayName);
                getSimpleCategoryFromConfiguration(dataParse, freeCategory);// extract simple category
                return freeCategory;

            } else if(type.equals(DYNAMIC_LIST)){

                String refName;
                ArrayList<String> list_val;
                int numberOfVal = Integer.parseInt(dataParse.getNextWord()); // retrive the n in [n]
                dataParse.skipCurrentWord(); // skip the ']' symbol
                nextWord = dataParse.getNextWord();

                if(nextWord.equals(DEPENDS_ON)){ // is a depended list???
                    // if so
                    DependantDynamicCategory category = new DependantDynamicCategory();
                    category.setIdentifier(id);
                    category.setMandatory(mandatory);
                    category.setNumberofValue(numberOfVal);
                    category.setDisplayName(displayName);
                    nextWord  = dataParse.getNextWord();
                    MultipleValue independ = (MultipleValue) classification.getCategoryById(nextWord);
                    category.setDependent_on(independ);
                    return category;
                } else {  // we have an independent list

                    IndependantDynamicCategory independantDynamicCategory = new IndependantDynamicCategory();
                    if(nextWord.equals("\"")) {
                        // reference name
                        refName = dataParse.getNextWord();

                        dataParse.skipCurrentWord();
                        nextWord = dataParse.getNextWord();
                    } else refName = id; // the default value of the refName is the id
                    // we have the initial values of the dynamic list???
                    if(nextWord.equals("=")){ // if so
                        list_val = dataParse.getNextListElements();
                        independantDynamicCategory.setValues(list_val);

                    } else{ // the dynamic list refer to an another dynamic list
                        independantDynamicCategory.setReference_to(refName);
                        dataParse.back();

                    }
                    independantDynamicCategory.setIdentifier(id);
                    independantDynamicCategory.setReference_id(refName);
                    independantDynamicCategory.setDisplayName(displayName);

                    return independantDynamicCategory;
                }



            } else{
                StaticCategory staticCategory = new StaticCategory();
                staticCategory.setIdentifier(id);
                staticCategory.setDisplayName(displayName);
                staticCategory.setMandatory(mandatory);

                int numberOfVal = Integer.parseInt(dataParse.getNextWord()); // retrive the n in [n]
                staticCategory.setNumberofValue(numberOfVal);
                dataParse.skipCurrentWord(); // skip the ']' symbol
                staticCategory.setValues(dataParse.getNextListElements()); // get list elements
                return staticCategory;
            }

        } else { // we finish to extract the classifications data
            dataParse.back();

            return null;
        }



    }


    public static Project getProjectFromFiles(){


        return getProjectFrom(PROJECT_MT);
    }


    public static Project getProjectFrom(String filename){
        Project project = new Project ();
        String lines = FileUtils.getLinesFrom(filename);
        StringParser stringParser = new StringParser();
        stringParser.setData(lines);
        extractProject(project,stringParser);
        return project;

    }

    /**
     * retrive the response for a QA phase with their respective scores
     * @param dataParser the rest of string
     * @param qualityAssement the qa phase object
     */
    private static void getResponseDataForQA(StringParser dataParser, QualityAssement qualityAssement){

        int begin = dataParser.getData().indexOf("[");
        int end = dataParser.getData().indexOf("]");

        String res = dataParser.getData().substring(begin,end+1);

        int i=0;
        String answers, score_str="";
        double score ;
        while( (i < res.length()) && res.charAt(i) != ']'){
            answers = StringUtils.substringBetween(res,"\"","\"");
            score_str ="";
            i = Utility.indexOfNth(res,"\"",2)+1;

            while ((i < res.length()) && !Character.isDigit(res.charAt(i))) i++;
            while ((i < res.length()) && (Character.isDigit(res.charAt(i)) || res.charAt(i) == '.'))  score_str += res.charAt(i++);
            res = res.substring(i);
            i =0;
            score = Double.parseDouble(score_str);
            //System.out.println("adding answer={"+ answers+", score="+score_str+"}");
            qualityAssement.addResponse(answers,score);

        }
        dataParser.updateData(end+1);
    }
    private static String[] getNextListElements(StringParser dataParser){

        int begin = dataParser.getData().indexOf("[");
        int end = dataParser.getData().indexOf("]");
        String res = dataParser.getData().substring(begin,end+1);
        dataParser.updateData(end+1);
        return StringUtils.substringsBetween(res,"\"","\"");
    }

    public static void setReferences(ArrayList<Category> allCategories){

        allCategories.stream()
                .filter(cat -> (cat instanceof IndependantDynamicCategory))
                .filter(cat -> ((IndependantDynamicCategory) cat).getReference_to() != null)
                .forEach( cat->{
                    IndependantDynamicCategory value = (IndependantDynamicCategory)
                            Classification.getCategoryByReference(allCategories,((IndependantDynamicCategory) cat)
                                    .getReference_to());
                    assert value != null;
                    ((IndependantDynamicCategory) cat).setReference(value);
                });
    }

    public static void setLinKObject(ArrayList<Category> categories){
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
            setReferences(categories);
    }

    private static void getSimpleCategoryFromConfiguration(StringParser dataParse, FreeCategory freeCategory) {
        String val ="", nextWord , type;
        int maxValue=1, maxChar=100;
        nextWord = dataParse.getNextWord(); // we have the type (int, string, real, ...)
        try {  // [n] ????
            maxValue = Integer.parseInt(nextWord);
            dataParse.skipCurrentWord(); // skip the ']'
            dataParse.skipCurrentWord(); // skip the ':'
            nextWord = dataParse.getNextWord();
        }catch (Exception e){ // we dont have a [n] for the simple category

        }

        type = nextWord;
        // find the max character
        nextWord =dataParse.getNextWord();

        if( nextWord.equals("(")){ // do we specify that max char??
            nextWord = dataParse.getNextWord(); // if so we gonna extract it
            maxChar = Integer.parseInt(nextWord); // max character

            dataParse.getNextWord(); // ignore the ')'
        } else dataParse.backTo(nextWord);
        nextWord = dataParse.getNextWord();

        if(nextWord.equals("style")){ // do we ha

            String pattern = dataParse.gextNextQuotedWord();

            dataParse.getNextWord();// ignore the ')'
            nextWord = dataParse.getNextWord();
            // pattern defaults values
            if(nextWord.equals("=")){
                val = dataParse.gextNextQuotedWord();
                dataParse.getNextWord();// ignore the ]



            }else dataParse.backTo(nextWord);
        } else dataParse.backTo(nextWord);
        TypeOf typeOf = getTypeOfObject(type,maxChar);

        Value value = new Value();
        value.setType(typeOf);
        value.setValue(val);
        freeCategory.setNumberofValue(maxValue);
        freeCategory.setValue(value);
    }

    public static TypeOf getTypeOfObject(String type, int maxChar){

        if(type.equals("string") || type.equals("text")){
            StringType type1=  new StringType();
            type1.setMaxCharacter(maxChar);
            return type1;
        }

        else if(type.equals("real"))
            return new RealType();
        else if(type.equals("bool"))
            return new BooleanType();
        else if(type.equals("date"))
            return new DateType();
        else if(type.equals("int")){
            IntegerType intType = new IntegerType();
            intType.setMaxCharacter(maxChar);
            return intType;
        }
        return null;

    }




}
