package controller;

import model.Classification;
import model.Paper;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import utils.Initialiazer;
import utils.ProjectUtils;
import utils.ScreeningUtils;
import utils.Utility;
import view.ClassificationView;
import view.Views;

import java.util.ArrayList;
import java.util.List;

public class ClassificationController {


    private static final ClassificationView views = new ClassificationView();

    /**
     * this method open the classification phase
     * @param driver the web driver
     */
    public void openClassificationPhase(WebDriver driver){

        // go the principal menu page 
        try {

            driver.findElement(By.linkText(ScreeningUtils.LK_DASHBORD_LINK)).click();
        }catch (Exception e){
            driver.findElement(By.linkText(ProjectUtils.LK_CURRENT_PROJECT)).click();
        }
        // call the view open in order to open the classification phase
        views.openClassification(driver);
    }


    /**
     * set the papers to classify
     * @param driver the web driver
     * @param classification the classificator object
     *
     */
    public void setUpAClassification(WebDriver driver, Classification classification){

        if(classification == null ) return;
        ArrayList<Paper> toClassify = new ArrayList<>();
        // pass the arraylist by reference so to populate it 
        Utility.getPapersForClassification(driver,toClassify);
        classification.setPapersToClassify(toClassify);
        // classify all the papers by taking some decisions
        classification.classifyAllPapers();
    }


    /**
     * 
     * this methode open the user pending assigment for the classification
     * phase
     * @param driver the web driver
     */
    public void openUserPendingPapers(WebDriver driver){
        views.openMyPendingPapersToClassify(driver);
    }

    /**
     * this method open the user pending assignments and
     * extract it to  an arraylist of papers,
     * @param driver the web driver
     * @param classification classification object
     */
    public void setUsersPendingAssignments(WebDriver driver, Classification classification){

      ArrayList<Paper> papers = views.getUserPapersToCLassify(driver);
      classification.setPapersToClassify(papers);

    }

    /**
     * this method assigns classificators for the classification phase
     * @param driver the web driver
     * @param classification the classification object
     */

    public void assignClassificators(WebDriver driver, Classification classification){
        // delegate the task
        views.assign_classificator(driver,classification);


    }
    /**
     * this method assigns validators for the phase of validation
     * @param driver the web driver
     * @param classification the classification object
     */
    public void assign_validators(WebDriver driver, Classification classification){
        // delegate the method
        views.assign_validator(driver,classification);
        //System.out.println(classification);
    }

    /**
     * a function that take a classification phase object and initialize 
     * and set up every thing so we can proceed to validate papers
    * @param driver the web driver
     * @param classification the classification object
     */
    public void makeReadyForValidationPhase(WebDriver driver, Classification classification){

        try {
            openClassificationPhase(driver);
        } catch (Exception e){};
        // do we retrieve the validators from the dom?
        if(classification.getValidators().size() == 0){
            // if not, we assign or extract the validators from the DOM
            assign_validators(driver,classification);
        }


    }
    /**
     * this function make ready everything for the classification phase
     * like assignment of validators, get all the papers to classify
     * @param driver the web driver
     * @param classification instance of classification
     */
    public void makeReadyForClassification(WebDriver driver, Classification classification){

        openClassificationPhase(driver);
        // get the validators or assign 
        if(classification.getClassifierLength() == 0){
            assignClassificators(driver,classification);
        }
        // get all the papers to classify
        if(classification.getPapersToClassifyLength() == 0){
            setUpAClassification(driver,classification);
        }

    }

    /**
     * this method finish the classification phase
     * like classifying  every paper
     * @param driver the web driver
     * @param classification objecy
     */
    public void finishClassificationPhase(WebDriver driver, Classification classification){

        makeReadyForClassification(driver,classification);
            // for every classificators
            classification.getClassificators().parallelStream()
                    .forEach(relisUser -> {
                        // create a new instance of web driver and set up
                        Initialiazer initialiazer = new Initialiazer();
                        initialiazer.init();
                        WebDriver drive = initialiazer.getWebDriver();
                        // connect the classificator
                        ConnexionController.connect(drive,relisUser);
                        ProjectController.openProject(drive,"Project 2");
                        ClassificationView.openClassification(drive);
                        // classify the papers
                        views.classifyAndTest(drive,classification);
                    });
                    // test the classification result
            extractDOM_classificationValues(driver,classification);

    }

    public void finishValidationPhase(WebDriver driver, Classification classification){

        //views.openValidatePaperPage(driver);
        makeReadyForValidationPhase(driver,classification);
        int i=0, max =classification.getPapersToClassifyLength();
        while (++i < max){
            views.validateNextPaper(driver,classification);
        }

        views.extracDOM_validation(driver,classification);

    }

    
    public void extractDOM_classificationValues(WebDriver driver,Classification classification){

        views.extracDOM_classification_values(driver,classification);
    }



}
