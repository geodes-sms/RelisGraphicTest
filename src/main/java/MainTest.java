
import model.RelisUser;
import model.ScreeningDecisionMaker;

public class MainTest {





  static RelisUser getMockUser(){

      RelisUser user = new RelisUser();
      user.setUser_email("mahamatyoussoufi@yahoo.com");
      user.setUsername("john2020");
      user.setPassword("mondaymyday");
      user.setFull_name("John Teller");
      return user;
  }



  public static void main(String[] args) {


      ScreeningDecisionMaker decision = new ScreeningDecisionMaker();
      decision.setUpperBoundPaperLength(98);
      decision.makeDecision();


  }








}
