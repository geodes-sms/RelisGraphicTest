package model;

import controller.ProjectController;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import model.user_work.ScreeningPhaseWork;
import org.openqa.selenium.WebDriver;
import utils.*;

import java.util.ArrayList;
import java.util.stream.Stream;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ScreeningPhase {


    private ScreeningDecisionMaker decisionMaker = new ScreeningDecisionMaker();
    private String name;
    private String description;
    private String fields;

    private String projectName;
    private  ArrayList<ScreeningPhaseWork> participants = new ArrayList<>();

    private int excludedPapeCount;
    private int includedPaperCout;
    private int paperInConflictCount;

    private int numberOfUsers = 2;
    private ArrayList<Paper> papers = new ArrayList<>();

  public ScreeningPhase(String name) {
    this.name = name;
  }

  public void setUpPhase(WebDriver driver){

    try{
      papers =  new ArrayList<>();
      Utility.getAllPaperFromTable_id(driver,papers);
      //
      decisionMaker.setUpperBoundPaperLength(papers.size());
      decisionMaker.makeDecision(numberOfUsers);
      papers = decisionMaker.applyDecisionForPapers(papers);
      includedPaperCout = decisionMaker.getINCLUDED();
      excludedPapeCount = decisionMaker.getEXCLUDED();
      paperInConflictCount = decisionMaker.getIN_CONFLICT();
      System.out.println("Finished set up of the screening phase !!! PAPERS LENGTH=" + papers.size());

    } catch (Exception e ){

      e.printStackTrace();
    }

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





    public  int getCriteriaStatitics(Criteria c1){

      return participants.stream()
        .mapToInt(user-> user.getCriteriaCount(c1)).sum();

    }


    public void addPhaseWorkingUser(ScreeningPhaseWork work){

      participants.add(work);
    }
    public void startScreeningPhaseForUsers(){

    participants.forEach(ScreeningPhaseWork::finishScreeningPhase);
    }


    public String toString(){

      String papers_str = "";
      for (Paper p : papers)
        papers_str += p+", \n";
      String users ="";
      for(ScreeningPhaseWork p : participants)
          users += p +"\n";

      return "[ PhaseName :=> "+ name+" ,\n" +
              "description=>" + description+
              "\n fiedls=>" + fields+
              "\npaper => [ " + papers_str+ " ],\n"+
            "Included Paper => " + includedPaperCout+" ,\n"
        + "Excluded Paper => " + excludedPapeCount +" ,\n"+
        "In concflict paper => " + paperInConflictCount+"\n" +
              "Participants => " + users +"\n]";
    }



  public void addPaper(Paper paper) {
    papers.add(paper);
  }

  public void showInfo() {

    participants.forEach(System.out::println);
  }


  public int getPapersLength(){return papers.size();}


  public void startThisPhaseScreening(){

      Stream<ScreeningPhaseWork> stream = (numberOfUsers == 1)?
              participants.stream(): participants.parallelStream();

        stream.forEach( user->{
        user.setUp();
        user.finishScreeningPhase();

    });



    if(numberOfUsers > 1){

        papers.forEach(Paper::notifyChange);
        participants.forEach(ScreeningPhaseWork::printInfo);
        participants.forEach(ScreeningPhaseWork::closeDriver);
    }

  }


  public ScreeningPhaseWork getPhaseWorkByUserFullName(String userFullName) {

      return participants.stream()
      .filter(user -> user.getParticipant().getFull_name().equals(userFullName))
      .findFirst().orElse(null);
  }


  public String getDataByFormat( ){
      int x= 0,y=0,z=0;


      for (Paper paper : papers) {

          if(paper.isIncluded())
              x +=1;
          else if (paper.isExcluded())
              y +=1;
          else z +=1;
      }

    return ""+x+","+y+","+ z +",0,0,"+ papers.size();

  }

    /**
     * this function will check if we have the
     * good statistics for the screening phase
     * like included paper, excluded paper, etc
     * @param result the dom statistics in string format
      * @return true if all the statistics are correct otherwise false
     */
  public boolean correctResultOfScreeningPhase(String result){
    // 12,12,32,0,0,98

    boolean correct = true;
    String[] data = result.split("\n");
    String globalResult = data[0];
    System.out.println(getDataByFormat()+" \n VS \n" + globalResult);
    if(!globalResult.equals(getDataByFormat()))
      return false;
    for (int i = 1; i < data.length; i++) {
        // john, 12,23,20
      String[] resultCounters = data[i].split(",");
      ScreeningPhaseWork user_state = getPhaseWorkByUserFullName(resultCounters[0]);

      assert user_state != null;
      System.out.println("phase work exist");
      System.out.println(data[i] +"\n VS \n" + user_state.getUsersResultOfScreeing()+"" +
        "\n*****************************************************");
      if(!user_state.getUsersResultOfScreeing().equals(data[i])) correct= false;

    }

    return correct;
  }


    /**
     * this function is resolving all the conflict
     * between papers
     */
  public void resolvePapersConflicts(){

    papers.stream()
      .filter(p-> p.getDecision() == PaperDecision.IN_CONFLICT)
      .forEach(
        Paper::resolveConflict
      );

      propageChanges();

  }

    /**
     * this method will checks if we have a conflict between
     * reviewers decision for a paper
     * after the screening phase it'll send
     * all the decision for a paper that
     * have more than one reviewer
     */
  private void propageChanges(){

      participants.forEach(
              ScreeningPhaseWork::updateChanges
      );
  }

    /**
     * @return the number of the reviewers for this screening phase
     */
  public int getParticipantNumbers(){
      return participants.size();
  }


    /***
     * close all the web browser for every reviewers
     */
    public void quitWebBrowser() {
      participants.forEach(p-> p.getParticipant().getDriver().quit());
    }


}
