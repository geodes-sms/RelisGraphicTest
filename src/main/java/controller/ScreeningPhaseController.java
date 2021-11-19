package controller;

import model.*;
import model.user_work.ScreeningPhaseWork;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import utils.Utility;
import view.ScreeningView;

import java.util.ArrayList;

import static org.testng.Assert.assertTrue;

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
            //Utility.getAllPapers(driver);
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

    public String openCurrentScreeningPhase(WebDriver driver){
        return screeningView.openScreeningPage(driver);
    }

    public ArrayList<String> getScreeningPhaseName(WebDriver driver){
      return screeningView.getScreening_phasesNames(driver);
    }


    public static ArrayList<String> getAllAssignmentsPapers(RelisUser user_screening){

        ScreeningView.showMyAssignment(user_screening.getDriver());
        ArrayList<String> data = new ArrayList<>();
         Utility.getPapersKeyFromDOMFromId(user_screening.getDriver(), data);
         return data;

    }

    public static ArrayList<String> getUserPendingAssignments(RelisUser user){
        ScreeningView.showMyPendingAssignmentsPage(user.getDriver());
        ArrayList<String> data = new ArrayList<>();
        Utility.getPapersKeyFromDOMFromId(user.getDriver(), data);
        return data;

    }

    public static ArrayList<String> getUserScreenedAssignements(RelisUser user){

        ScreeningView.showAllScreenedAssignmentsPage(user.getDriver());
        ArrayList<String> data = new ArrayList<>();
        Utility.getPapersKeyFromDOMFromId(user.getDriver(), data);
        return data;
    }

    public static ArrayList<String> getAllScreenedAssignments(RelisUser user){
        ScreeningView.showAllScreenedAssignmentsPage(user.getDriver());
        ArrayList<String> data = new ArrayList<>();
        Utility.getPapersKeyFromDOMFromId(user.getDriver(), data);
        return data;

    }
    public static ArrayList<Paper> getUserPapersAssignments(ScreeningPhaseWork user){
        ScreeningView.showMyPendingAssignmentsPage(user.getParticipant().getDriver());
        System.out.println("Defbut de getting assignments @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@");
        ArrayList<String> papersKeys= new ArrayList<>();

        Utility.getPapersKeyFromDOMFromId(user.getParticipant().getDriver(),papersKeys);
        System.out.println("We got " + papersKeys.size() +" Papers");
        ArrayList<Paper> p = new ArrayList<>();
        papersKeys.forEach(
                paperKey ->{
                    p.add((Paper) user.getPhase().getPaperByKey(paperKey).clone());
                }
        );
        return p;
    }



    /**
     * this method assigns all the reviewers for a screening phase
     * @param driver the web driver
     * @param phase the current screening phase
     */
    public void assignReviewers(WebDriver driver, ScreeningPhase phase){

      ArrayList<RelisUser> users = ScreeningView.assign_papers(driver,phase.getNumberOfUsers());
      users.forEach(p -> {

        ScreeningPhaseWork phaseWork = new ScreeningPhaseWork();
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

  /**
   * this function make ready for the screening phase 
   * like assigning reviewers, retrieve all the papers that 
   * considered in a screening phase
   * @param driver the web driver
   * @param phase a screening phase object
   */
  public void makeReadyForScreeningpPhase(WebDriver driver, ScreeningPhase phase){


      if(phase.getParticipantNumbers() == 0){
          assignReviewers(driver,phase);
      }
      if(phase.getPapersLength() == 0)
          phase.setUpPhase(driver);
  }


  public ScreeningPhase getCurrentScreeningPhase(WebDriver driver, Screening screening){
      if(screening == null) return null;

      String phaseName = openCurrentScreeningPhase(driver);
      return screening.getScreeningphaseByName(phaseName);
  }




}
