package view;

import controller.ClassificationController;
import controller.ProjectController;
import databases.DataBase;
import jdk.jshell.execution.Util;
import lombok.val;
import model.Classification;
import model.Paper;
import model.QualityAssement;
import model.RelisUser;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import utils.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.function.BiFunction;
import java.util.random.RandomGenerator;

import static utils.ClassificationUtils.*;

public class ClassificationView {


    public ArrayList<Paper> getUserPapersToCLassify(WebDriver driver){


        openMyPendingPapersToClassify(driver);
        WebElement table = driver.findElement(By.id(ProjectUtils.ID_PROJECT_TABLE_USERS));
        return getAllPapersFrom(driver, table);
    }


    private static ArrayList<Paper> getAllPapersFrom(WebDriver driver, WebElement table){


        WebElement element;
        ArrayList<Paper> assigments= new ArrayList<>();
        while (true){
            try{
                // get all the papers present from the current table
                List<WebElement> papers = table.findElements(By.tagName("tr"));
                // we remove the first web element which is the table header
                papers.remove(0);
                papers.forEach(paper ->{
                    Paper paper_key = extracPaperFrom(paper);
                    assigments.add(paper_key);

                });
                // get web element for the next click link
                element = driver.findElement(By.id(ProjectUtils.ID_NEXT_PAPERS_PAGE));
                // there is no next table  ?
                if(Utility.hasClass(element,"disabled")) break;
                element.findElement(By.linkText("Next")).click();
            } catch (Exception e){
                e.printStackTrace();
                break;

            }
        }
        return assigments;
    }



    /**
     * this method assigns the user who participate in the classifaction phase
     * or if it's alredy assigned, it return the assigned users
     * @param driver the web driver
     * @param classification the classificator object
     */
    public void assign_classificator(WebDriver driver, Classification classification){

        openAssignClassificatorPage(driver);

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

    public void assign_validator(WebDriver driver,Classification classification){
        openAssignValidatorsPage(driver);

        WebElement rest_papers = driver.findElement(By.cssSelector("#home b"));
        if(rest_papers.getText().equals("Number of papers to assign :0")){
            getValidators(driver, classification);
            return;
        }
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


    /**
     * return the web element of a 'menu' of the left side bar
     * like an example the 'classification menu'
     * @param driver the web driver
     * @param name the menu name
     * @return a web element for the menu with name=name
     */
    private WebElement getSideBarMenuOptionsOf(WebDriver driver, String name){

        WebElement ul = driver.findElement(By.className(CLASS_SIDE_BAR_MENU));
        List<WebElement> li = ul.findElements(By.tagName("li"));

        return li.stream()
                .filter(p-> p.getText().equals(name))
                .findFirst()
                .orElse(null);
    }
    private void getValidators(WebDriver driver, Classification classification){

        showProgressBarForValidationPhase(driver);
        classification.setValidators(Views.extractUsers(driver));
    }




    /**
     * get all the papers for the classification phase
     * @param driver the web driver
     * @return arraylist of Paper
     */
    public static ArrayList<Paper> getAllPapersToCLassify(WebDriver driver){
        ProjectController.openAllPapersPage(driver);
        // select the table that contains the screening phaseas
        WebElement table = driver.findElement(By.className(ScreeningUtils.CLASS_SCREENING_PHASES_TABLE));
        return Utility.getAllPapersFrom(driver,table);

    }

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

        WebElement nextPaper = papers.stream()
                .filter(ClassificationView::isNotValidateYet)
                .findFirst().
                orElse(null);
        return nextPaper;
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
        String paper_key = getNextPaperForValidation(driver);
        List<WebElement> panels = driver.findElements(By.className("x_panel"));
        WebElement classifyPaperSection = panels.get(1);
        WebElement ul = classifyPaperSection.findElement(By.tagName("ul"));
        try {
            Views.clickLiWebElement(ul,"Correct");
        } catch (Exception e){};




    }

    public void classifyPaper(WebDriver driver, Classification classification){

        openClassifyPaperPage(driver);
        String key = getNextPaperForClassification(driver);
        Paper paper = classification.getPaper(key);
        assert paper != null;
        List<WebElement> inputs = driver.findElements(By.className(CLASS_FORM_GROUP));
        int index=0;
        for (WebElement input : inputs){
            if(index == 0){
                WebElement textArea = input.findElement(By.cssSelector(CSS_INPUT_TEXT));
                textArea.sendKeys(paper.getTitle());
            } else if (index >=1 && index <= 5){
                String labelCategory = input.findElement(By.tagName("label")).getText();
                String choice = DataBase.getInstance().getNextClassificationCategory(labelCategory);
                System.out.println("choice =" + choice);
                chooseEntryOptions(driver,input, choice);
            } else if(index >= 8 && index < 10){
                WebElement checkBox =  input.findElement(By.className(CLASS_SWITCH_CKECKBOX));
                if(new Random().nextBoolean())checkBox.click();
            } else if (index == 10){

                input.findElement(By.cssSelector(CSS_INPUT_TEXT)).sendKeys("4");
            } else if(index == 11){
                input.findElement(By.cssSelector(CSS_INPUT_TEXT)).sendKeys(
                        Utility.extractYearFrom(paper.getKey())
                );
            }
            index++;
        }

        driver.findElement(By.className(CLASS_BTN_SUCCES)).click();
        //driver.findElement(By.id(ClassificationUtils.ID_DOMAIN_CLASSIFICATION_FIELD)).click();


    }


    /**
     * choose a criteria to resolve a conflict
     * @param driver the web driver
     * @param critere the criteria we wanna choose
     */
    public static void chooseEntryOptions(WebDriver driver,WebElement elem, String critere){


        elem.findElement(By.className(ScreeningUtils.CLASS_CRITERIA_OPTIONS_RESOLVING_CONFLICT))
                .click();
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
    public void openClassification(WebDriver driver){

        driver.findElement(By.linkText(ProjectUtils.LK_CURRENT_PROJECT)).click();
        driver.findElement(By.className(ProjectUtils.CLASS_HOME_PROJECT)).click();

        openClassificationPhase(driver, ClassificationUtils.CLASSIFICATION_NAME);
    }

    /**
     * open the classification phase
     * @param driver the web driver
     * @param phaseName the word classification
     */
    private void openClassificationPhase(WebDriver driver, String phaseName){

        // select the table that contains the screening phaseas
        WebElement table = driver.findElement(By.className(ScreeningUtils.CLASS_SCREENING_PHASES_TABLE));

        System.out.println(table.getText());
        // get all the systematic review phases
        List<WebElement> trs = table.findElements(By.tagName("tr"));
        trs.remove(0);
        // we remove the first web element which is the table header

        for(WebElement element : trs){

            List<WebElement> tds = element.findElements(By.tagName("td"));

            WebElement phase = tds.get(0);
            if((phase !=null) && phase.getText().equals(phaseName)){
                WebElement gotoPhae;

                try{
                    gotoPhae = tds.get(4).
                            findElement(By.cssSelector(ClassificationUtils.CSS_GOTO_CLASSIFICATION));
                    gotoPhae.sendKeys(Keys.ENTER);
                } catch (Exception e){
                    element.findElement(By.cssSelector(ClassificationUtils.CSS_UNLOCK_CLASSIFICATION))
                            .sendKeys(Keys.ENTER);
                    openClassificationPhase(driver, phaseName);
                }

                return;
            }
        }
    }


    /**
     * open the 'my pending assignments' page
     * @param driver the web driver
     */
    public void openMyPendingPapersToClassify(WebDriver driver){

        WebElement classification_menu = getSideBarMenuOptionsOf(driver,CLASSIFICATION_NAME);

        try {
            classification_menu.findElement(By.linkText(LK_MY_PENDING_PAPERS_TO_CLASSIFY)).click();
        } catch (Exception e){
            classification_menu.findElement(By.linkText(CLASSIFICATION_NAME)).click();
            Views.clickLiWebElement(classification_menu,LK_MY_PENDING_PAPERS_TO_CLASSIFY);
        }

    }


    /**
     * Open the all assignments papers to classify papge
     * @param driver the web driver
     */
    public void openAllPaperToCLassifyPage(WebDriver driver){

        WebElement menu = getSideBarMenuOptionsOf(driver,CLASSIFICATION_NAME);

        try {
            menu.findElement(By.linkText(LK_ALL_PAPERS_TO_CLASSIFY)).sendKeys(Keys.ENTER);
        } catch (Exception e){
            menu.findElement(By.linkText(CLASSIFICATION_NAME)).click();
            Views.clickLiWebElement(menu,LK_ALL_PAPERS_TO_CLASSIFY);
        }

    }

    /**
     * open the progress bar page of every classificator
     * @param driver the webdriver
     */
    public void showProgressBarForValidationPhase(WebDriver driver){

        WebElement menu = getSideBarMenuOptionsOf(driver,CLASSIFICATION_NAME);

        try {
            menu.findElement(By.linkText(LK_CLASSIFICATION_PROGRESS)).sendKeys(Keys.ENTER);
        } catch (Exception e){
            menu.findElement(By.linkText(CLASSIFICATION_NAME)).click();
            Views.clickLiWebElement(menu,LK_CLASSIFICATION_PROGRESS);
        }
    }

    /**
     * this method open the classification phase
     * @param driver the web driver
     */
    public void openAssignClassificatorPage(WebDriver driver){

        driver.findElement(By.linkText(ScreeningUtils.LK_DASHBORD_LINK)).click();
        driver.findElement(By.cssSelector(ClassificationUtils.CSS_ASSIGN_CLASSIFICATORS_PAGE))
                .sendKeys(Keys.ENTER);

    }

    /**
     * open the progress bar page of every classificator
     * @param driver the webdriver
     */
    public void showProgressBarForClassificators(WebDriver driver){

        WebElement classification_menu = getSideBarMenuOptionsOf(driver,CLASSIFICATION_NAME);

        try {
            classification_menu.findElement(By.linkText(LK_CLASSIFICATION_PROGRESS)).click();
        } catch (Exception e){
            classification_menu.findElement(By.linkText(CLASSIFICATION_NAME)).click();
            Views.clickLiWebElement(classification_menu,LK_CLASSIFICATION_PROGRESS);
        }
    }

    /**
     * this method open the assign validators page
     * @param driver the web driver
     */
    public void openAssignValidatorsPage(WebDriver driver){

        driver.findElement(By.linkText(ScreeningUtils.LK_DASHBORD_LINK)).click();
        driver.findElement(By.cssSelector(CSS_ASSIGN_VALIDATORS_PAGE))
                .sendKeys(Keys.ENTER);

    }
    //

    public void openClassifyPaperPage(WebDriver driver){

        driver.findElement(By.linkText(ScreeningUtils.LK_DASHBORD_LINK)).click();
        driver.findElement(By.cssSelector(ClassificationUtils.CSS_CLASSIFY_PAPER_PAGE))
                .sendKeys(Keys.ENTER);

    }

    public void openValidatePaperPage(WebDriver driver){

        driver.findElement(By.linkText(ScreeningUtils.LK_DASHBORD_LINK)).click();
        driver.findElement(By.cssSelector(CSS_VALIDATE_BTN))
                .sendKeys(Keys.ENTER);

    }

    public void openValidatedPapersPage(WebDriver driver){
        WebElement classification_menu = getSideBarMenuOptionsOf(driver,LK_VALIDATION_MENU);
        System.out.println("Opening validation page");
        try {
            classification_menu.findElement(By.linkText(LK_VALIDATED_PAPERS)).click();
        } catch (Exception e){
            System.out.println("ELEM : " + classification_menu.getText());
            classification_menu.findElement(By.linkText(LK_VALIDATION_MENU)).sendKeys(Keys.ENTER);
            Views.clickLiWebElement(classification_menu,LK_VALIDATED_PAPERS);
        }

    }

}


