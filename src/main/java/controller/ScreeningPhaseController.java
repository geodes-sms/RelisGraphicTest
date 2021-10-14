package controller;

import databases.PapersDataBase;
import model.*;
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

public class ScreeningPhaseController {




    private ArrayList<String> assignments = new ArrayList<>();
    private static final ScreeningView screeningView = new ScreeningView();

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

    public ArrayList<String> getScreeningPhaseName(WebDriver driver){
      return screeningView.getScreening_phasesNames(driver);
    }


    public static ArrayList<String> getAllAssignmentsPapers(RelisUser user_screening){

        ScreeningView.showMyAssignment(user_screening.getDriver());
        return Utility.work_through_table(user_screening.getDriver());

    }

    public static ArrayList<String> getUserPendingAssignments(RelisUser user){
        ScreeningView.showMyPendingAssignmentsPage(user.getDriver());
        return Utility.work_through_table(user.getDriver());
    }

    public static ArrayList<String> getUserScreenedAssignements(RelisUser user){

        ScreeningView.showAllScreenedAssignmentsPage(user.getDriver());
        return Utility.work_through_table(user.getDriver());
    }

    public static ArrayList<String> getAllScreenedAssignments(RelisUser user){
        ScreeningView.showAllScreenedAssignmentsPage(user.getDriver());
        return Utility.work_through_table(user.getDriver());

    }
    public static ArrayList<Paper> getUserPapersAssignments(PhaseWork user){
        ScreeningView.showMyAssignment(user.getParticipant().getDriver());
        ArrayList<String> papersKeys=  Utility.work_through_table(user.getParticipant().getDriver());
        ArrayList<Paper> p = new ArrayList<>();
        papersKeys.forEach(
                paperKey ->{
                    p.add((Paper) user.getPhase().getPaperByKey(paperKey).clone());
                }
        );
        return p;
    }






    public void assignReviewers(WebDriver driver, ScreeningPhase phase){

      ArrayList<RelisUser> users = ScreeningView.assign_papers(driver);
      users.forEach(p -> {

        PhaseWork phaseWork = new PhaseWork();
        phaseWork.setParticipant(p);
        phaseWork.setPhase(phase);
        phase.addPhaseWorkingUser(phaseWork);
      });
    }


  /**
   * function for Resolving the conflict between papers.
   * it changes all the user decision that needs
   * to be changed in order to  resolve all the conflicts
   * @param driver the web driver
   * @param phase the current phase
   */
  public void resolveConflict(WebDriver driver, ScreeningPhase phase){
      // first of all, resolve the conflict by choosing a random user decision
       phase.resolvePapersConflicts();
      // then choose this decision for the others users
      ScreeningView.resolve(driver,phase);
      // now all the conflict are resolved
    }









  public static int CountExcludeByCriteria(ArrayList<Paper> papers, Criteria c1) {
    return   papers
      .stream()
      .filter(p -> p.getCriteria() != null)
      .filter(p -> p.getCriteria().equals(c1))
      .mapToInt(p->  1)
      .sum();

  }





}
