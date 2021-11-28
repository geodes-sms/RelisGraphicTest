package view;
import model.*;
import org.apache.hc.client5.http.classic.ExecChain;
import org.openqa.selenium.*;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import utils.*;

import static utils.ScreeningUtils.*;
import static utils.ScreeningUtils.LK_OPEN_SCREENING_MENU;

public class ScreeningView {


    private static void  openHomeMenu(WebDriver driver){
        try {
            driver.findElement(By.linkText(LK_DASHBORD_LINK)).click();
        } catch (Exception e){
            driver.findElement(By.linkText(ProjectUtils.LK_CURRENT_PROJECT)).click();
        }
    }
    private static String open_current_screening_phase(WebDriver driver) {
        if(screening_options_menu_exist(driver)){

         openHomeMenu(driver);
          WebElement title = Views.findElementBy(driver,By.cssSelector(".x_title h3"));

          return title.getText().substring(title.getText().indexOf("- ")+2);
        }
        System.out.println("la premiere fois");
        ArrayList<WebElement>  phases = work_through_table(driver);

        WebElement next_phase = phases.stream()
          .filter(ph1 ->{
            List<WebElement> elementList = ph1.findElements(By.tagName("td"));
            return  !elementList.get(
              SCREENING_PHASE_PERCENT_COMPLETITION_INDEX
            ).getText().equals("100 %");
          }).findFirst().orElse(null);

          if(next_phase != null){

            List<WebElement> tds = next_phase.findElements(By.tagName("td"));
            String phaseName  = tds.get(0).getText();
            WebElement element = tds.get(4).findElement(By.cssSelector(CSS_GO_TO_PHASE));
            assert element != null;
            element.sendKeys(Keys.ENTER);

            return phaseName;
      }

          return "";

    }


    private static boolean  screening_options_menu_exist(WebDriver driver) {
        try{
         //   Utility.sleep(10);
            WebElement element =  driver.findElement(By.linkText(LK_OPEN_SCREENING_MENU));


            try{
                WebElement exist = element.findElement(
                        By.linkText(LK_SCREENING_MY_ASSIGMENTS_PAGE)
                );
            } catch ( Exception o){

                element.click();
            }
           //if(element != null) element.click();
            return true;
        }catch(Exception e) {

            return false;}
    }



    private static ArrayList<WebElement> work_through_table(WebDriver driver){

        driver.findElement(By.className(ProjectUtils.CLASS_HOME_PROJECT)).click();
        // select the table that contains the screening phaseas
        WebElement table = driver.findElement(By.className(CLASS_SCREENING_PHASES_TABLE));

        // get all the systematic review phases
        List<WebElement> phases = table.findElements(By.tagName("tr"));

        // we remove the first web element which is the table header
        phases.remove(0);

        return  (ArrayList<WebElement>) phases.stream()
                .filter(ScreeningView::takeScreenPhases)
                .collect(Collectors.toList());
    }

    public ArrayList<String> getScreening_phasesNames(WebDriver driver){

        ArrayList<String> phases = new ArrayList<>();
        work_through_table(driver).forEach( p->{
            String name =  p.findElements(By.tagName("td")).get(0).getText();
            phases.add(name);
        });
        return phases;
    }


    /**
     *
     * @param phase the project phase web element
     * @return true if it is a screening phase otherwise false
     */
    private static boolean takeScreenPhases( WebElement phase){

        boolean taken = false;
        List<WebElement> phaseElements= phase.findElements(By.tagName("td"));

        for (int j = 0; j < phaseElements.size(); j++)
            if(phaseElements.get(0).getText().startsWith(SCREEN_PHASE_START_WORD))
                taken =  true;

        return taken;
    }

    /**
     * open the assign user page
     * @param driver the web driver
     */
    private static void openAssignPaperPage(WebDriver driver){

        WebElement button = Views.findElementBy(driver, By.cssSelector(CSS_ASSIGN_PAPER_BUTTON));
        // we choose assign paper options
        button.sendKeys(Keys.ENTER);
    }

    public static ArrayList<RelisUser> assign_papers(WebDriver driver, int number){
        openAssignPaperPage(driver);

        WebElement rest_papers = Views.findElementBy(driver, By.cssSelector("#home b"));
        // we have already assigned
        if(rest_papers.getText().equals("Number of papers to assign :0")){
          return getReviewer(driver);
        }


        // we select some random users
        ArrayList<RelisUser> reviewers = Views.chooseUserforScreening(driver,number);
        // we submit the assaignment
        driver.findElement(By.className(CLASS_SUCCESS_BUTTON)).click();
        return reviewers;
    }




    /**
     * return all the reviewers for the current project
     * @param driver
     */
    public static ArrayList<RelisUser> getReviewer(WebDriver driver){

        System.out.println("Showing the progress statitis@@@@@@@@@@@@@@ ");
      //  Utility.sleep(5);
        // go to screening menu
        showProgressScreening(driver);
        //Utility.sleep(10);
        System.out.println("Dans le prgess page");
        return   Views.extractUsers(driver);

    }

    /**
     * show the screening process page
     * @param driver the web driver
     *
     */
    private static void showProgressScreening(WebDriver driver) {
       // open_current_screening_phase(driver);
        Views.openSuBMenuFrom(driver, LK_OPEN_SCREENING_MENU, LK_SCREENING_PROGRESS_PAGE);
    }

    /**
     * show the 'my assignment page'
     * @param driver the web driver
     */
    public static void showMyAssignment(WebDriver driver){
        try{

            Views.openSuBMenuFrom(driver, LK_OPEN_SCREENING_MENU, LK_SCREENING_MY_ASSIGMENTS_PAGE);

        } catch ( Exception e ){
            Views.openSuBMenuFrom(driver, LK_OPEN_SCREENING_MENU, LK_SCREENING_ONLINE_MY_ASSIGNMENTS_PAGE);
        }

    }

    /**
     * show the 'all assignments page'
     * @param driver the web driver
     */
    public static void  showAllAssigmentsPage(WebDriver driver){
//        open_current_screening_phase(driver);
        Views.openSuBMenuFrom(driver, LK_OPEN_SCREENING_MENU, LK_ALL_ASSIGNMENTS);

    }

    /**
     * show the 'my pending' pages
     * @param driver
     */
    public static void showMyPendingAssignmentsPage(WebDriver driver){
        System.out.println("Dans le wait de pending assignment");

        Views.openSuBMenuFrom(driver, LK_OPEN_SCREENING_MENU, LK_MY_PENDING_PAPERS);

    }

    /**
     * show 'the screen process page'
     * @param driver the web driver
     */
    public static void showScreeningPhasePage(WebDriver driver){
//        open_current_screening_phase(driver);
        Views.openSuBMenuFrom(driver, LK_OPEN_SCREENING_MENU, LK_SCREEN_PAPERS);
    }

    /**
     * show the all screened assigments page
     * @param driver the web driver
     */
    public static void showAllScreenedAssignmentsPage(WebDriver driver) {
        open_current_screening_phase(driver);
        Views.openSuBMenuFrom(driver, LK_OPEN_SCREENING_MENU, LK_ALL_SCREENED_PAPERS);
    }

    /**
     * show the 'my screened assignments page'
     * @param driver the web driver
     */
    public static  void  showMyScreenedAssignmentsPage(WebDriver driver){
        open_current_screening_phase(driver);
        Views.openSuBMenuFrom(driver, LK_OPEN_SCREENING_MENU, LK_MY_SCREENED_PAPERS);
    }




    public String openScreeningPage(WebDriver driver){
        String name = open_current_screening_phase(driver);

        return name;
    }

  /**
   * open a screening phase by his name
   * @param driver the web driver
   * @param phaseName the screening phase name
   */
    public  static void openScreeningPhaseByName(WebDriver driver, String phaseName){

     Views.findElementBy(driver,By.linkText(ProjectUtils.LK_CURRENT_PROJECT)).click();
      ArrayList<WebElement>  phases = work_through_table(driver);

      for (WebElement phase : phases){

        List<WebElement> tds  = phase.findElements(By.tagName("td"));
        String current_phase_name = tds.get(0).getText();
          System.out.println("PHASES NAME => "+ current_phase_name );
        if(current_phase_name.equals( phaseName)){
          RelisUser connectedUser = Utility.getCurrentConnectedUser(driver);
          System.out.println("USER GROUP TYPE=" + connectedUser.getUser_usergroup());

          WebElement element;
          int last = tds.size()-1;
          element  = tds.get(last).findElement(By.cssSelector(CSS_GO_TO_PHASE));
          assert element != null;
          element.sendKeys(Keys.ENTER);
//          // open the screening phase
//          ((JavascriptExecutor)driver).executeScript("arguments[0].click();",
//            element.findElement(By.className(CLASS_GOTO_PHASE)));
            break;
        }

      }

    }

    public String  openNextScreeningPhase(WebDriver driver){

      return open_current_screening_phase(driver);
    }


    private static void getIncludePaperButton(WebDriver driver){

        WebElement div = driver.findElement(By.className(CLASS_SCREENING_DECISION));
        List<WebElement> buttons = div.findElements(By.tagName("button"));
        Utility.chooseWebElementAndClick(buttons, INCLUDE_PAPER_DECISION);
    }

    private static void getExcludePaperButton(WebDriver driver){
        WebElement div = driver.findElement(By.className(CLASS_SCREENING_DECISION));
        List<WebElement> buttons = div.findElements(By.tagName("button"));
        Utility.chooseWebElementAndClick(buttons, EXCLUDE_PAPER_DECISION);
    }

    /**
     * function for saving the current decision
     * for the current paper for screening
     * and pass to the next paper
     * @param driver
     */
    public static void  saveAndGetNextPaperForScreening(WebDriver driver){
        driver.findElement(By.cssSelector(CSS_BUTTON_SAVE_AND_NEXT_PAPER)).click();

    }

    /**
     * the method is used by the 'getExcludePaper()' function
     * to choose the correct criteria for the exclusion
     * @param driver the current web driver
     * @param criteria the criteria for the exlcusion
     */
    private static void chooseCriteria(WebDriver driver, String criteria){

        WebElement criteria_menu = driver.findElement(By.id(ID_CRITERIA_OPTIONS));
        criteria_menu.click();
        List<WebElement> options = criteria_menu.findElements(By.tagName("option"));

        Utility.chooseWebElementAndClick(options,criteria);

    }


  /**
   * choose a criteria to resolve a conflict
   * @param driver the web driver
   * @param critere the criteria we wanna choose
   */
  public static void chooseConflictCriteria(WebDriver driver, String critere){


    driver.findElement(By.className(CLASS_CRITERIA_OPTIONS_RESOLVING_CONFLICT)).click();
      WebElement element = driver.findElement(By.cssSelector(".select2-results__options"));
      List<WebElement> options = element.findElements(By.tagName("li"));
      for (WebElement li : options){
          if(li.getText().equals(critere)) {
            Views.performClickWithJsExec(driver,li);
            break;
          }
      }

    }

    /**
     * this method is for choosing
     * to exclude a paper and pass
     * to the next screening paper
     * @param driver the web driver
     * @param criteria the criteria of exclusion
     */
    public static void excludePaper(WebDriver driver,String criteria){
        getExcludePaperButton(driver);
        chooseCriteria(driver,criteria);
        saveAndGetNextPaperForScreening(driver);
    }

    public static void doExclude(WebDriver driver, String c){
      getExcludePaperButton(driver);
      chooseConflictCriteria(driver,c);
      saveAndGetNextPaperForScreening(driver);
    }

    /**
     *
     * this function will click on the include button
     * for a screening phase and pass to the next paper
     * for screening
     * @param driver the web driver
     */
    public static void includePaper(WebDriver driver){
        // click the 'include' button
        getIncludePaperButton(driver);
        // pass to the next paper for screening
        saveAndGetNextPaperForScreening(driver);
    }


    public static void resolve(WebDriver driver, ScreeningPhase phase){

        int n = phase.getPaperInConflictCount();
        for (int i = 0; i < n; i++) {
            resolveConflict(driver, phase);
        }


    }

    public static  void resolveConflict(WebDriver driver, ScreeningPhase phase){


     // driver.findElement(By.linkText(ScreeningUtils.LK_DASHBORD_LINK)).click();
      driver.findElement(By.linkText(LK_PAPERS_IN_THIS_PHASE)).click();
      ;
     // driver.findElement(By.linkText(ScreeningUtils.LK_IN_CONFLICTS_PAPERS)).click();
      new WebDriverWait(driver,2).until(
        ExpectedConditions.presenceOfElementLocated(By.linkText(LK_IN_CONFLICTS_PAPERS))
      ).click();
      WebElement table = driver.findElement(By.id(ProjectUtils.ID_PROJECT_TABLE_USERS));
      List<WebElement> inputs = table.findElements(By.tagName("tr"));
      inputs.remove(0); // remove the header
      List<WebElement> tds = inputs.get(0).findElements(By.tagName("td"));
      String key = tds.get(1).getText();
      tds.get(2).findElement(By.tagName("a")).click();
      resolveCOnflict(driver,key, phase);

    }
    private static void resolveCOnflict(WebDriver driver,String key, ScreeningPhase phase){

      List<WebElement> table= driver.findElements(By.className(CLASS_SCREENING_PHASES_TABLE));
      List<WebElement> inputs = table.get(1).findElements(By.tagName("tr"));
      inputs.remove(0);
      inputs.remove(0);
      List<WebElement> users_decision = new ArrayList<>();
      AtomicInteger index= new AtomicInteger(0);
      inputs.forEach(u-> {
          if(index.get() %2 == 0)
            users_decision.add(inputs.get(index.get()));
          index.getAndIncrement();
      });
     changeConflictDecision(users_decision,driver,key, phase);
    }

    private static void changeConflictDecision(List<WebElement> decisions, WebDriver driver,String key,
                                               ScreeningPhase phase){


        try {
          for (WebElement user : decisions) {
          List<WebElement> tds = user.findElements(By.tagName("td"));
          Paper paper = phase.getPaperByKey(key);
          // we change the user decision in order to resolve the conflict
          if (!(tds.get(3).getText().equals(paper.getDecision().toString()))
            || (paper.getDecision() != PaperDecision.INCLUDED && !tds.get(4).getText().equals(paper.getCriteria().getName()))) {
            tds.get(tds.size()-1).findElement(By.cssSelector(CSS_UPDATE_DECISION)).click();

            if(paper.getDecision() == PaperDecision.INCLUDED)
              includePaper(driver);
            else doExclude(driver, paper.getCriteria().getName());
            break;
          }
        }
        } catch (Exception e){
            System.out.println("Erreur survenue dans la fonction changeConflictDecision DETAILS \n");
            e.printStackTrace();
            System.out.println("Fin de details\n");
        }


    }


  public static PaperDecision getDecisionFor(ArrayList<Paper> papers,Paper paper) {

    Optional<Paper> p = Optional.of(papers.stream()
      .filter(p1 -> p1.equals(paper))
      .findFirst()
      .get());

    return p.get().getDecision();


  }

  /**
   *  retrieve the screening result
   *  like the % of included paper, % of excluded papers
   *  and the papers in conflicts number
   * @param driver the web driver
   * @return a string in form of included,excluded,conflicts
   */

  private static String getGlobalScreeningResult(WebElement driver){
      WebElement elementList = driver.findElement(By.tagName("table"));
      List<WebElement> tableTr = elementList.findElements(By.tagName("tr"));
      tableTr.remove(0); // remove the header
      String result ="";
      int index = 0;
      for( WebElement tr : tableTr){

        List<WebElement> tds = tr.findElements(By.tagName("td"));
        result +=  tds.get(1).getText() + ((index++ == tableTr.size()-1 )? "" :",");
      }

      return result+"\n";

  }

    /**
     * this method retrieve every users data from the 'statistic' page
     * @param driver the web driver
     * @return the users data result in a string
     */
  private static String getUsersResultOfScreeing(WebElement driver) {


    WebElement elementList = driver.findElement(By.tagName("table"));
    List<WebElement> tableTr = elementList.findElements(By.tagName("tr"));
    tableTr.remove(0); // remove the header
    String result ="";
    for( WebElement tr : tableTr){

      List<WebElement> tds = tr.findElements(By.tagName("td"));
      String userFullName = tds.get(0).getText();
      String includedPaperCount = tds.get(1).getText();
      String excludedPapeCount = tds.get(2).getText();
      String paperInConflictCount = tds.get(3).getText();

      result += userFullName+","+includedPaperCount+","+excludedPapeCount+","+paperInConflictCount+"\n";
    }

    return result;



  }


    /**
     *
     * this function extract the data from the 'statistic page'
     * like the global included papers numbers, excluded papers number
     * ond also every user who participated in the screening phase
     *
     * @param driver the current web driver
     * @return the string of all the data separed by '\n'
     */
  public static String extractScreeningResult(WebDriver driver){

      driver.findElement(By.linkText(LK_STATISTICS_BUTTON)).click();
      List<WebElement> elements = driver.findElements(By.cssSelector(".row"));
      List<WebElement> result_data = elements.get(1).findElements(By.className("col-md-6"));

      String data = getGlobalScreeningResult(result_data.get(0));
      data += getUsersResultOfScreeing(result_data.get(2));
      return data;

  }


    /**
     * this function will enable the validation for the screening phase
     * @param driver the web driver
     */
  public static void enable_validation(WebDriver driver){

      Views.enable_validation(driver, LABEL_SCREENING, LABEL_SCREENING_ENABLE_VALIDATION);
  }

    /**
     * this function will desable the validation for the screening phase
     * @param driver the web driver
     */
    public static void  desable_validation(WebDriver driver){

        Views.desable_validation(driver, LABEL_SCREENING, LABEL_SCREENING_ENABLE_VALIDATION);
    }

}
