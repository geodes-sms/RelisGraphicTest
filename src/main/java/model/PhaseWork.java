package model;

import controller.ConnexionController;
import controller.ProjectController;
import controller.ScreeningPhaseController;
import databases.PapersDataBase;
import lombok.Data;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import utils.Initialiazer;
import utils.ScreeningUtils;
import utils.Utility;
import view.ScreeningView;

import java.util.ArrayList;
import java.util.Random;

@Data
public class PhaseWork  implements Observer {

  private ArrayList<Paper> pendingAssignments = new ArrayList<>();

  private RelisUser participant;

  private ScreeningPhase phase;

  private int included;
  private int excluded;
  private int conflict;


  public void setUp() {

    Initialiazer initialiazer = new Initialiazer();
    initialiazer.init();
    participant.setDriver(initialiazer.getWebDriver());
    ConnexionController connexion = new ConnexionController();
    connexion.connect(participant.getDriver(), participant);
    ProjectController projectManager = new ProjectController();

    projectManager.openProject(participant.getDriver(), "");
    pendingAssignments = ScreeningPhaseController.getUserPapersAssignments(this);


  }
  @Override
  public void update(Observable observable) {

    Paper p = (Paper) observable;
    updatePapersDecisionMetric(p);

  }

  private void updatePapersDecisionMetric(Paper p){


    Paper my_paper = getPaperByKey(p.getKey());
    assert  my_paper != null;
    my_paper.incrementConflictCount(1);
    if(my_paper.getLastDecision() == PaperDecision.INCLUDED){
      my_paper.incrementIncludeCount(1);
    } else{
      my_paper.incrementExcludeCount(1);
      my_paper.getCriteria().increment(1);
    }
  }

  public String getNextDecision(Paper paper, PaperDecision decision) throws CloneNotSupportedException {

    Paper p = Utility.getPaperByKey(pendingAssignments, paper.getKey());
    assert p != null;
    if (decision == PaperDecision.INCLUDED) {
      p.incrementIncludeCount(1);
      return "TRUE";

    } else if (decision == PaperDecision.EXCLUDED) {
      p.incrementExcludeCount(1);
      return p.getCriteria().getName();
    } else {
      p.setDecision(PaperDecision.IN_CONFLICT);
      paper.addObserver(this);


      PaperDecision lastDecision = paper.getLastDecision();
      if (lastDecision == PaperDecision.INCLUDED) {

        p.setCriteria((Criteria) PapersDataBase.getInstance().nextCriteriaValue().clone());
        p.setLastDecision(PaperDecision.EXCLUDED);
        paper.setLastDecision(PaperDecision.EXCLUDED);
        paper.setLast_decision_user(this);
        //System.out.println("EXCLUDE CASE " + p.getCriteria());


        return p.getCriteria().getName();

      } else if (lastDecision == PaperDecision.EXCLUDED) {

        PhaseWork userDecision = paper.getLast_decision_user();

        Criteria c = PapersDataBase.getInstance().getNextCriteriaFrom(userDecision.
          getPaperByKey(paper.getKey()).getCriteria());
        if (c == null) {

          paper.setLastDecision(PaperDecision.INCLUDED);
          paper.setLast_decision_user(this);

          p.setLastDecision(PaperDecision.INCLUDED);
          // paper.notifyChange();

          return "TRUE";
        } else {
          p.setCriteria((Criteria) c.clone());
          p.setLastDecision(PaperDecision.EXCLUDED);
          paper.setLastDecision(PaperDecision.EXCLUDED);
          paper.setLast_decision_user(this);
          //paper.notifyChange();
          return p.getCriteria().getName();
        }

      } else {

        boolean nextDecision = new Random().nextBoolean();
        if (nextDecision) {
          paper.setLastDecision(PaperDecision.INCLUDED);
          p.setLastDecision(PaperDecision.INCLUDED);
          paper.setLast_decision_user(this);
          return "TRUE";

        } else {

          Criteria c = PapersDataBase.getInstance().nextCriteriaValue();
          p.setCriteria((Criteria) c.clone());
          p.setLastDecision(PaperDecision.EXCLUDED);
          paper.setLastDecision(PaperDecision.EXCLUDED);
          paper.setLast_decision_user(this);
          return p.getCriteria().getName();

        }


      }


    }

  }


  public Paper getPaperByKey(String key) {
    return Utility.getPaperByKey(pendingAssignments, key);
  }


  public void printStatistics() {


    for (int i = 0; i < pendingAssignments.size(); i++) {


      Paper paper = pendingAssignments.get(i);
      if(paper.isExcluded()) excluded +=1;
      else if(paper.isIncluded()) included +=1;
      else conflict +=1;
    }
    System.out.println("[" + participant);
    System.out.print("include paper=" + included);
    System.out.print(", exclude paper=" + excluded);
    System.out.println(", in conflict=" + conflict + " ]");

  }


  public int getCriteriaCount(Criteria criteria) {

    return ScreeningPhaseController.CountExcludeByCriteria(pendingAssignments, criteria);
  }



  private String extrackPaperKey(String sentence) {

    sentence = sentence.substring(8);
    int pos = sentence.indexOf(" ");

    return sentence.substring(0, pos);

  }

  public void printInfo(){

    excluded=0;included=0;conflict=0;
    for (int i = 0; i < pendingAssignments.size(); i++) {


      Paper paper = pendingAssignments.get(i);
      if (paper.isExcluded()) excluded += 1;
      else if (paper.isIncluded()) included += 1;
      else {
        System.out.println(paper);
        conflict += 1;
        if(paper.getLastDecision() == PaperDecision.INCLUDED)
            included +=1;
        else excluded +=1;
      }
    }
    System.out.println("[ General Statistics");
    System.out.print("include paper=" + included);
    System.out.print(", exclude paper=" + excluded) ;
    System.out.println(", in conflict=" + conflict+" ]");
    System.out.println("********************************************88");

  }
  public void finishScreeningPhase() {

    ScreeningView.showScreeningPhasePage(participant.getDriver());
    int size = pendingAssignments.size();

    WebDriver driver = participant.getDriver();
    while (size-- > 0) {
      String key = driver.findElement(By.className(ScreeningUtils.CLASS_CURRENT_SCREENING_PAPER)).getText();

      String paperKey = extrackPaperKey(key);
      Paper paper = this.phase.getPaperByKey(paperKey);
      assert paper != null;


      String decision = null;
      try {
        decision = getNextDecision(paper, paper.getDecision());
      } catch (CloneNotSupportedException e) {
        e.printStackTrace();
      }
      if (decision.equals("TRUE")) {
        ScreeningView.includePaper(driver);
        //  System.out.println("paper:>" + paper +" ;{ INCLUDED}");
      } else {
        ScreeningView.excludePaper(driver, decision);
        // System.out.println("paper:>" + paper +" ;{ EXCLUDED}");
      }
    }


  }


  @Override
  public String toString(){

    return "PhaseWork[ user=" + participant+"\n]";
  }


  public String getUsersResultOfScreeing(){
    printInfo();
    return participant.getFull_name()+","+included+","+excluded+","+conflict;
  }

  public void closeDriver() {

    participant.getDriver().quit();
  }

  public void updateChanges() {

    pendingAssignments.stream()
            .filter(p-> p.getDecision() == PaperDecision.IN_CONFLICT)
            .forEach(
                    paper -> {
                      Paper updatedPaper = phase.getPaperByKey(paper.getKey());
                      if(updatedPaper.getDecision() != paper.getDecision()){

                        paper.setDecision(updatedPaper.getDecision());
                        if(paper.getDecision() == PaperDecision.EXCLUDED)
                          paper.setCriteria(updatedPaper.getCriteria());
                      } else if((updatedPaper.getDecision() == PaperDecision.EXCLUDED
                      && paper.getLastDecision() == PaperDecision.EXCLUDED))
                      {
                        paper.setCriteria(updatedPaper.getCriteria());
                      }

                    }
            );
  }
}
