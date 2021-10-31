package utils;

import com.google.common.collect.Multimap;
import databases.DataBase;
import model.*;
import org.apache.commons.lang3.StringUtils;
import utils.FileUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainTest {
    public static final String SIMPLE= "Simple";
    public static final String LIST = "List";
    public static final String DYNAMIC_LIST = "DynamicList";
    public static final String DEPENDS_ON = "depends_on";


    public static Category getClassificationCategories(String lines,Classification classification){

        if(lines.trim().equals("")) return null;
        if(lines.trim().startsWith(SIMPLE)){
            Category category = extractSimpleCategory(lines);
           // classification.addCategory(category);
            return category;
        }
        else if(lines.trim().startsWith(LIST)){
            Category cate = extractListCategory(lines);
            //classification.addCategory(cate);
            return cate;
        }

        else if(lines.trim().startsWith(DYNAMIC_LIST)){
            if(lines.contains(DEPENDS_ON)) {
                Category category = extractDependedDynamicList(lines, classification);
               // classification.addCategory(category);
                return category;
            }
            else {
                Category category = extractDynamicList(lines.replaceAll("\\{"," "),classification);
               // classification.addCategory(category);
                return category;
            }
        }
        return null;
    }
    public static void main(String[] args) {
//        TestListenerAdapter tla = new TestListenerAdapter();
//        TestNG testng = new TestNG();
//        testng.setTestClasses(new Class[] {ProjectTest.class});
//        testng.addListener(tla);
//        testng.run();

        Classification classification = new Classification();

       // setCategories(classification);
        //classification.showCategories();
        Paper paper = new Paper();
        paper.setTitle("the world after covid");
        paper.setYear("2021");
        paper.setKey("afterCovid21");

        String textCat = "Simple string_100 \"String100\" * :string(100)";
        String textCat1 = "Simple int_4 \"Int4\" * :int(4)";
        String textCat2 ="Simple bool_1 \"Boolean\" * :bool";
        String textCat3 = "Simple date_1 \"Date\" * :date";
        String textCat4 = "Simple pattern \"pattern\" :string(50) style(\"[A-Z]+[0-9]*\")";
        String textCat5 = "Simple note \"Note\" [1] :text(500) ";
//        Category c = extractSimpleCategory(textCat4);
//        System.out.println("extracted cat= \n"+ c);


//
//        classification.getAllCategories().forEach(
//                category -> {
//                    category.classifyData(paper);
//                    System.out.println(category.displayDataContent());
//
//                }
        //);
       // classification.getAllCategories().forEach(category -> System.out.println(category));


        String txt  ="                                      \n project mt   \n          \"model of x\" " +
                "\n Screening";
        Project p =new Project();
        StringParser data= new StringParser();
        data.setData(txt);
        extractProject(p,data);




    }



    public static void  extractProject(Project project, StringParser dataParse){

        String data = dataParse.getData();
        if(data.equals("")) return;

        String nextWord = getNextWord(dataParse);
        if(nextWord.equals("project")){

            String id = getNextWord(dataParse);
            String name = dataParse.gextNextQuotedWord();
            project.setProject_name(name);
            project.setProjectId(id);
        } else if(nextWord.equals("Screening")){

            Screening screening = new Screening();
            project.setScreening(screening);

        }



        System.out.println("next data=" + dataParse.getData());
        //extractProject(project,data);
    }


    public static String getNextWord(StringParser parser){

        int pos=0;

        String word = "", data =parser.getData();
        while( (pos < data.length())&& (!Character.isAlphabetic(data.charAt(pos)) &&
                (!Character.isDigit(data.charAt(pos))))) pos ++;

        while( (pos < data.length()) &&
                (Character.isAlphabetic(data.charAt(pos)) ||
                        (Character.isDigit(data.charAt(pos)))))
            word += data.charAt(pos++);
        parser.updateData(pos);

        return word;


    }
    public static String getNextWord2(String text){
        if(text.equals(""))
            return "";
        int i =0;

        String next = "";
        int index =0;
        while ((index < text.length()) && (text.charAt(index) == ' ' || text.charAt(index) == '\n')) index++;
        while ((index < text.length()) && ( text.charAt(index) != ' ')) next += text.charAt(index++);
        return next;
    }
    public static void runDynamicTest(Classification classification){
        String t1= "DynamicList dynListOne \"DynamicListOne\"  [1] =[\"DynamicListData1\",\"DynamicListData2\"" +
                ",\"DynamicListData3\",\"DynamicListData4\",\"DynamicListData5\"]";
        String depends = "DynamicList dependentList \"dependentList\"  *[0] depends_on superCategory";
        String list = "List listOne \"ListOne\" * [1] = [\"value 1\", \"value 2\"]";

        String list3 = "List listThree \"List Three Values\" [3] = [\"ListThreeVal1\",\"ListThreeVal2\",\"L" +
                "istThreeVal3\",\"ListThreeVal4\",\"ListThreeVal5\"]";
        Category category  = extractListCategory(list3);

        System.out.println("extracted dynamic list =>\n" + category);


    }

    public static void setCategories(Classification classification){
        String lines = FileUtils.getLinesFrom("src/main/resources/classification/classifi.txt");
        String[] data = lines.split("\n");
        int i=0;
        while (i < data.length){
            String curr =data[i].trim();
            if(curr.equals("")) {
                i++;
                continue;
            }
            if(data[i].contains("{")){
                curr = curr.replaceAll("\\{","");
                curr = curr.replaceAll("}","");
                Category supercategory = getClassificationCategories(curr,classification);
                i++;
//                System.out.println("super cat => " + supercategory);
                classification.addCategory(supercategory);
                while (!data[i].contains("}")){
                    curr = data[i];
                    Category subcategory  = getClassificationCategories(curr,classification);
                    //System.out.println("sub cat" + subcategory);
                    assert supercategory != null;
                    supercategory.addSubCategory(subcategory);
                    i++;
                }

                //System.err.println("donnees =" + curr);
            }else classification.addCategory(getClassificationCategories(curr,classification));
            i++;
        }
        setReferences(classification.getAllCategories());
    }
    public static String extractCategoryId(String line, String type){
        String result ="";

        int index = line.indexOf(type);
        index += type.length();
        while (line.charAt(index) == ' ') index++;

        int next = line.indexOf(" ", index);
        result = line.substring(index,next);
        return result;

    }


    public static String getNextQuotedString(String data, int from){
        String result ="";
        String subData = data.substring(from);

        if((2+from) >= data.length() || !(subData.contains("\""))) return result;

        int start, end;
        start = data.indexOf("\"",from)+1;
        end = data.indexOf("\"", start);
        result = data.substring(start,end);

        return result;
    }

    public static Category  extractDependedDynamicList(String lines,Classification classification){


        DependantDynamicCategory dependantDynamicCategory = new DependantDynamicCategory();
        setDataForMultiValue(lines,dependantDynamicCategory);
        int pos = lines.indexOf(DEPENDS_ON);
        pos += DEPENDS_ON.length();
        while (lines.charAt(pos) == ' ') pos++;
        String id_depends= "";
        while (( pos < lines.length())&&lines.charAt(pos) != ' ') id_depends += lines.charAt(pos++);
        MultipleValue sub = (MultipleValue) classification.getCategoryById(id_depends);
        assert  sub != null;
        dependantDynamicCategory.setDependent_on(sub);
        return dependantDynamicCategory;

    }

    private static void setDataForMultiValue(String lines,MultipleValue list){
        int cardinality=1 , equal_index;
        String partBeforeEqual, afterEqual;
        equal_index = lines.lastIndexOf("=");
        // do we have the list values like = [a,b,c,d..]???
        if(equal_index != -1){
            partBeforeEqual = lines.substring(0, equal_index);
            afterEqual = lines.substring(equal_index);

        } else {
            partBeforeEqual = lines;
           afterEqual = "";
        }
        int index = 0;
        while (partBeforeEqual.charAt(index) == ' ') index++;
        String type_of_list = "";
        while (Character.isAlphabetic(partBeforeEqual.charAt(index))) type_of_list += partBeforeEqual.charAt(index++);
        String id  = extractCategoryId(partBeforeEqual,type_of_list);
        String[] quotedString = StringUtils.substringsBetween(partBeforeEqual,"\"","\"");
        String displayName  = quotedString[0];
        String tmp = StringUtils.substringBetween(partBeforeEqual,"[","]");
        if(tmp != null)
            cardinality  = Integer.parseInt((tmp));
        if(cardinality == -1)
            cardinality = 1_000;
        boolean mandatory = lines.contains("*");
        if(!afterEqual.equals("")){
            String[] datas = StringUtils.substringsBetween(afterEqual,"\"","\"");
            ArrayList<String> data = new ArrayList<>(Arrays.asList(datas));
            list.setValues(data);
        }
        list.setIdentifier(id);
        list.setMandatory(mandatory);
        list.setNumberofValue(cardinality);
        list.setDisplayName(displayName);

    }
    public static Category  extractDynamicList(String lines,Classification classification){

        // dinamynamicList id "displayName" * [1] "referenced" = [a,b,c,d]

        int equal_index;
        String partBeforeEqual;
        equal_index = lines.lastIndexOf("=");
        partBeforeEqual = (equal_index != -1) ?lines.substring(0, equal_index) : lines;
        IndependantDynamicCategory independantDynamicCategory = new IndependantDynamicCategory();
        setDataForMultiValue(lines,independantDynamicCategory);
        String[] quotedString = StringUtils.substringsBetween(partBeforeEqual,"\"","\"");
        String ref_name="";
        if(quotedString.length == 2)
            ref_name = quotedString[1];

        if(equal_index == -1) {
            independantDynamicCategory.setReference_to(ref_name);
        }
        else independantDynamicCategory.setReference_id(ref_name);

        return independantDynamicCategory;


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
    public static Category extractListCategory(String lines){

       StaticCategory list_category = new StaticCategory();
       setDataForMultiValue(lines, list_category);
        return list_category;


    }
    public static Category extractSimpleCategory(String lines){

        String name,id;
        boolean mandatory;
        String type= "";
        id = extractCategoryId(lines,SIMPLE);
        int maxlength = 1,nextPos=0, maxChar=1;
        name = getNextQuotedString(lines,nextPos);
        nextPos = lines.indexOf(name,nextPos)+1;
        String firstPart = lines.substring(0,lines.indexOf(":"));
        mandatory = firstPart.contains("*");
        String subLines = lines.substring(0, lines.lastIndexOf(":"));
        String tmp = StringUtils.substringBetween(subLines,"[","]");
        if(tmp != null) maxlength = Integer.parseInt(tmp);
        tmp = StringUtils.substringBetween(lines,"(",")");
        if(tmp != null)  maxChar = Integer.parseInt(tmp);
        nextPos  =lines.lastIndexOf(":")+1;
       while ((nextPos < lines.length()) && (Character.isAlphabetic(lines.charAt(nextPos))))
           type += lines.charAt(nextPos++);

        tmp = StringUtils.substringBetween(lines,"style(\"","\")");
        if(tmp != null)
            System.out.println("pattern=" + tmp);
        FreeCategory category = new FreeCategory();
        category.setMandatory(mandatory);
        category.setIdentifier(id);
        category.setDisplayName(name);
        category.setNumberofValue(maxlength);
        Value value = new Value();
        TypeOf typeOf = Utility.getTypeOfObject(type,maxChar);
        value.setType(typeOf);
        category.setValue(value);
        return category;
    }





}
