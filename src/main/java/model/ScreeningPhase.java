package model;

import controller.ConnexionController;
import controller.ProjectController;
import controller.ScreeningPhaseController;
import databases.PapersDataBase;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.openqa.selenium.WebDriver;
import utils.*;

import java.nio.file.Watchable;
import java.util.ArrayList;
import java.util.Optional;
import java.util.Random;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ScreeningPhase {


    private ScreeningDecisionMaker decisionMaker = new ScreeningDecisionMaker();
    private String name;
    private static ArrayList<PhaseWork> participants = new ArrayList<>();

    private int excludedPapeCount;
    private int includedPaperCout;
    private int paperInConflictCount;

    private ArrayList<Paper> papers = new ArrayList<>();

  public ScreeningPhase(String name) {
    this.name = name;
  }

  public void setUpPhase(WebDriver driver){

    try{
      papers = ProjectController.getAllPapers(driver);
      //
      decisionMaker.setUpperBoundPaperLength(papers.size());
      decisionMaker.makeDecision();
      papers = decisionMaker.applyDecisionForPapers(papers);
      includedPaperCout = decisionMaker.getINCLUDED();
      excludedPapeCount = decisionMaker.getEXCLUDED();
      paperInConflictCount = decisionMaker.getIN_CONFLICT();
    } catch (Exception e ){

      e.printStackTrace();
    }

  }
  public static void setParticipants(ArrayList<PhaseWork> assigned_reviewers) {
    participants = assigned_reviewers;
  }




    private Paper getPaper(Paper p){

        return papers.stream()
                        .filter(p::equals)
                        .findFirst()
                .orElse(null);
    }




//    private void showTableItem(WebElement tr){
//        List<WebElement> elements= tr.findElements(By.tagName("td"));
//        elements.forEach(p-> System.out.print(p.getText() +" "));
//        System.out.println();
//
//    }





    public Criteria getPaperExclusionCriteria( Paper paper){

        return Utility.getPaperByKey(papers,paper.getKey()).getCriteria();
    }

    public void printInfo(){
    int x= 0,y=0,z=0;


        for (int i = 0; i < papers.size(); i++) {

            x += papers.get(i).getInclude_count();
            y += papers.get(i).getExclude_count();
            z += papers.get(i).getConflict_count();
        }
        System.out.println("[ General Statistics");
        System.out.print("include paper=" + x);
        System.out.print(", exclude paper=" + y) ;
        System.out.println(", in conflict=" + z+" ]");

    }


    public Paper getPaperByKey(String key) {

        return Utility.getPaperByKey(papers,key);
    }





    public static int getCriteriaStatitics(Criteria c1){

      return participants.stream()
        .mapToInt(user-> user.getCriteriaCount(c1)).sum();

    }


    public void addPhaseWorkingUser(PhaseWork work){

      participants.add(work);
    }
    public void startScreeningPhaseForUsers(){

    participants.forEach(PhaseWork::finishScreeningPhase);
    }


    public String toString(){

      String papers_str = "[ ";
      for (Paper p : papers)
        papers_str += p+", \n";

      return "[ PhaseName :=> "+ name+" ,\n" +
              "paper => [ " + papers_str+ " ],\n"+
            "Included Paper => " + includedPaperCout+" ,\n"
        + "Excluded Paper => " + excludedPapeCount +" ,\n"+
        "In concflict paper => " + paperInConflictCount+"\n]";
    }

    public void expandChanges(){

      papers.forEach(Paper::notifyChange);
    }

  public void addPaper(Paper paper) {
    papers.add(paper);
  }

  public void showInfo() {

    participants.forEach(System.out::println);
  }


  public int getPapersLength(){return papers.size();}


  public void startThisPhaseScreening(){

    participants.parallelStream().forEach( user->{
        user.setUp();
        user.finishScreeningPhase();


    });

    papers.forEach(Paper::notifyChange);
    participants.forEach(PhaseWork::printInfo);
  }

  public PhaseWork getPhaseWorkByUserFullName(String userFullName) {

      return participants.stream()
      .filter(user -> user.getParticipant().getFull_name().equals(userFullName))
      .findFirst().orElse(null);
  }


  public String getDataByFormat( ){

    return ""+includedPaperCout+","+excludedPapeCount+","+ paperInConflictCount
      +",0,0,"+ papers.size();

  }
  public boolean correctResultOfScreeningPhase(String result){

    boolean correct = true;
    String[] data = result.split("\n");
    String globalResult = data[0];
    System.out.println(getDataByFormat()+" \n VS \n" + globalResult);
    if(!globalResult.equals(getDataByFormat()))
      return false;
    for (int i = 1; i < data.length; i++) {

      String[] resultCounters = data[i].split(",");
      PhaseWork user_state = getPhaseWorkByUserFullName(resultCounters[0]);

      assert user_state != null;
      System.out.println("phase work exist");
      System.out.println(data[i] +"\n VS \n" + user_state.getUsersResultOfScreeing()+"" +
        "\n*****************************************************");
      if(!user_state.getUsersResultOfScreeing().equals(data[i])) correct= false;

    }

    return correct;
  }


  public void resolvePapersConflicts(){

    papers.stream()
      .filter(p-> p.getDecision() == PaperDecision.IN_CONFLICT)
      .forEach(
        Paper::resolveConflict
      );
  }
}
