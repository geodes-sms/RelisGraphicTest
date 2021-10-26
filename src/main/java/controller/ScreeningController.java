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


  public void openScreeningPhaseByName(WebDriver driver, String name){

    views.openScreeningPhaseByName(driver,name);
  }

  public String openNextPhase(WebDriver driver){

    return views.openNextScreeningPhase(driver);


  }


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
  public void startTheCurrentPhase(WebDriver driver, Screening screening){

    ScreeningPhase phase = phaseController.getCurrentScreeningPhase(driver,screening);
    phaseController.makeReadyForScreeningpPhase(driver, phase);
    phase.startThisPhaseScreening();
  }

  public ScreeningPhase getCurrentScreeningPhase(WebDriver driver, Screening screening){
    return phaseController.getCurrentScreeningPhase(driver,screening);
  }


  public void resolveConflict(WebDriver driver,ScreeningPhase phase){

    phaseController.resolveConflict(driver,phase);
  }




}
