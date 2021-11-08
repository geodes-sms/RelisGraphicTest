package view;

import model.*;
import model.user_work.QualityAssementSessionWork;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import utils.Utility;

import java.util.ArrayList;
import java.util.List;

import static utils.QAUtils.*;
import static utils.ScreeningUtils.*;

public class QAViews {




    public void assign_qa_papers(WebDriver driver, QualityAssement qa){
        Views.openUserAssignmentPage(driver, CSS_ASSIGN_USERS_TASK);
        WebElement rest_papers = driver.findElement(By.cssSelector("#home b"));
        if(rest_papers.getText().equals("Number of papers to assign :0")){
            getUsersForTask(driver, qa);
            return;
        }
        // we select some random users
        ArrayList<RelisUser> reviewers = Views.chooseUserforScreening(
                driver, qa.getNumberOfParticipants());
        // we submit the assaignment
        driver.findElement(By.className(CLASS_SUCCESS_BUTTON)).click();
        qa.setPhaseWorkSession(reviewers);
    }

    private void getUsersForTask(WebDriver driver, QualityAssement qa) {

        Views.openSuBMenuFrom(driver, LK_QA_MENU,LK_PROGRESS_QA);
        ArrayList<RelisUser> participants = Views.extractUsers(driver);
        qa.setPhaseWorkSession(participants);

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
            Views.openSuBMenuFrom(driver,LK_PLANNING, LK_QUESTIONS);
        } catch (Exception e){
            Views.scrollToElement(driver,By.linkText(LK_PLANNING));
            Views.openSuBMenuFrom(driver,LK_PLANNING, LK_QUESTIONS);
        }

    }

    public void ShowAnswersPage(WebDriver driver){
        try{
            Views.openSuBMenuFrom(driver,LK_PLANNING, LK_ANSWERS);
        } catch (Exception e){
            Views.scrollToElement(driver,By.linkText(LK_PLANNING));
            Views.openSuBMenuFrom(driver,LK_PLANNING, LK_ANSWERS);
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
       for(WebElement elem : list){

           List<WebElement> tds = elem.findElements(By.tagName("td"));
           String question = tds.get(1).getText();
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
        for(WebElement elem : list){

            List<WebElement> tds = elem.findElements(By.tagName("td"));
            String answer = tds.get(1).getText();
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

        WebElement nextQuestion = getNextQuestion(driver);
        if(nextQuestion == null) return;

        List<WebElement> elements= driver.findElements(By.className(CLASS_X_PANEL));
        WebElement nextPaper = elements.get(1);

        String title = nextPaper.findElement(By.cssSelector("."+CLASS_PAPERS_TITLE +" u")).getText();

        List<WebElement> tds_elem = nextQuestion.findElements(By.tagName("td"));
        String question = tds_elem.get(0).getText();
        question = question.substring(0,question.indexOf("\n"));
        WebElement droite_elem  = tds_elem.get(0).findElement(By.cssSelector(".droite"));
        String answer = sessionWork.getAnswerFor(question,title);
        String css_selector = "a[title='"+ answer+"']";

        droite_elem.findElement(By.cssSelector(css_selector)).sendKeys(Keys.ENTER);

        doPaperQA(driver, sessionWork);
    }


    public boolean is_empty_assess(WebDriver driver){
        List<WebElement> elements = driver.findElements(By.cssSelector(".row"));
        WebElement content = elements.get(1);
        if(content.getText() .equals(NOT_RECORD_FOR_QA))
            return true;
        return false;
    }






}
