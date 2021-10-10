package view;

import model.RelisUser;
import org.openqa.selenium.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import utils.*;

public class ScreeningView {


    private static void open_current_screening_phase(WebDriver driver) {
        if(screening_options_menu_exist(driver))
            return;
        ArrayList<WebElement>  phases = work_through_table(driver);
        WebElement ph1 = phases.get(0);

        List<WebElement> elementList = ph1.findElements(By.tagName("td"));

        System.out.println("phase 1 => ["  + ph1.getText() +"]");
        RelisUser connectedUser = Utility.getCurrentConnectedUser(driver);
        WebElement element = !(connectedUser.getUser_usergroup().equals("1"))?
                Utility.chooseWebElement(elementList, ScreeningUtils.LK_GO_TO_PHASE):
         Utility.chooseWebElement(elementList, ScreeningUtils.SCREENING_MANAGER_ELEMENT);
        assert element != null;

        // open the screening phase
        ((JavascriptExecutor)driver).executeScript("arguments[0].click();",
                element.findElement(By.className(ScreeningUtils.CLASS_GOTO_PHASE)));

        driver.findElement(By.linkText(ScreeningUtils.LK_OPEN_SCREENING_MENU)).click();

    }


    private static boolean  screening_options_menu_exist(WebDriver driver) {
        try{
            WebElement element =  driver.findElement(By.linkText(ScreeningUtils.LK_OPEN_SCREENING_MENU));

            try{
                WebElement exist = element.findElement(
                        By.linkText(ScreeningUtils.LK_SCREENING_MY_ASSIGMENTS_PAGE)
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
        WebElement table = driver.findElement(By.className(ScreeningUtils.CLASS_SCREENING_PHASES_TABLE));

        // get all the systematic review phases
        List<WebElement> phases = table.findElements(By.tagName("tr"));

        // we remove the first web element which is the table header
        phases.remove(0);

        return  (ArrayList<WebElement>) phases.stream()
                .filter(ScreeningView::takeScreenPhases)
                .collect(Collectors.toList());
    }

    public ArrayList<WebElement> getScreening_phases(WebDriver driver){

        return work_through_table(driver);
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
            if(phaseElements.get(0).getText().startsWith(ScreeningUtils.SCREEN_PHASE_START_WORD))
                taken =  true;

        return taken;
    }

    private static void openAssignPaperPage(WebDriver driver){
        open_current_screening_phase(driver);

        WebElement button = driver.findElement(By.cssSelector(ScreeningUtils.CSS_ASSIGN_PAPER_BUTTON));
        // we choose assign paper options
        button.sendKeys(Keys.ENTER);
    }

    public static ArrayList<RelisUser> assign_papers(WebDriver driver){
        openAssignPaperPage(driver);
        List<WebElement> users = driver.findElements(By.className("form-group"));
        users.remove( users.size()-1);
        users.remove(users.size()-1);
        // we select some random users
        ArrayList<RelisUser> reviewers = chooseUserforScreening(users);
        // we submit the assaignment
        driver.findElement(By.className(ScreeningUtils.CLASS_SUCCESS_BUTTON)).click();
        return reviewers;
    }


    private  static ArrayList<RelisUser> chooseUserforScreening(List<WebElement> users){

        Random random = new Random();
        ArrayList<RelisUser> reviewers = new ArrayList<>();
        if(users.size() < 4){
            users.forEach( elem ->
                    {
                        RelisUser user  = Utility.getUserByFullName(elem.getText());
                        reviewers.add(user);
                        elem.findElement(By.tagName("span")).click();
                    }
            );

        } else{

            do{
                // choose a random user
                int i = random.nextInt(0,users.size());
                WebElement userWebELment = users.remove(i);
                RelisUser user  = Utility.getUserByFullName(userWebELment.getText());
                reviewers.add(user);
                userWebELment.findElement(By.tagName("span")).click();
            } while (reviewers.size() < 4);
        }

        return reviewers;
    }

    /**
     * return all the reviewers for the current project
     * @param driver
     */
    public static ArrayList<RelisUser> getReviewer(WebDriver driver){
        open_current_screening_phase(driver);
        // go to screening menu
        showProgressScreening(driver);
        WebElement element =  driver.findElement(By.className("x_content"));

        return   setReviewer(element);

    }

    /**
     * show the screening process page
     * @param driver the web driver
     *
     */
    private static void showProgressScreening(WebDriver driver) {
        open_current_screening_phase(driver);
        //driver.findElement(By.linkText(ScreeningUtils.LK_OPEN_SCREENING_MENU)).click();
        new WebDriverWait(driver,2).until(ExpectedConditions.presenceOfElementLocated(
                (By.linkText(ScreeningUtils.LK_SCREENING_PROGRESS_PAGE))
        )).sendKeys(Keys.ENTER);
    }

    /**
     * show the 'my assignment page'
     * @param driver the web driver
     */
    public static void showMyAssignment(WebDriver driver){

        open_current_screening_phase(driver);
        //driver.findElement(By.linkText(ScreeningUtils.LK_OPEN_SCREENING_MENU)).click();
        new WebDriverWait(driver,2).until(ExpectedConditions.presenceOfElementLocated(
                (By.linkText(ScreeningUtils.LK_SCREENING_MY_ASSIGMENTS_PAGE))
        )).sendKeys(Keys.ENTER);

    }

    /**
     * show the 'all assignments page'
     * @param driver the web driver
     */
    public static void  showAllAssigmentsPage(WebDriver driver){
        open_current_screening_phase(driver);
        new WebDriverWait(driver,2).until(ExpectedConditions.presenceOfElementLocated(
                (By.linkText(ScreeningUtils.LK_ALL_ASSIGNMENTS))
        )).sendKeys(Keys.ENTER);

    }

    /**
     * show the 'my pending' pages
     * @param driver
     */
    public static void showMyPendingAssignmentsPage(WebDriver driver){

        open_current_screening_phase(driver);
        //driver.findElement(By.linkText(ScreeningUtils.LK_OPEN_SCREENING_MENU)).click();
        new WebDriverWait(driver,2).until(ExpectedConditions.presenceOfElementLocated(
                (By.linkText(ScreeningUtils.LK_MY_PENDING_PAPERS))
        )).sendKeys(Keys.ENTER);

    }

    /**
     * show 'the screen process page'
     * @param driver the web driver
     */
    public static void showScreeningPhasePage(WebDriver driver){
        open_current_screening_phase(driver);
        //driver.findElement(By.linkText(ScreeningUtils.LK_OPEN_SCREENING_MENU)).click();
        new WebDriverWait(driver,2).until(ExpectedConditions.presenceOfElementLocated(
                (By.linkText(ScreeningUtils.LK_SCREEN_PAPERS))
        )).sendKeys(Keys.ENTER);
    }

    /**
     * show the all screened assigments page
     * @param driver the web driver
     */
    public static void showAllScreenedAssignmentsPage(WebDriver driver) {
        open_current_screening_phase(driver);
       // driver.findElement(By.linkText(ScreeningUtils.LK_OPEN_SCREENING_MENU)).click();
        new WebDriverWait(driver,2).until(ExpectedConditions.presenceOfElementLocated(
                (By.linkText(ScreeningUtils.LK_ALL_SCREENED_PAPERS))
        )).sendKeys(Keys.ENTER);
    }

    /**
     * show the 'my screened assignments page'
     * @param driver the web driver
     */
    public static  void  showMyScreenedAssignmentsPage(WebDriver driver){
        open_current_screening_phase(driver);
        driver.findElement(By.linkText(ScreeningUtils.LK_OPEN_SCREENING_MENU)).click();
       // new RuntimeException(.addSuppressed(4));

        new WebDriverWait(driver,2).until(ExpectedConditions.presenceOfElementLocated(
                (By.linkText(ScreeningUtils.LK_MY_SCREENED_PAPERS))
        )).sendKeys(Keys.ENTER);
    }


    private static ArrayList<RelisUser> setReviewer(WebElement element){
        ArrayList<String> userName= new ArrayList<>(Arrays.asList(element.getText().split("\n")));
        userName.remove(0);
        userName.remove(userName.size()-1);
        ArrayList<RelisUser> reviewer = new ArrayList<>();
        userName.stream()
                .map(p -> {
                    int i = p.indexOf(":");
                    return p.substring(0,i);
                })
                .forEach(full_name
                        -> reviewer.add(Utility.getUserByFullName(full_name))
                );
        return reviewer;
    }

    public void openScreeningPage(WebDriver driver){
        open_current_screening_phase(driver);
        ScreeningView.showScreeningPhasePage(driver);

    }


    private void getIncludePaperButton(WebDriver driver){

        WebElement div = driver.findElement(By.className(ScreeningUtils.CLASS_SCREENING_DECISION));
        List<WebElement> buttons = div.findElements(By.tagName("button"));
        Utility.chooseWebElementAndClick(buttons, ScreeningUtils.INCLUDE_PAPER_DECISION);
    }

    private void getExcludePaperButton(WebDriver driver){
        WebElement div = driver.findElement(By.className(ScreeningUtils.CLASS_SCREENING_DECISION));
        List<WebElement> buttons = div.findElements(By.tagName("button"));
        Utility.chooseWebElementAndClick(buttons, ScreeningUtils.EXCLUDE_PAPER_DECISION);
    }

    /**
     * function for saving the current decision
     * for the current paper for screening
     * and pass to the next paper
     * @param driver
     */
    public void  saveAndGetNextPaperForScreening(WebDriver driver){
        driver.findElement(By.cssSelector(ScreeningUtils.CSS_BUTTON_SAVE_AND_NEXT_PAPER)).click();

    }

    /**
     * the method is used by the 'getExcludePaper()' function
     * to choose the correct criteria for the exclusion
     * @param driver the current web driver
     * @param criteria the criteria for the exlcusion
     */
    private void chooseCriteria(WebDriver driver, String criteria){

        WebElement criteria_menu = driver.findElement(By.id(ScreeningUtils.ID_CRITERIA_OPTIONS));
        criteria_menu.click();
        List<WebElement> options = criteria_menu.findElements(By.tagName("option"));
        // we remove the first one which is the 'select...'
        options.remove(0);
        Utility.chooseWebElementAndClick(options,criteria);

    }

    /**
     * this method is for choosing
     * to exclude a paper and pass
     * to the next screening paper
     * @param driver the web driver
     * @param criteria the criteria of exclusion
     */
    public void excludePaper(WebDriver driver,String criteria){
      //  getExcludePaperButton(driver);
        chooseCriteria(driver,criteria);
        saveAndGetNextPaperForScreening(driver);
    }

    /**
     *
     * this function will click on the include button
     * for a screening phase and pass to the next paper
     * for screening
     * @param driver the web driver
     */
    public void includePaper(WebDriver driver){
        // click the 'include' button
        getIncludePaperButton(driver);
        // pass to the next paper for screening
        saveAndGetNextPaperForScreening(driver);
    }

}
