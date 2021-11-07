package model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.openqa.selenium.WebDriver;

import java.util.ArrayList;


@AllArgsConstructor
@NoArgsConstructor
@Data
public class Screening {


  private static  Screening single_instance=null;
  private  int reviewers_per_paper = 1;
  private  String conflict_type;

  private String conflit_resolution_by;

  private ArrayList<Criteria> criteria_list = new ArrayList<>();

  private ArrayList<String> sources = new ArrayList<>(), strategies = new ArrayList<>();

  private  ArrayList<ScreeningPhase> phases =  new ArrayList<>();

  private String validationPercentage;
  private String validation_type;




  public  void addPhase(ScreeningPhase phase ){
    phases.add(phase);
  }

  public  void setScreeningPhaseName(ArrayList<String> names){

   for (String name : names){

     ScreeningPhase phase = new ScreeningPhase(name);
     phases.add(phase);
   }
  }
  public  ScreeningPhase getphaseByName(String phase_name){

    for (ScreeningPhase phase : phases)
      if(phase.getName().equals(phase_name))
        return phase;
      return null;
  }


  public  void showScreeningPhase(){

    phases.forEach(System.out::println);
  }


  public  String getScreeningByIndex(int index){

    if(index < phases.size())
      return phases.get(index).getName();
    return "";
  }


  public  void startScreeningPhase(ScreeningPhase phase){

    phase.startScreeningPhaseForUsers();
  }


  public ScreeningPhase getScreeningphaseByName(String  name){

      return phases.stream()
        .filter(phase ->  phase.getName().equals(name))
        .findFirst()
        .orElse(null);
  }



  public void runPhaseSetUp(WebDriver driver){


    phases.forEach(p -> p.setUpPhase(driver));
  }

  public void addCriteria(Criteria c ){
    criteria_list.add(c);
  }

  public void addSource(String s){
    sources.add(s);
  }

  public void addStrategy(String strategy){
    strategies.add(strategy);
  }





}
