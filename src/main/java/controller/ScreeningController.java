package controller;

import model.*;
import org.openqa.selenium.WebDriver;
import view.ScreeningView;

import java.util.ArrayList;

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
    phaseController.assignReviewers(driver,phase);
    return phase;

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




}
