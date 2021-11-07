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

    public void openClassificationPhase(WebDriver driver){

        try {

            driver.findElement(By.linkText(ScreeningUtils.LK_DASHBORD_LINK)).click();
        }catch (Exception e){
            driver.findElement(By.linkText(ProjectUtils.LK_CURRENT_PROJECT)).click();
        }

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
        Utility.getPapersForClassification(driver,toClassify);
        classification.setPapersToClassify(toClassify);
        classification.classifyAllPapers();
    }


    public void openUserPendingPapers(WebDriver driver){
        views.openMyPendingPapersToClassify(driver);
    }

    public void setUsersPendingAssignments(WebDriver driver, Classification classification){

      ArrayList<Paper> papers = views.getUserPapersToCLassify(driver);
      classification.setPapersToClassify(papers);

    }


    public void assignClassificators(WebDriver driver, Classification classification){

        views.assign_classificator(driver,classification);


    }

    public void assign_validators(WebDriver driver, Classification classification){
        views.assign_validator(driver,classification);
        //System.out.println(classification);
    }

    public void makeReadyForValidationPhase(WebDriver driver, Classification classification){

        try {
            openClassificationPhase(driver);
        } catch (Exception e){};
        if(classification.getValidators().size() == 0){
            assign_validators(driver,classification);
        }


    }
    public void makeReadyForClassification(WebDriver driver, Classification classification){

        openClassificationPhase(driver);
        if(classification.getClassifierLength() == 0){
            assignClassificators(driver,classification);
        }
        if(classification.getPapersToClassifyLength() == 0){
            setUpAClassification(driver,classification);
        }

    }

    /**
     * this method finish classifies every paper
     * @param driver the web driver
     * @param classification objecy
     */
    public void finishClassificationPhase(WebDriver driver, Classification classification){

        makeReadyForClassification(driver,classification);

            classification.getClassificators().parallelStream()
                    .forEach(relisUser -> {
                        Initialiazer initialiazer = new Initialiazer();
                        initialiazer.init();
                        WebDriver drive = initialiazer.getWebDriver();
                        ConnexionController.connect(drive,relisUser);
                        ProjectController.openProject(drive,"Project 2");
                        ClassificationView.openClassification(drive);
                        views.classifyAndTest(drive,classification);
                    });
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
