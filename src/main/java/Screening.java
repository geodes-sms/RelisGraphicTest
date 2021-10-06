import org.openqa.selenium.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class Screening {



    private ArrayList<WebElement> phases =new ArrayList<>();
    private  final ArrayList<RelisUser> reviewer = new ArrayList<>();
    private WebDriver driver = null;



    public void setUp(WebDriver main_driver){
        getReviewer(main_driver);
        RelisUser user = reviewer.get(0);

        Initialiazer initialiazer = new Initialiazer();
        initialiazer.init();
        driver = initialiazer.getWebDriver();

        Connexion connexion = new Connexion();
        connexion.connect(driver,user);
        ProjectManager projectManager  = new ProjectManager();
        projectManager.openProject(driver,"");
        this.open_current_screening_phase(driver);

    }
    private  void work_through_table(WebDriver driver){


        // select the table that contains the screening phaseas
        WebElement table = driver.findElement(By.className(ScreeningUtils.CLASS_SCREENING_PHASES_TABLE));

        // get all the systematic review phases
        List<WebElement> phases = table.findElements(By.tagName("tr"));

        // we remove the first web element which is the table header
        phases.remove(0);
        removeAllPhases();
        System.out.println("c'est vider " + this.phases.size());
        this.phases = ((ArrayList<WebElement>) phases.stream()
                .filter(Screening::takeScreenPhases)
                .collect(Collectors.toList()));

    }

    private  void removeAllPhases() {
        for (WebElement w: this.phases) {
            phases.remove(w);
        }
    }

    /**
     *
     * @param phase the project phase web element
     * @return true if it is a screening phase otherwise false
     */
    private static boolean takeScreenPhases( WebElement phase){


        List<WebElement> phaseElements= phase.findElements(By.tagName("td"));

            for (int j = 0; j < phaseElements.size(); j++)
                if(phaseElements.get(0).getText().startsWith(ScreeningUtils.SCREEN_PHASE_START_WORD))
                    return true;
        return false;
    }

    private void openAssignPaperPage(WebDriver driver){
        open_current_screening_phase(driver);

        WebElement button = driver.findElement(By.cssSelector(ScreeningUtils.CSS_ASSIGN_PAPER_BUTTON));
        // we choose assign paper options
        button.sendKeys(Keys.ENTER);
    }

    private void openScreeningPage(WebDriver driver){
        WebElement button = driver.findElement(By.cssSelector(ScreeningUtils.CSS_SCREEN_BUTTON));
        // we choose assign paper options
        button.sendKeys(Keys.ENTER);
    }
//    private void showTableItem(WebElement tr){
//        List<WebElement> elements= tr.findElements(By.tagName("td"));
//        elements.forEach(p-> System.out.print(p.getText() +" "));
//        System.out.println();
//
//    }
    public void assign_papers(WebDriver driver){
        openAssignPaperPage(driver);
        List<WebElement> users = driver.findElements(By.className("form-group"));
        users.remove( users.size()-1);
        users.remove(users.size()-1);
        // we select some random users
        chooseUserforScreening(users);
        // we submit the assoignment
        driver.findElement(By.className(ScreeningUtils.CLASS_SUCCESS_BUTTON)).click();
    }

    private void open_current_screening_phase(WebDriver driver) {
        work_through_table(driver);
        WebElement ph1 = this.phases.get(0);
        System.out.println("Current other X =>  " + ph1.getText());
        List<WebElement> elementList = ph1.findElements(By.tagName("td"));

        WebElement element = Utility.chooseWebElement(elementList, ScreeningUtils.SCREENING_MANAGER_ELEMENT);
        assert element != null;

        // open the screening phase
        ((JavascriptExecutor)driver).executeScript("arguments[0].click();",
                element.findElement(By.className(ScreeningUtils.CLASS_GOTO_PHASE)));
    }

    private  void chooseUserforScreening(List<WebElement> users){

        Random random = new Random();
        if(users.size() < 4){
            users.forEach( elem ->
                    {
                        RelisUser user  = Utility.getUserByFullName(elem.getText());
                        reviewer.add(user);
                        elem.findElement(By.tagName("span")).click();
                    }
            );
            return;
        }
        do{
            // choose a random user
            int i = random.nextInt(0,users.size());
            WebElement userWebELment = users.remove(i);
            RelisUser user  = Utility.getUserByFullName(userWebELment.getText());
            reviewer.add(user);
            userWebELment.findElement(By.tagName("span")).click();
        } while (reviewer.size() < 4);
    }




    /**
     * return all the reviewers for the current project
     * @param driver
     */
    public void getReviewer(WebDriver driver){
        open_current_screening_phase(driver);
        // go to screening menu
        showProgressScreening(driver);
        WebElement element =  driver.findElement(By.className("x_content"));
        ArrayList<String> userName= new ArrayList<>(Arrays.asList(element.getText().split("\n")));
        userName.remove(0);
        userName.remove(userName.size()-1);

        removeAllReviewers();
        userName.stream()
                .map(p -> {
                    int i = p.indexOf(":");
                    return p.substring(0,i);
                })
                .forEach(full_name
                -> reviewer.add(Utility.getUserByFullName(full_name))
                );
    }

    private void showProgressScreening(WebDriver driver) {
        Dimension dimension = driver.manage().window().getSize();
        driver.manage().window().setSize(new Dimension(977,dimension.height));
        MainTest.sleep(6);

        driver.manage().window().setSize(new Dimension(975,dimension.height));
        checkWindownSize(driver);
        driver.findElement(By.linkText(ScreeningUtils.LK_OPEN_SCREENING_MENU)).click();
        driver.findElement(By.linkText(ScreeningUtils.LK_SCREENING_PROGRESS_PAGE)).sendKeys(Keys.ENTER);
    }

    private  void removeAllReviewers() {
        for (RelisUser r : reviewer) {
            reviewer.remove(r);
        }
    }

    private void checkWindownSize(WebDriver driver){

        if(driver == null) return;
        Dimension dimension= driver.manage().window().getSize();
        if(dimension.getWidth() < 977)
            driver.findElement(By.className(ScreeningUtils.CLASS_MAIN_MENU)).click();
        System.out.println("Dimension changed");

    }
    public void startUserScreeningPhase(){

        driver.findElement(By.linkText(ScreeningUtils.LK_DASHBORD_LINK)).click();
        this.openScreeningPage(driver);



    }
}
