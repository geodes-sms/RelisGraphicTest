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



  private  ArrayList<ScreeningPhase> phases =  new ArrayList<>();


  public  void addPhase(ScreeningPhase phase ){
    phases.add(phase);
  }

  public  void setScreeningPhaseName(ArrayList<String> names){

    names.forEach(name -> phases.add(new ScreeningPhase(name)));
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






}
