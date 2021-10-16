package controller;

import model.Classification;
import model.Paper;
import org.openqa.selenium.WebDriver;
import utils.Utility;
import view.ClassificationView;
import view.ScreeningView;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class ClassificationController {


    private static final ClassificationView views = new ClassificationView();

    public void openClassificationPhase(WebDriver driver){

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


        openUserPendingPapers(driver);

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
        System.out.println(classification);

    }
}
