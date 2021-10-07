package controller;

import databases.PapersDataBase;
import model.Paper;
import model.RelisUser;
import model.Screening;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import utils.ScreeningUtils;
import utils.Utility;
import view.ScreeningView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class ScreeningController {


    private ArrayList<String> assignments = new ArrayList<>();
    private static final ScreeningView screeningView = new ScreeningView();
    private ArrayList<WebElement> screening_phases;


    /**
     *  get all the assigned papers
     * @param driver the web driver
     */
    public void getAllAssignmentsData(WebDriver driver){
        if(assignments.size() == 0){
            ScreeningView.showAllAssigmentsPage(driver);
            Utility.work_through_table(driver);
        }


    }
    public void startUserScreeningPhase( RelisUser user){
        openCurrentScreeningPhase(user.getDriver());
        //driver.findElement(By.linkText(ScreeningUtils.LK_DASHBORD_LINK)).click();
       // screeningView.openScreeningPage(driver);

    }

    /**
     *
     * @param driver
     * @return
     */
    public ArrayList<RelisUser> getAssignedReviewers(WebDriver driver){

        return ScreeningView.getReviewer(driver);
    }

    public void openCurrentScreeningPhase(WebDriver driver){
        screeningView.openScreeningPage(driver);
    }



    public ArrayList<String> getAllAssignmentsPapers(RelisUser user_screening){

        ScreeningView.showMyAssignment(user_screening.getDriver());
        return Utility.work_through_table(user_screening.getDriver());

    }

    public ArrayList<String> getUserPendingAssignments(RelisUser user){
        ScreeningView.showMyPendingAssignmentsPage(user.getDriver());
        return Utility.work_through_table(user.getDriver());
    }

    public ArrayList<String> getUserScreenedAssignements(RelisUser user){

        ScreeningView.showAllScreenedAssignmentsPage(user.getDriver());
        return Utility.work_through_table(user.getDriver());
    }

    public ArrayList<String> getAllScreenedAssignments(RelisUser user){
        ScreeningView.showAllScreenedAssignmentsPage(user.getDriver());
        return Utility.work_through_table(user.getDriver());

    }
    public ArrayList<String> getUserPapersAssignments(RelisUser user){
        ScreeningView.showMyPendingAssignmentsPage(user.getDriver());
        return Utility.work_through_table(user.getDriver());
    }

    public void setUserCurrentAssignmentForScreening(RelisUser user){
        user.getCurrentScreeningPhase().setMy_assignments(getUserPapersAssignments(user));
    }


    public void makeReadyForScreening(RelisUser user){
        setUserCurrentAssignmentForScreening(user);
        openCurrentScreeningPhase(user.getDriver());

    }

    private String extrackPaperKey(String sentence){

        sentence = sentence.substring(8);
        int pos = sentence.indexOf(" ");

        return sentence.substring(0,pos);

    }
    public void finishScreening(RelisUser user){;
        int size = user.getAssignmentsLength();

        WebDriver driver = user.getDriver();
        while (size-- > 0){
           String key = driver.findElement(By.className(ScreeningUtils.CLASS_CURRENT_SCREENING_PAPER)).getText();

           String decision = user.getPaperDecision(key);
            if (decision.equals("TRUE")) screeningView.includePaper(driver);
            else screeningView.excludePaper(driver, decision);
        }






    }





}
