package view;

import controller.ProjectController;
import model.Paper;
import model.RelisUser;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import utils.*;

import java.util.*;
import java.util.function.BiFunction;

import static utils.ClassificationUtils.*;

public class Views {


    /**
     * this fonction chooses random users
     * for the screening phase or the classification phase
     * @param driver the web driver
     * @param number the number of user that we wanna choose
     *   if  number is greather than the user of the project
     *   we choose all the project users
     * @return an array of all the choosed users
     */
    public   static ArrayList<RelisUser> chooseUserforScreening(WebDriver driver, int number){

        ArrayList<RelisUser> reviewers = new ArrayList<>();

        // list of every users web element
        List<WebElement> users = driver.findElements(By.className("form-group"));
        users.remove( users.size()-1);

        // we don't have enough user to choose ?
        if(users.size() <= number){ // if so we choose all the users

            users.forEach( elem ->
                    {
                        elem.findElement(By.tagName("span")).click();
                        RelisUser user  = Utility.getUserByFullName(elem.getText());
                        reviewers.add(user);

                    }
            );
        // otherwise we choose n users
        } else{
            // we remove the first two elements which is not important

            users.remove(users.size()-1);
            do{
                // choose a random user
                int size = users.size();
                int i = Utility.nextInt(0,size);
                WebElement userWebELment = users.remove(i);
                RelisUser user  = Utility.getUserByFullName(userWebELment.getText());
                reviewers.add(user);
                userWebELment.findElement(By.tagName("span")).click();
            } while (reviewers.size() < number);
        }

        return reviewers;
    }


    /**
     *
     * @param driver
     * @return
     */
    public static ArrayList<RelisUser> extractUsers(WebDriver driver){
        WebElement element =  driver.findElement(By.className("x_content"));
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


    public static void clickLiWebElement(WebDriver driver,WebElement ul, String name){
        WebDriverWait webDriverWait = new WebDriverWait(driver,2);
        for (WebElement li : ul.findElements(By.tagName("li"))){
            try{
                if(li.getText().equals(name)){
                    Views.performClickWithJsExec(driver,li.findElement(By.tagName("a")));
                    return;
                }
            }
            catch (Exception e){
                System.out.println("***********************************************88\n\n\n");
                e.printStackTrace();
                System.out.println("**********************************************8\n\n");
            };


        }
    }


    public static WebElement work_through_table(WebDriver driver, BiFunction<List<WebElement>,String,WebElement>
            action, String params){

        try {
            // select the table that contains the papers
            WebElement table = driver.findElement(By.id(ProjectUtils.ID_PROJECT_TABLE_USERS));

            WebElement element ;
            WebElement result = null;

            while (true){
                try{
                    // get web element for the next click link
                    element = driver.findElement(By.id(ProjectUtils.ID_NEXT_PAPERS_PAGE));
                    // get all the papers present from the current table
                    List<WebElement> other_papers = table.findElements(By.tagName("tr"));
                    // we remove the first web element which is the table header
                    other_papers.remove(0);

                    result = action.apply(other_papers,params);
                    if( result != null) return result;
                    // there is no next table  ?
                    if(Utility.hasClass(element,"disabled")) break;
                    element.findElement(By.linkText("Next")).click();
                } catch (Exception e){
                    return null;
                }
            }
            return  result;

        } catch (Exception e){
            return  null;
        }


    }


    /**
     * return the web element of a 'menu' of the left side bar
     * like an example the 'classification menu'
     * @param driver the web driver
     * @param name the menu name
     * @return a web element for the menu with name=name
     */
    public static WebElement getSideBarMenuOptionsOf(WebDriver driver, String name){

        WebElement ul = driver.findElement(By.className(CLASS_SIDE_BAR_MENU));
        List<WebElement> li = ul.findElements(By.tagName("li"));

        return li.stream()
                .filter(p-> p.getText().equals(name))
                .findFirst()
                .orElse(null);
    }


    /**
     * open the progress bar page of every classificator
     * @param driver the webdriver
     */
    public static void openSuBMenuFrom(WebDriver driver, String menu_name, String subOptions){

        WebDriverWait webDriverWait  = new WebDriverWait(driver,5);

        try {
            driver.findElement(By.linkText(subOptions)).click();

        } catch (Exception m){
            scrollToElement(driver,By.linkText(menu_name));
            driver.findElement(By.linkText(menu_name)).click();
            webDriverWait.until(ExpectedConditions.presenceOfElementLocated(
                    By.linkText(subOptions)
            )).sendKeys(Keys.ENTER);

        }
    }

    /**
     * open the classification phase
     * @param driver the web driver
     * @param phaseName the word classification
     */
    public static void openProjectPhase(WebDriver driver, String phaseName, String link){

        // select the table that contains the screening phaseas
        WebElement table = driver.findElement(By.className(ScreeningUtils.CLASS_SCREENING_PHASES_TABLE));

        // get all the systematic review phases
        List<WebElement> trs = table.findElements(By.tagName("tr"));
        trs.remove(0);
        // we remove the first web element which is the table header

        for(WebElement element : trs){

            List<WebElement> tds = element.findElements(By.tagName("td"));

            WebElement phase = tds.get(0);
            if((phase !=null) && phase.getText().equals(phaseName)){
                WebElement gotoPhae;

                try{
                    gotoPhae = tds.get(4).
                            findElement(By.cssSelector(link));
                    gotoPhae.sendKeys(Keys.ENTER);
                } catch (Exception e){
                    element.findElement(By.cssSelector(ClassificationUtils.CSS_UNLOCK_CLASSIFICATION))
                            .sendKeys(Keys.ENTER);
                    openProjectPhase(driver, phaseName,link);
                }

                return;
            }
        }
    }

    public static void openUserAssignmentPage(WebDriver driver, String link){

        try {
            driver.findElement(By.linkText(ProjectUtils.LK_CURRENT_PROJECT)).click();
        } catch (Exception e){
            driver.findElement(By.linkText(ScreeningUtils.LK_DASHBORD_LINK)).click();
        }

        driver.findElement(By.cssSelector(link))
                .sendKeys(Keys.ENTER);

    }


    public static void scrollToElement(WebDriver driver, By element){
        JavascriptExecutor js = (JavascriptExecutor) driver;
        // Scrolling down the page till the element is found
        js.executeScript("arguments[0].scrollIntoView();",driver.findElement(element));
    }

    public static void performClickWithJsExec(WebDriver driver, WebElement element){
        JavascriptExecutor js=(JavascriptExecutor) driver;

//        js.executeScript("arguments[0].click()", element);
        element.click();

    }

    public static boolean isWhiteBg(String bg){
        return bg.equals("rgba(255, 255, 255, 1)");
    }

    /**
     * this function will open  a menu item
     * @param driver the webdriver
     * @param menu_name the menu
     */
    public static void openMenuFrom(WebDriver driver, String menu_name) {

        scrollToElement(driver,By.linkText(menu_name));
        driver.findElement(By.linkText(menu_name)).click();
    }


    /**
     * this method will check wether we have pending papers for a phase
     * @param driver the webdriver
     * @return true if there's no pending papers otherwise false
     */
    public static boolean noPendingPapers(WebDriver driver){

        driver.findElement(By.linkText(ScreeningUtils.LK_DASHBORD_LINK)).click();
        List<WebElement> counts = driver.findElements(By.className("count"));

        String count = counts.get(2).getText();

        return count.equals("0");
    }

    public static void getSettingsWebElementFor(String mode, WebDriver driver){

        List<WebElement> options = driver.findElement(By.className("x_content")).findElements(
          By.tagName("div")
        );
    }
}
