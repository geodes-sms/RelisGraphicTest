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
import static utils.QAUtils.*;
import static utils.ScreeningUtils.CLASS_SUCCESS_BUTTON;

public class Views {


    private static final String LK_SETTINGS = "Settings";
    private static final String CSS_VALIDATION_RESULT = "Results" ;

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
        List<WebElement> users = Views.findElementsBy(driver,By.className("form-group"));

        users.remove(users.size()-1);
        WebElement review = users.get(users.size()-1);
        if(review.findElement(By.tagName("label")).getText().equals("Reviews per paper"))
            users.remove(users.size()-1);
        // we don't have enough user to choose ?
        if(users.size() <= number){ // if so we choose all the users
         
            for( WebElement elem  : users){

                        if(elem.getText().equals("Reviews per paper")) continue;
                        elem.findElement(By.tagName("span")).click();
                        RelisUser user  = Utility.getUserByFullName(elem.getText());
                        reviewers.add(user);

                    }

        // otherwise we choose n users
        } else{
            // we remove the first two elements which is not important

           // users.remove(users.size()-1);
            System.out.println("We have" + users.size() +" and we want" +
                    " " + number);
            do{
                // choose a random user
                int size = users.size();
                int i = Utility.nextInt(0,size);
                WebElement userWebELment = users.remove(i);
                System.out.println("WEB ELEMENT USER CHOOSED =>" + userWebELment.getText()
                +" $$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$\n\n");
                RelisUser user  = Utility.getUserByFullName(userWebELment.getText());
                reviewers.add(user);
                
                userWebELment.findElement(By.tagName("span")).click();
            } while (reviewers.size() < number);
        }

        return reviewers;
    }

    public static List<WebElement> findElementsBy(WebDriver driver, By className) {

        WebDriverWait webDriverWait = new WebDriverWait(driver, 2);
        return webDriverWait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(className));
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

        WebElement ul = findElementBy(driver, By.className(CLASS_SIDE_BAR_MENU));
        List<WebElement> li = ul.findElements(By.tagName("li"));

        return li.stream()
                .filter(p-> {
                    //System.out.println("(" +p.findElement(By.tagName("a")).getText() +", "+ name +")");
                    return p.findElement(By.tagName("a")).getText().equals(name);
                })
                .findFirst()
                .orElse(null);
    }


    public static WebElement open_sub_menu_admin(WebDriver driver,  String name){
        WebElement ul = findElementsBy(driver, By.className(CLASS_SIDE_BAR_MENU)).get(1);
        List<WebElement> li = ul.findElements(By.tagName("li"));

        return li.stream()
                .filter(p-> {
                    System.out.println("(" +p.findElement(By.tagName("a")).getText() +", "+ name +")");
                    return p.findElement(By.tagName("a")).getText().equals(name);
                })
                .findFirst()
                .orElse(null);

    }


    public static void choose_sub_menuFrom(WebElement menu, String subMenu){

        List<WebElement> elements = menu.findElement(By.className(CLASS_CHILD_SIDE_MENU)).findElements(
                By.tagName("li")
        );

        try {
            WebElement option =
                    elements.stream().filter( li -> {


                      System.out.println("(" +li.findElement(By.tagName("a")).getText() +", "+ subMenu +")");
                        return li.findElement(By.tagName("a")).getText().equals(subMenu);
                            } )
                            .findFirst().get();
            option.findElement(By.tagName("a")).sendKeys(Keys.ENTER);
//            System.out.println("submenu clicked");

        } catch (Exception e){

          // System.err.println("Element do not exist {{{{ li= " + menu.getText() +" @ " +  subMenu+"}}}}}}}}}}}}");
            e.printStackTrace();
        }

    }


    public static void open_admin_menu_options(WebDriver driver, String menu, String sub_name) {

        WebElement element = open_sub_menu_admin(driver, menu);
        if(element == null) return;
        try {
            if(!Utility.hasClass(element, "active")){
                element.click();
                open_sub_menu_admin(driver, menu);
            }
            choose_sub_menuFrom(element, sub_name);
        } catch (Exception e ){
            element = open_sub_menu_admin(driver,menu);

            scrollToElement2(driver,element);
            element = getSideBarMenuOptionsOf(driver,menu);
            element.click();
            element = getSideBarMenuOptionsOf(driver,menu);
            choose_sub_menuFrom(element, sub_name);
        }


    }
    /**
     * open the progress bar page of every classificator
     * @param driver the webdriver
     */
    public static void openSuBMenuFrom(WebDriver driver, String menu_name, String subOptions){

        try {

            WebElement menu = getSideBarMenuOptionsOf(driver,menu_name);
            menu.click();
            menu = getSideBarMenuOptionsOf(driver, menu_name);
            choose_sub_menuFrom(menu, subOptions);
        } catch (Exception e){

            WebElement menu = getSideBarMenuOptionsOf(driver,menu_name);

            scrollToElement2(driver,menu);
            menu = getSideBarMenuOptionsOf(driver,menu_name);
            menu.click();
            menu = getSideBarMenuOptionsOf(driver,menu_name);
            choose_sub_menuFrom(menu, subOptions);
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
    public static void scrollToElement2(WebDriver driver, WebElement element){
        JavascriptExecutor js = (JavascriptExecutor) driver;
        // Scrolling down the page till the element is found
        js.executeScript("arguments[0].scrollIntoView();",element);
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

    /**
     * get the web element for the project phase setting
     * like Screening, Quality Assement.
     * @param mode the phase name
     * @param driver the web driver
      * @return the web element for the setting or else null
     */
    public static WebElement getSettingsWebElementFor(String mode, WebDriver driver){

        open_admin_menu_options(driver, LK_PLANNING,LK_SETTINGS);
        WebElement settings = driver.findElement(By.className("x_content"));

        List<WebElement> divs  = settings.findElements(By.cssSelector("div:first-child"));

        return  divs.stream().filter( element -> {

                    By title = By.cssSelector(".x_title h2");
                    return element.findElement(title).getText()
                            .equals(mode);
                })
                .findFirst().orElse(null);

    }


    private static void manage_validation_phase(WebDriver driver, String phase, String validation_label, boolean enable){

        WebElement setting = getSettingsWebElementFor(phase, driver);

        if(setting == null) return;

        setting.findElement(By.cssSelector(LK_EDIT_BTN)).sendKeys(Keys.ENTER);

        List<WebElement> form_group = driver.findElements(By.className(ClassificationUtils.CLASS_FORM_GROUP));

        WebElement validation_web_element =
                form_group.stream().filter( element -> {

                            String dom_text  = element.findElement(By.tagName("label")).getText();
                            dom_text = dom_text.replace(" *", "");

                            return dom_text.equals(validation_label);
                        }
                ).findFirst().orElse(null);
        if(validation_web_element == null)
            return;

        WebElement checkBox =validation_web_element.findElement(By.tagName("div")).findElement(By.tagName("span"));

        if(enable){

            if( Views.isWhiteBg(checkBox.getCssValue("background-color"))) // if Checked
            {
                checkBox.click();
            }
        } else {
            if( !Views.isWhiteBg(checkBox.getCssValue("background-color"))) // if Checked
            {
                checkBox.click();
            }
        }

        driver.findElement(By.className(CLASS_SUCCESS_BUTTON)).click();
    }

    /**
     * this function will enable the validation phase
     * for a project phase like classification
     * @param driver the web driver
     * @param phase the project phase name
     * @param validation_label the label displayed for the validation process
     */
    public static  void enable_validation(WebDriver driver, String phase, String validation_label){
            manage_validation_phase(driver,phase,validation_label, true);
    }

    /**
     * this function will desable the validation phase
     * for a project phase like classification
     * @param driver the web driver
     * @param phase the project phase name
     * @param validation_label the label displayed for the validation process
     */
    public static void desable_validation(WebDriver driver, String  phase, String validation_label){
        manage_validation_phase(driver,phase,validation_label, false);
    }


    /**
     * this method open the assign validators page
     * @param driver the web driver
     */
    public static void openAssignValidatorsPage(WebDriver driver){

        driver.findElement(By.linkText(ScreeningUtils.LK_DASHBORD_LINK)).click();
        driver.findElement(By.cssSelector(CSS_ASSIGN_VALIDATORS_PAGE))
                .sendKeys(Keys.ENTER);

    }

    /**
     * this function will open the validate page
     * @param driver the web driver
     */
    public static void openValidatePaperPage(WebDriver driver){
        driver.findElement(By.linkText(ScreeningUtils.LK_DASHBORD_LINK)).click();
        driver.findElement(By.cssSelector(CSS_VALIDATE_BTN))
                .sendKeys(Keys.ENTER);

    }


    /**
     *
     * this function will open the validation result page
     * @param driver the web driver
     */
    public static void open_validation_result_page(WebDriver driver){
        openSuBMenuFrom(driver, LK_VALIDATION_MENU,CSS_VALIDATION_RESULT);
    }


    /**
     * this function will open all papers to validate web page
     * @param driver the web driver
     */
    public static void open_all_validate_assignments(WebDriver driver){

        openSuBMenuFrom(driver,LK_VALIDATION_MENU, LK_ALL_PAPERS_TO_CLASSIFY);
    }



    public static void make_incorrect_decision(WebDriver driver, String note){

        driver.findElement(By.id(ID_VALIDATION_NOTE_TEXT_AREA)).sendKeys(note);
        driver.findElement(By.className(CLASS_SUCCESS_BUTTON)).click();

    }


    public static WebElement findElementBy(WebDriver driver, By elem){

        WebDriverWait driverWait = new WebDriverWait(driver,2);
       return driverWait.until(ExpectedConditions.presenceOfElementLocated(elem));
    }
}
