package controller;

import model.Classification;
import model.Paper;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import utils.ProjectUtils;
import utils.ScreeningUtils;
import utils.Utility;
import view.ClassificationView;

import java.util.ArrayList;

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
        ArrayList<Paper> toClassify = Utility.getAllPapersToCLassify(driver);

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
        System.out.println(classification);
    }
    public void classifNextPaper(WebDriver driver,Classification classification){


        views.classifyPaper(driver,classification);
    }

    private void makeReadyForClassification(WebDriver driver, Classification classification){

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
        while (true){
            try {

                views.classifyPaper(driver,classification);
            } catch (Exception e){
                e.printStackTrace();
                break;
            }

        }
    }

    public void finishValidationPhase(WebDriver driver, Classification classification){


        while (true){
            try {

                views.validateNextPaper(driver,classification);
            }catch (Exception e){
                e.printStackTrace();
                break;}
        }
    }
}
