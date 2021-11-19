package view;

import controller.ProjectController;
import controller.QualityAssementController;
import model.*;
import model.user_work.QualityAssementSessionWork;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import utils.ClassificationUtils;
import utils.FourthParamsFunctions;
import utils.Utility;

import java.util.ArrayList;
import java.util.List;

import static utils.QAUtils.*;
import static utils.ScreeningUtils.*;

public class QAViews {


    public ArrayList<RelisUser> assign_qa_papers(WebDriver driver, int numberOfUser){
        Views.openUserAssignmentPage(driver, CSS_ASSIGN_USERS_TASK);
        WebElement rest_papers = driver.findElement(By.cssSelector("#home b"));
        if(rest_papers.getText().equals("Number of papers to assign :0")){
            return getUsersForTask(driver);
        }
        // we select some random users
        ArrayList<RelisUser> reviewers = Views.chooseUserforScreening(
                driver, numberOfUser);
        // we submit the assaignment
        driver.findElement(By.className(CLASS_SUCCESS_BUTTON)).click();
        return reviewers;
    }


    /**
     * this function will assign the validators for the qa phase
     * @param driver the web driver
     * @param numberOfUser the number of user
     * @return an arraylist of the users
     */
    public ArrayList<RelisUser> assign_qa_validators(WebDriver driver, int numberOfUser){
        Views.openAssignValidatorsPage(driver);
        WebElement rest_papers = driver.findElement(By.cssSelector("#home b"));
        if(rest_papers.getText().equals("Number of papers to assign :0")){
            return getUsersForTask(driver);
        }
        // we select some random users
        ArrayList<RelisUser> reviewers = Views.chooseUserforScreening(
                driver, numberOfUser);
        // we submit the assaignment
        driver.findElement(By.className(CLASS_SUCCESS_BUTTON)).click();
        return reviewers;
    }


    private ArrayList<RelisUser> getUsersForTask(WebDriver driver) {

        Views.openSuBMenuFrom(driver, LK_QA_MENU,LK_PROGRESS_QA);
        return Views.extractUsers(driver);

    }

    public void showMyPendingPapersPage(WebDriver driver){
        Views.openSuBMenuFrom(driver,LK_QA_MENU, LK_MY_PENDING_PAPERS);
    }

    public void showMyAssignmentsPage(WebDriver driver){
        Views.openSuBMenuFrom(driver,LK_QA_MENU,LK_SCREENING_MY_ASSIGMENTS_PAGE);
    }
    public static void showAssessQAPage(WebDriver driver){
        Views.openSuBMenuFrom(driver,LK_QA_MENU,LK_ASSESS_PAGE);
    }

    public void showQuestionsPage(WebDriver driver){
        try{
            Views.open_admin_menu_options(driver,LK_PLANNING, LK_QUESTIONS);
        } catch (Exception e){

            Views.scrollToElement(driver,By.linkText(LK_PLANNING));
            Views.open_admin_menu_options(driver,LK_PLANNING, LK_QUESTIONS);
        }

    }

    public void ShowAnswersPage(WebDriver driver){
        try{
            Views.open_admin_menu_options(driver,LK_PLANNING, LK_ANSWERS);
            Utility.sleep(10);
        } catch (Exception e){
            Views.scrollToElement(driver,By.linkText(LK_PLANNING));
            Utility.sleep(10);
            Views.open_admin_menu_options(driver,LK_PLANNING, LK_ANSWERS);
        }
    }


    public static ArrayList<QA_Paper> extractParticipantPapersTitle(WebDriver driver, QualityAssement qualityAssement){
        showAssessQAPage(driver);
        ArrayList<QA_Paper> titles = new ArrayList<>();
        List<WebElement> papers_titles = driver.findElements(By.className(CLASS_PAPERS_TITLE));
        papers_titles.remove(0);
        for (WebElement element : papers_titles)
        {
            String title = element.findElement(By.tagName("u")).getText();
            titles.add(Utility.getQA_PaperByTitle(qualityAssement.getQa_papers(), title));
        }
        return titles;

    }


    public void getQuestion(WebDriver driver, QuestionAnswesPaper questionAnswesPaper){

        showQuestionsPage(driver);
        Utility.work_through_table_id(driver,QAViews::extractQuestion,questionAnswesPaper);

    }

    public void getAnswers(WebDriver driver, QuestionAnswesPaper questionAnswesPaper){

        ShowAnswersPage(driver);
        Utility.work_through_table_id(driver,QAViews::extractAnswer,questionAnswesPaper);

    }


    /**
     * retrieve all the question for the QA phase
     * @param list the 'tr' of the questions table
     * @param obj qa
     * @return int
     */
    private static int extractQuestion(List<WebElement> list, Object obj){
       QuestionAnswesPaper questions = (QuestionAnswesPaper) obj;
       list.remove(0); // remove the header
       for(WebElement elem : list){

           List<WebElement> tds = elem.findElements(By.tagName("td"));
           tds.forEach( p -> System.out.print(" " + p));
           String question = tds.get(1).getText();
           System.out.println("question $$$$$$$$$$$$$$$$$$$$$ =>" + question);
           questions.addQuestions(question);

       }

        return 1;
    }

    /**
     * retrieve all the answer for the QA phase
     * @param list the 'tr' of the questions table
     * @param obj qa
     * @return int
     */
    private static int extractAnswer(List<WebElement> list, Object obj){
        QuestionAnswesPaper questions = (QuestionAnswesPaper) obj;
        list.remove(0); // remove the header
        for(WebElement elem : list){

            List<WebElement> tds = elem.findElements(By.tagName("td"));
            String answer = tds.get(1).getText();
            System.out.println("answer ::::: " + answer);
            float score = Float.parseFloat(tds.get(2).getText());
            questions.addAnswer(answer,score);

        }

        return 1;
    }


//
    public static WebElement getNextQuestion(WebDriver driver) {

        try {
            List<WebElement> elements = driver.findElements(By.className(CLASS_X_PANEL));
            WebElement nextPaper = elements.get(1);

            WebElement table = nextPaper.findElement(By.tagName("table"));
            List<WebElement> trs = table.findElements(By.tagName("tr"));

            for (WebElement entry : trs) {

                List<WebElement> tds = entry.findElements(By.tagName("td"));
                WebElement droite_elem = tds.get(0).findElement(By.cssSelector(".droite"));
                List<WebElement> buttons = droite_elem.findElements(By.tagName("button"));
                long nextQuestion = buttons.stream()
                        .filter(btn -> Views.isWhiteBg(btn.getCssValue("background-color")))
                        .count();
                if (nextQuestion == 3) return entry;

            }
        } catch (Exception exception) {
            return null;
        }
        return null;
    }


    /***
     * this method proceed the qa phase by filling all the papers questions answers
     * 
     * @param driver the web drivrr
     * @param sessionWork a user connected to do the qa 
     */
    public static void doPaperQA(WebDriver driver, QualityAssementSessionWork sessionWork){

        int max = sessionWork.getQa_papers().size();
        System.out.println("Nombre de doPapers => " + max +"         )))))))))))))))))))))))))))))))))))))))))))))))00");
        for(int i =0; i < max*4; i++){

            // get the next question for the current paper
            WebElement nextQuestion = getNextQuestion(driver);
            if (nextQuestion == null){
                System.out.println("QUestion NULL &&&&&&&&&&&&$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$");
                continue;
            }
            // get all the papers title
            List<WebElement> elements= driver.findElements(By.className(CLASS_X_PANEL));
            // get the first one to assess
            WebElement nextPaper = elements.get(1);
            // retrieve the title for the first paper
            String title = nextPaper.findElement(By.cssSelector("."+CLASS_PAPERS_TITLE +" u")).getText();

            List<WebElement> tds_elem = nextQuestion.findElements(By.tagName("td"));
            String question = tds_elem.get(0).getText();
            question = question.substring(0,question.indexOf("\n"));
            WebElement droite_elem  = tds_elem.get(0).findElement(By.cssSelector(".droite"));
            String answer = sessionWork.getAnswerFor(question,title);
            System.out.println("Answer={" + answer+"} , title=" + title);
            String css_selector = "a[title='"+ answer+"']";

            droite_elem.findElement(By.cssSelector(css_selector)).sendKeys(Keys.ENTER);
        }

    }


    public boolean is_empty_assess(WebDriver driver){
        List<WebElement> elements = driver.findElements(By.cssSelector(".row"));
        WebElement content = elements.get(1);
        if(content.getText() .equals(NOT_RECORD_FOR_QA))
            return true;
        return false;
    }

    /**
     * this method will enable the validation for the Qa phase
     * @param driver the web drivedr
     */
    public static  void enable_validation(WebDriver driver){


        Views.enable_validation(driver,LABEL_QA, LABEL_ENABLE_VALIDATION);
    }

    /**
     * this method will desable the validation for the Qa phase
     * @param driver the web drivedr
     */
    public static  void desable_validation(WebDriver driver){


        Views.desable_validation(driver,LABEL_QA, LABEL_ENABLE_VALIDATION);
    }


    /**
     *
     * @param driver the web driver
     * @param paper_element the paper web elemnent
     * @param subject the qa object
     * @param object the relis user / the validator
     */
    public static int validate_papers_process(WebDriver driver,List<WebElement>
            paper_element, Object subject, Object object){

        System.out.println("dans la bonne function");
            RelisUser user = (RelisUser) object;
            int val = 0;
            QualityAssement assement = (QualityAssement) subject;
            paper_element.remove(0);
            for(WebElement element : paper_element){
//                System.out.println("Element ->"  + element.getText());
                List<WebElement> tds = element.findElements(By.tagName("td"));
                String validation_response_dom = tds.get(3).getText();
                // if the paper is validated continue
                if(!validation_response_dom.equals("")) continue;
                String user_full_name = tds.get(2).getText();
                // validate the papers
                if(user_full_name.equals(user.getFull_name())){

                    String paper_ids = tds.get(1).findElement(By.tagName("a")).getAttribute("title");
                    String selector = "a[title='"+ paper_ids+"']";
                    String key = paper_ids.substring(0, paper_ids.indexOf(" "));
                    QA_Paper paper = assement.getQa_papersByKey(key);
                    tds.get(1).findElement(By.cssSelector(selector)).click();
                    if(!paper.getValidation_response().equals(CORRECT)){
                        driver.findElement(By.cssSelector(CSS_INCORRECT_BTN)).click();
                        driver.findElement(By.cssSelector(CSS_INCORRECT_BTN)).click();
                        Views.make_incorrect_decision(driver, paper.getValidation_response_note());
                    }
                    driver.findElement(By.cssSelector(CSS_CORRECT_BTN)).click();
                    val = 1;
                    break;
                }
            }
            return val;
        }



        public static int validate_demo(WebDriver driver, QualityAssement qa , RelisUser user){

            Views.open_validation_result_page(driver);
            FourthParamsFunctions functions  = QAViews::validate_papers_process;
           return Utility.find_element_table_id(driver,functions, user, qa);
        }



    public static void finish_validation_phase(WebDriver driver, Project project, RelisUser user){

        int max = 0;

           while (max != 1)  max = validate_demo(driver, project.getQa(), user);


    }



}
