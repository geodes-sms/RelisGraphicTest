package controller;

import model.*;
import model.user_work.QualityAssementSessionWork;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import utils.QAUtils;
import utils.Utility;
import view.QAViews;
import view.Views;

import java.util.ArrayList;
import java.util.List;

public class QualityAssementController {

    private static final QAViews views = new QAViews();
    public static void openQA_page(WebDriver driver){

        Views.openProjectPhase(driver, QAUtils.Qa_entry_point_name,QAUtils.CSS_GOTO_QA);

    }


    /**
     * this method assign the reviewers for the QA phase
     * @param driver web driver
     * @param assement the QA object
     */
    public static void assign_roles(WebDriver driver, QualityAssement assement){

        ArrayList<RelisUser> users = views.assign_qa_papers(driver, assement.getNumberOfParticipants());
        assement.setPhaseWorkSession(users);
    }



    /**
     * this method assign the reviewers for the QA phase
     * @param driver web driver
     * @param assement the QA object
     */
    private static void assign_validators(WebDriver driver, QualityAssement assement){

        ArrayList<RelisUser> users = views.assign_qa_validators(driver, assement.getNumberOfValidators());
        assement.setValidators(users);
    }


    /**
     * this function will do the valiation phase for the qa
     * @param driver the web driver
     * @param project the project object
     */
    public static void validate_qa(WebDriver driver, Project project){
        QAViews.enable_validation(driver);
        assign_roles(driver,project.getQa());
        project.getQa().validate();
        project.getQa().getValidators()
                        .forEach( validator->
                                QAViews.finish_validation_phase(driver,project, validator));

    }



    /**
     * this method assign reviewers for the QA phase if it's not already assigned
     * it's also retrieve all the paper for this phase
     * @param driver the web driver
     * @param assement the QA object
     */
    public void setUpQualityAssements(WebDriver driver, QualityAssement assement){

        if(assement == null) return;
        System.out.println("***********************8 debut roles ******************");
        if(!assement.hasParticipant()){
            assign_roles(driver,assement);
        }
        System.out.println("fin de getting user roles");
        setQA_Papers(driver,assement);

        QuestionAnswesPaper qa = new QuestionAnswesPaper();
        // get the question for the qa
        getQuestions(driver,qa);

        // get all the answers from the dom
        getAnswer(driver,qa);

        assement.setQuestionAnswesPaper(qa);
        assement.makeReadyQASession();

    }




    private void setQA_Papers(WebDriver driver, QualityAssement assement){

        ProjectController.openAllPapersPage(driver);
        Utility.work_through_table_id(driver,QualityAssementController::addPaperToQA,assement);

    }

    public void get_or_set_papers_to_validate(WebDriver driver, QualityAssement assement){


    }

    /**
     * this method extract all the papers for this phase
     * it will click the 'paper in this phase menu'
     * then select 'all' and retrieve the papers
     * @param papers the web element of the table 'tr element'
     * @param object the QA object to assign the data
     * @return 1 if everything went good otherwise 0
     */
    private static int addPaperToQA(List<WebElement> papers, Object object){

        QualityAssement assement = (QualityAssement) object;
        ArrayList<Paper> qa_papers = new ArrayList<>();
        papers.remove(0); // remove the header
        for (WebElement element : papers){
            List<WebElement> tds = element.findElements(By.tagName("td"));

            String phrases = element.getText();
            String key = phrases.substring(0,phrases.indexOf(" "));
            String title = tds.get(0).findElement(By.tagName("a")).getAttribute("title");
            title = title.substring(title.indexOf(" - ")+3);
            Paper p = new Paper();
            p.setKey(key);
            p.setTitle(title);
            qa_papers.add(p);

        }
        assement.initQa_Papers(qa_papers);
        return 1;
    }


    public void setParticipantPapers(WebDriver driver){

        views.showMyAssignmentsPage(driver);
    }


    public void getQuestions(WebDriver driver, QuestionAnswesPaper qa){

        views.getQuestion(driver,qa);

    }

    /***
     * this method wll extract all the answer and their score
     * from the dom
     *
     * @param driver the webdriver
     * @param qa the qa element
     */
    public void getAnswer(WebDriver driver, QuestionAnswesPaper qa){
        views.getAnswers(driver,qa);
    }


    /**
     * this function will starts the qa phase
     * @param driver the web driver
     * @param sessionWork the user
     */
    public static void startQA_phase(WebDriver driver, QualityAssementSessionWork sessionWork){
//        QAViews.showAssessQAPage(driver); // go to the qa assess page
        QAViews.doPaperQA(driver,sessionWork); // asses the all the papers
        boolean done = views.is_empty_assess(driver);
        assert done;

    }


    public void do_qa_assess(WebDriver driver, QualityAssement assement) {

        assement.setNumberOfParticipants(1);
        setUpQualityAssements(driver,assement);

        assement.startParticipantsQA_session();
        assement.closeAllWebDriver();


    }
}
