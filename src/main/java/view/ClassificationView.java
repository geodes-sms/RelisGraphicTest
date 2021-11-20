package view;

import model.*;
import model.relis_categories.Category;
import model.relis_categories.DependantDynamicCategory;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import utils.*;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.BiFunction;
import java.util.stream.Collectors;


import static utils.ClassificationUtils.*;

public class ClassificationView {




    /**
     * @param driver the web driver
     * @return all the papers of the current connected user 
     * to classify
     */
    public ArrayList<Paper> getUserPapersToCLassify(WebDriver driver){


        openMyPendingPapersToClassify(driver);
        ArrayList<Paper> papers = new ArrayList<>();
        Utility.getAllPaperFromTable_id(driver,papers);
        return papers;
    }

    /**
     * assign the classificators for the classification phase
     * @param driver the web driver
     * @param classification classification object
     */
    public void assign_classificator(WebDriver driver, Classification classification){

        Views.openUserAssignmentPage(driver, CSS_ASSIGN_CLASSIFICATORS_PAGE);

        WebElement rest_papers = driver.findElement(By.cssSelector("#home b"));
        if(rest_papers.getText().equals("Number of papers to assign :0")){
             getClassificator(driver, classification);
             return;
        }
        // we select some random users
        ArrayList<RelisUser> reviewers = Views.chooseUserforScreening(
                driver,classification.getNumber_of_classifier());
        // we submit the assaignment
        driver.findElement(By.className(ScreeningUtils.CLASS_SUCCESS_BUTTON)).click();
        classification.setClassificators(reviewers);

    }

    /**
     * assign the validators to validate the classification phasr
     * @param driver the web driver 
     * @param classification classification phase
     */
    public void assign_validator(WebDriver driver,Classification classification){
        Views.openAssignValidatorsPage(driver);

        WebElement rest_papers = driver.findElement(By.cssSelector("#home b"));
        if(rest_papers.getText().equals("Number of papers to assign :0")){
            getValidators(driver, classification);
            return;
        }
        driver.findElement(By.id("percentage")).clear();
        driver.findElement(By.id("percentage")).sendKeys("100");
        // we select some random users
        ArrayList<RelisUser> reviewers = Views.chooseUserforScreening(
                driver,classification.getNumber_of_validator());
        // we submit the assaignment
        driver.findElement(By.className(ScreeningUtils.CLASS_SUCCESS_BUTTON)).click();
        classification.setValidators(reviewers);
    }


    /**
     * this method get the users who were assign for the validation phase
     * @param driver the web driver
     * @param classification the classificatin objet
     */
    private void getClassificator(WebDriver driver, Classification classification) {
        showProgressBarForClassificators(driver);
        // we assign as classificators
        classification.setClassificators(Views.extractUsers(driver));

    }


    /***
     * this method retrieve the validators whom are assigned for
     * the validation phase
     * @param driver the web driver
     * @param classification classification object
     */
    private void getValidators(WebDriver driver, Classification classification){

        showProgressBarForValidationPhase(driver);
        classification.setValidators(Views.extractUsers(driver));
    }




//    /**
//     * get all the papers for the classification phase
//     * @param driver the web driver
//     * @return arraylist of Paper
//     */
//    public static ArrayList<Paper> getAllPapersToCLassify(WebDriver driver){
//        ProjectController.openAllPapersPage(driver);
//
//        return Utility.getAllPapersToCLassify(driver);
//
//    }

    private String  openNextPaperInTable(WebDriver driver, int index){

        WebElement table = driver.findElement(By.id(ProjectUtils.ID_PROJECT_TABLE_USERS));
        List<WebElement> trs = table.findElements(By.tagName("tr"));
        trs.remove(0);
        List<WebElement> next_paper = trs.get(0).findElements(By.tagName("td"));
        String tmp = next_paper.get(index).getText();
        String paper_key = tmp.substring(0, tmp.indexOf(" "));
        next_paper.get(index).findElement(By.tagName("a")).click();
        return paper_key;
    }

    private  WebElement getNextPaperToValidateWebElement
            ( List<WebElement> papers, String filter){

        return papers.stream()
                .filter(ClassificationView::isNotValidateYet)
                .findFirst().
                orElse(null);
    }
    private String  getNextPaperForValidation(WebDriver driver){
        openValidatedPapersPage(driver);
        BiFunction<List<WebElement>,String,WebElement> nextPaperToValide =
                this::getNextPaperToValidateWebElement;
       WebElement nextPaper = Views.work_through_table(driver,nextPaperToValide,"Correct");
        assert nextPaper != null;
        List<WebElement> next_paper = nextPaper.findElements(By.tagName("td"));
        String tmp = next_paper.get(1).getText();
        String paper_key = tmp.substring(0, tmp.indexOf(" "));
        next_paper.get(1).findElement(By.tagName("a")).click();
        return paper_key;
    }

    private static boolean isNotValidateYet(WebElement p) {

        List<WebElement> tds = p.findElements(By.tagName("td"));
        if(tds.get(3).getText().equals(""))
            return true;
        return false;
    }

    /**
     * get the next paper to classify
     * @param driver the web driver
     */
    public String getNextPaperForClassification(WebDriver driver){


        String paper_key = openNextPaperInTable(driver,0);
        List<WebElement> panels = driver.findElements(By.className("x_panel"));
        WebElement classifyPaperSection = panels.get(1);
        WebElement ul = classifyPaperSection.findElement(By.tagName("ul"));
        List<WebElement> li_s = ul.findElements(By.tagName("li"));

        li_s.get(0).findElement(By.tagName("a")).click();
        return paper_key;

    }


    public void validateNextPaper(WebDriver driver, Classification classification){
        Views.openValidatePaperPage(driver);
        String paper_key = getNextPaperForValidation(driver);
        ClassificatedPaper classificatedPaper = classification.getClassifiedPaperByKey(paper_key);
        List<WebElement> panels = driver.findElements(By.className("x_panel"));
        WebElement classifyPaperSection = panels.get(1);
        WebElement ul = classifyPaperSection.findElement(By.tagName("ul"));
        try {
            Views.clickLiWebElement(driver,ul,"Correct");
            String not = "";
            classificatedPaper.addValidation("Correct", not);
        } catch (Exception e){};




    }
    /**
     * this method fill dom input for a  category that has sub-categories
     * @param driver  the current web driver
     * @param classification classification object
     * @param key the paper key
     * @param btn_index the nth category that has subcategories
     */
    public void fillSubCategory(WebDriver driver, Classification classification,String key,int btn_index){
            List<WebElement> buttons = Views.findElementsBy(driver, By.cssSelector(".fa-plus"));
            buttons.get(btn_index).click();
            Utility.sleep(1);
            driver.switchTo().activeElement();
            String labele = Views.findElementBy(driver, By.className("modal-title")).getText();
            System.out.println("LABEL SUB CATEGORY =>" + labele);
            labele = labele.substring( labele.indexOf("Add  : ") + "Add  : ".length());
            
            List<WebElement> sections = Views.findElementsBy(driver, By.cssSelector(CSS_RELIS_MODALS_FORM));


            Category category = classification.getCategoryByDisplayName(key,labele);
            
            int index =0, max= category.getSubCategorys().size();
            for(WebElement element : sections){
                String labelCategory = Views.findElementBy(driver,By.tagName("label")).getText();
                labelCategory = labelCategory.replace("*","").trim();
                if(index ==0 ){

                    category.fillWebElementInput(driver,element);
                    System.out.println(category.displayDataContent());
                } else {
                    Category subCategory = category.getSubCategoryByDisplayName(labelCategory);
                    System.out.println("calling the sub cat " + subCategory.displayDataContent() );
                    subCategory.fillWebElementInput(driver,element);
                }

                if(++index >= max) break;
            }
            driver.findElement(By.id("submit_but")).click();
        //    Utility.sleep(1);
    }

    public void edit_classification(WebDriver driver, Classification classification){
        Views.openSuBMenuFrom(driver,LK_CLASSIFY_MENU,LK_My_CLASSIFIED);
        String key = getNextPaperForClassification(driver);
        System.out.println("key=" + key);


    }


    /**
     * this function is serve to fill all the dependent dynamic list
     * like an example after user choice an entry of the independent list
     *  the entry choosed will also be visible in the dependent list options.
     * @param driver the webdriver
     * @param classificatedPaper the paper that we already classificated
     */
    private void fill_dependent_categories(WebDriver driver, ClassificatedPaper classificatedPaper){


        // get the non-fill input
        ArrayList<Category> categories = classificatedPaper.getDependedAndHasDependsSubCategories();
        // get all the inputs for the categories
        List<WebElement> inputs = driver.findElements(By.className(CLASS_FORM_GROUP));
        inputs = inputs.stream().filter(Objects::nonNull).collect(Collectors.toList());
        AtomicInteger i= new AtomicInteger();
        int max = classificatedPaper.getCategories().size()-1;
        // we remove the first one which is the paper title and it can't be modified
        inputs.remove(0);
        // we loop through all the inputs
        for( WebElement input : inputs){

            String labelCategory = input.findElement(By.tagName("label")).getText();
            labelCategory = labelCategory.replace("*","").trim();
            // for the dependent category
            for( Category category : categories) {
                if(labelCategory.equals(category.getDisplayName()))
                    // we fill the dependent category input
                    category.fillWebElementInput(driver,input);
            }
            // are we arrive at the end?
            if( (i.incrementAndGet() ) >= max ) break;
        }
    }

    /**
     * this function is take care the classification phase
     * and after it's will launch the test to check whether
     * all the input are the same as we entered.
     * @param driver the webdriver instance
     * @param classification the classification object
     */
    public void classifyAndTest(WebDriver driver, Classification classification){

        int max = classification.getPapersToClassifyLength();

        for (int i = 0; i < max; i++) {

            classifyPaperSection(driver,classification);
        }
        // launch the test
       // extracDOM_classification_values(driver,classification);
    }
    private void classifyPaperSection(WebDriver driver, Classification classification){
        openClassifyPaperPage(driver);
        String key = getNextPaperForClassification(driver);
        Paper paper = classification.getPaper(key);
//        System.out.println("paper to classify=" + paper);
        assert paper != null;
        ClassificatedPaper classificatedPaper = classification.getClassifiedPaperByKey(key);
        int inde =0, maximum = classification.getAllCategories().size();
        boolean hasSubCategory = false, haveToFill=false;
        List<WebElement> inputs = driver.findElements(By.className(CLASS_FORM_GROUP));
        System.out.println("on a " + inputs.size() +" category %%%%%%%%%%%%%%%%%%%%%%%");
        for(WebElement input : inputs){
            String labelCategory = input.findElement(By.tagName("label")).getText();
            System.out.println(" label :> " + labelCategory);
            labelCategory = labelCategory.replace("*","").trim();
            Category category = classification.getCategoryByDisplayName(key, labelCategory);
            if(input.findElement(By.tagName("div")).getText().equals(NOT_READY_FIELD)){
                hasSubCategory = true;

            } else {
                System.out.println("cat en cours de traitement=" + category.displayDataContent());
                if(category instanceof DependantDynamicCategory){
                    if(!((DependantDynamicCategory) category).getDependent_on().hasSubCategory() ){
                        category.fillWebElementInput(driver,input);
                    } else  haveToFill = true;
                } else
                category.fillWebElementInput(driver,input);
            }

            if((++inde+2) >= maximum) break;

        }
        driver.findElement(By.className(CLASS_BTN_SUCCES)).click();
        if(hasSubCategory){
            driver.findElement(By.cssSelector(CSS_EDIT_CLASSIFICATION_BTN)).click();

            List<WebElement> buttons = driver.findElements(By.cssSelector(".fa-plus"));
            for(int i =0; i< buttons.size();i++){
                fillSubCategory(driver,classification,key,i);

            }
            if(haveToFill)
                fill_dependent_categories(driver,classificatedPaper);
            driver.findElement(By.className(CLASS_BTN_SUCCES)).click();

        }



    }



    /**
     * choose a criteria to resolve a conflict
     * @param driver the web driver
     * @param critere the criteria we wanna choose
     */
    public static void chooseEntryOptions(WebDriver driver,WebElement elem, String critere){


        elem.findElement(By.className(ScreeningUtils.CLASS_CRITERIA_OPTIONS_RESOLVING_CONFLICT))
                .sendKeys(Keys.ENTER);
        WebElement element = driver.findElement(By.cssSelector(".select2-results__options"));
        List<WebElement> options = element.findElements(By.tagName("li"));
        for (WebElement li : options){
            if(li.getText().equals(critere)) {
                new WebDriverWait(driver,4).until(ExpectedConditions.elementToBeClickable(li)
                ).click();
                break;
            }
        }

    }


    /**
     *
     * @param elements
     * @param o
     * @return
     */
    private static int nextClassifiedsPapers(List<WebElement> elements, Object o){

        Classification classification = (Classification) o;
        ArrayList<String> headers = classification.getCategories_displayed_names();
        elements.remove(0);
        for (WebElement element : elements){

            List<WebElement> tds = element.findElements(By.tagName("td"));
            tds.remove(0);
            String key= tds.get(0).getText() .substring(0, tds.get(0).getText().indexOf(" "));
//            Category category = classification.getCategoryByDisplayName(key, headers.get(0));
            int i=0;
           for(WebElement elem : tds){
               if(i == 0) {
                   i++;
                   continue;
               }
               ClassificatedPaper value = classification.getClassifiedPaperByKey(key);
               String msg = "header={"+i+"} = "+headers.get(i)+", value={"+ elem.getText()+"}";
               System.out.print(msg +" VS => ");
               boolean l = value.compareDisplayNameVal(headers.get(i), elem.getText());
               System.out.print("   ,response=" +l);
               msg += " COMPARAISON_VALUE= " +l;
               i++;
               System.out.println();
               classification.append_test_message(msg+"\n");
           }
            System.out.println();

        }
        return 1;
    }

    /**
     * thid method will be call after the end of the classification phase
     * and will check if all the entries are correct as we entered 
     * @param driver the web driver
     * @param classification classification object
     */
    public void extracDOM_classification_values(WebDriver driver, Classification classification){
        getTableHeaderData(driver,classification);
        Utility.work_through_table_id(driver,ClassificationView::nextClassifiedsPapers,classification);


    }
    /**
     * this mey
     * @param values
     * @param o
     * @return
     */
    private static int checkValidationResult(List<WebElement> values, Object o){

        Classification classification = (Classification) o;

        for(WebElement element : values){

            List<WebElement> table_element = element.findElements(By.tagName("td"));
            String text = table_element.get(1).getText();
            String key = text.substring(0, text.indexOf(" "));
            ClassificatedPaper classificatedPaper = classification.getClassifiedPaperByKey(key);
            String validation_response = table_element.get(3).getText();
            String note = table_element.get(4).getText();

            boolean correct = validation_response.equals(classificatedPaper.getValidated_msg());
            String str ="VALIDATION_DECISION => {"+validation_response+"} VS " +
                    "{" +classificatedPaper.getValidated_msg()+"} => " + correct;
            classification.append_test_message(str+"\n");
            System.out.print(str);
            correct = note.equals(classificatedPaper.getValidation_note());
            str = "VALIDATION NOTE => {"+ note+"} VS {" +classificatedPaper.getValidation_note()+"} => " + correct;
            classification.append_test_message(str+"\n");
            System.out.println(" , "+str);

        }
        return 1;

    }
    public void  extracDOM_validation(WebDriver driver, Classification classification){
        Views.openSuBMenuFrom(driver, LK_VALIDATION_MENU,LK_VALIDATED_PAPERS);
        Utility.work_through_table_id(driver,ClassificationView::checkValidationResult,classification);

    }
    private void getTableHeaderData(WebDriver driver,Classification classification){
        Views.openSuBMenuFrom(driver,LK_RESULTS_CLASSIFICATION,LK_TABLE_RESULT_OF_CLASSIFICATION);
        WebElement element  = driver.findElement(By.className( ScreeningUtils.CLASS_SCREENING_PHASES_TABLE));
        List<WebElement> table_elements = element.findElements(By.tagName("th"));
        table_elements.remove(0); // remove the index #1, #2 ...
        table_elements.forEach( t-> {
           System.out.println("header= " +t.getText()+" =>" + t.getAttribute("aria-label"));
            String name = t.getAttribute("aria-label");
            name  = name.substring(0, name.indexOf(":"));
            classification.addCategoryDisplayedName(name);

        });
    }






























    /**************************************************************************************************
     *
     *
     *
     *
     *
     ****************************************************************************************************/
    private static Paper extracPaperFrom(WebElement trs) {

        Paper paper1 = new Paper();

        List<WebElement> tds = trs.findElements(By.tagName("td"));

        String key_title = tds.get(0).getText();

        String key = key_title.substring(0, key_title.indexOf(" "));
        String title = key_title.substring(key_title.indexOf(" - ")+3);
        paper1.setTitle(title);
        paper1.setKey(key);
        return paper1;
    }










    /***************************************************************************************************
     *
     *
     * ALL THE NEXT SECTION IS THE METHODS FOR SHOWING A SPECIFIC PAGE
     *
     *
     *****************************************************************************************************/
    public static void openClassification(WebDriver driver){

        Views.openProjectPhase(driver, ClassificationUtils.CLASSIFICATION_NAME,
                CSS_GOTO_CLASSIFICATION);
    }



    /**
     * open the 'my pending assignments' page
     * @param driver the web driver
     */
    public void openMyPendingPapersToClassify(WebDriver driver){

        WebElement classification_menu = Views.getSideBarMenuOptionsOf(driver,CLASSIFICATION_NAME);

        try {
            classification_menu.findElement(By.linkText(LK_MY_PENDING_PAPERS_TO_CLASSIFY)).click();
        } catch (Exception e){
            JavascriptExecutor je = (JavascriptExecutor) driver;
            je.executeScript("arguments[0].scrollIntoView(true);",
                    classification_menu.findElement(By.linkText(CLASSIFICATION_NAME)));
            classification_menu.findElement(By.linkText(CLASSIFICATION_NAME)).click();
            Views.clickLiWebElement(driver,
                    classification_menu,LK_MY_PENDING_PAPERS_TO_CLASSIFY);
        }

    }


    /**
     * Open the all assignments papers to classify papge
     * @param driver the web driver
     */
    public void openAllPaperToCLassifyPage(WebDriver driver){

        WebElement menu = Views.getSideBarMenuOptionsOf(driver,CLASSIFICATION_NAME);

        try {
            menu.findElement(By.linkText(LK_ALL_PAPERS_TO_CLASSIFY)).sendKeys(Keys.ENTER);
        } catch (Exception e){
            JavascriptExecutor je = (JavascriptExecutor) driver;
            je.executeScript("arguments[0].scrollIntoView(true);",
                    menu.findElement(By.linkText(CLASSIFICATION_NAME)));
            menu.findElement(By.linkText(CLASSIFICATION_NAME)).click();
            Views.clickLiWebElement(driver,menu,LK_ALL_PAPERS_TO_CLASSIFY);
        }

    }

    /**
     * open the progress bar page of every classificator
     * @param driver the webdriver
     */
    public void showProgressBarForValidationPhase(WebDriver driver){

       Views.openSuBMenuFrom(driver,LK_VALIDATION_MENU,LK_CLASSIFICATION_PROGRESS);
    }



    /**
     * open the progress bar page of every classificator
     * @param driver the webdriver
     */
    public void showProgressBarForClassificators(WebDriver driver){

        Views.openSuBMenuFrom(driver,CLASSIFICATION_NAME,LK_CLASSIFICATION_PROGRESS);
    }


    //

    public void openClassifyPaperPage(WebDriver driver){

        driver.findElement(By.linkText(ScreeningUtils.LK_DASHBORD_LINK)).click();
        driver.findElement(By.cssSelector(ClassificationUtils.CSS_CLASSIFY_PAPER_PAGE))
                .sendKeys(Keys.ENTER);


    }



    public void openValidatedPapersPage(WebDriver driver){

        Views.scrollToElement(driver,By.linkText(LK_VALIDATION_MENU));
        Views.openSuBMenuFrom(driver,LK_VALIDATION_MENU,LK_VALIDATED_PAPERS);


    }


    /**
     * this function will enable the validation for the classification phase
     * @param driver the web driver
     */
    public static void enable_validation(WebDriver driver){
        Views.enable_validation(driver, LABEL_CLASSIFICATION, LABEL_ENABLE_VALIDATION);
    }

    /**
     * this function will desable the validation for the classification phase
     * @param driver the web driver
     */
    public static void desable_validation(WebDriver driver) {

        Views.desable_validation(driver, LABEL_CLASSIFICATION, LABEL_ENABLE_VALIDATION);
    }

}


