package controller;

import model.*;
import org.openqa.selenium.WebDriver;
import utils.Utility;
import view.ScreeningView;

import java.util.ArrayList;

import static org.testng.Assert.assertTrue;

public class ScreeningController {

  private final ScreeningPhaseController phaseController = new ScreeningPhaseController();


  private static final ScreeningView views = new ScreeningView();

  /**
   * set the screening phase
   * @param driver the web driver
   */
  public void getScreeningPhaseData(WebDriver driver, Screening screening){
      screening.setScreeningPhaseName(phaseController.getScreeningPhaseName(driver));
      screening.showScreeningPhase();
      //openScreeningPhaseByName(driver, Screening.getScreeningByIndex(1));

  }

  /**
   * this method open a screening phase by his name
   * 
   * @param driver the web driver
   * @param name the screening phase name
   */
  public void openScreeningPhaseByName(WebDriver driver, String name){

    views.openScreeningPhaseByName(driver,name);
  }

  /**
   * this method open the next screening phase
   * and return his name as a string
   * @param driver
   * @return next screening phase title as string 
   */
  public String openNextPhase(WebDriver driver){

    return views.openNextScreeningPhase(driver);


  }


  /**
   * this method assign the reviewers for the next screening phase
   * @param driver the web driver
   * @param project the project object
   * @return an object of a ScreeningPhase which is the next phase
   */
  public ScreeningPhase assignReviewers(WebDriver driver,Project project){
    String current_phase_name = openNextPhase(driver);
    ScreeningPhase phase = project.getScreening().getScreeningphaseByName(current_phase_name);
    if(phase != null)
    phaseController.assignReviewers(driver,phase);
    return phase;

  }


  public void  doScreenPhase(WebDriver driver, ScreeningPhase phase){

    phaseController.makeReadyForScreeningpPhase(driver, phase);
    phase.startThisPhaseScreening();
    // now the screening phase is finish
    // get the screening result of the website
    String result = ScreeningView.extractScreeningResult(driver);
    // compare with the data that we have here
    boolean correct = phase.correctResultOfScreeningPhase(result);

    assertTrue(correct);
  }
  public String setUpCurrentPhase(WebDriver driver, Project project){

    String phaseName = openNextPhase(driver);
    ScreeningPhase phase = project.getScreening().getScreeningphaseByName(phaseName);
    if(phase != null)
      phase.setUpPhase(driver);
    return phaseName;
  }

  /**
   * this method start the next screening phase 
   * @param driver the web driver
   * @param project the current project
   */
  public void startTheCurrentPhase(WebDriver driver, Project project){

    Screening screening = project.getScreening();
    ScreeningPhase phase = phaseController.getCurrentScreeningPhase(driver,screening);
    phase.setProjectName(project.getProject_name());
    phaseController.makeReadyForScreeningpPhase(driver, phase);
    phase.startThisPhaseScreening();
  }

  public ScreeningPhase getCurrentScreeningPhase(WebDriver driver, Screening screening){
    return phaseController.getCurrentScreeningPhase(driver,screening);
  }


  /**
   * this method resolve all the conflict for the passed screening phase 
   * 
   * @param driver the web driver
   * @param phase the screening phase
   */
  public void resolveConflict(WebDriver driver,ScreeningPhase phase){

    phaseController.resolveConflict(driver,phase);
  }

  /**
   * this method will check all the papers are in connflicts
   * and it will resolve the conflict
   * @param driver the web driver
   * @param phase the screening phase
   * @return true if there's will be 0 conflicts after resolution
   */
  public boolean resolveIfConflicts(WebDriver driver, ScreeningPhase phase){
    // do we have more than one reviewer per paper?
    boolean resolved = true;
    if(phase.getParticipantNumbers() > 1){
      // if so we check out if we have conflicts
      // we'll resolve them
      resolveConflict(driver, phase);
      String result = ScreeningView.extractScreeningResult(driver);
      resolved = phase.correctResultOfScreeningPhase(result);

    }
    phase.quitWebBrowser();
    return  resolved;

  }





}
