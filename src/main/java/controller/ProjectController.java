package controller;

import lombok.NonNull;
import model.*;
import org.apache.hc.core5.concurrent.FutureCallback;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Action;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import utils.*;
import view.ScreeningView;
import view.Views;

import java.io.File;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static utils.ClassificationUtils.*;
import static utils.PaperUtils.*;
import static utils.ProjectUtils.*;


public class ProjectController {




    // FUNCTIONS PROGRAMMING

    // check if a paper is present given the list of paper
    private static final ThirdParamsFunctions paperIsPresent =
            ProjectController::checkPaperPresence;
    // delete a paper
    private static final ThirdParamsFunctions deletePaper_func
            = ProjectController::deletePaper;
    private static  final FourthParamsFunctions hasARole
            = ProjectController::HasARole;

  private static final ScreeningController screening_controller = new ScreeningController();
  private static final ScreeningPhaseController screeningPhaseController = new ScreeningPhaseController();


    
     /**
      * this function open all the papers papge 
      * @param driver the web driver
      */
    public static void openAllPapersPage(WebDriver driver) {
        openAllPaper(driver);
    }

    public static void re_open_main_project_menu(WebDriver driver, String project_name) {

        ProjectController.openProjectListPage(driver);
        ProjectController.openProject(driver,project_name);
    }

    /**
     * this function suppose to create a new project by
     * uploading the ".php" configuration
     * @param driver
     * @param project_id
     */
    public void createProject(WebDriver driver, String project_id) {


        openProjectListPage(driver);
        // push the  create new project button
        driver.findElement(By.linkText(ProjectUtils.LK_ADD_NEW_PROJECT_BUTTON)).click();
        System.out.println(" Le project id est =>" + project_id);
        Views.choose_project( driver, project_id);
        // we create the project

        // then create the project by clicking the submit button
        driver.findElement(By.cssSelector(CSS_BTN_SUBMIT)).click();
    }

    public static void open_project_all_phases(WebDriver driver, String projet_name){

        openProject(driver, projet_name);
        driver.findElement(By.className(CLASS_HOME_PROJECT)).click();
        open_project_phases(driver);
    }
    private static void open_project_phases(WebDriver driver){

        WebElement table  = driver.findElement(By.className(ScreeningUtils.CLASS_SCREENING_PHASES_TABLE));

        List<WebElement> phases = table.findElements(By.tagName("tr"));
        phases.remove(0); // remove the header tr
        WebElement ph = phases.stream()
                .filter( phase -> {
                    List<WebElement> tds = phase.findElements(By.tagName("td"));
                    System.out.println( tds.get(1).getText());
                    return tds.get(1).getText().equals(CLOSED_PHASE);}
                ).findFirst().orElse(null);
        if(ph != null){
            List<WebElement> tds = ph.findElements(By.tagName("td"));
            int last = tds.size()-1;
            tds.get(last).findElement(By.className(CLASS_UPLOAD_IMPORTED_PAPERS_BUTTON)).sendKeys(Keys.ENTER);
            open_project_phases(driver);
        }
    }
    /**
     * this function take a project name and it will open the correspondant project
     * @param driver the web driver
     * @param projectName the project name
     */
    public static void openProject(WebDriver driver, String projectName) {
        openProjectListPage(driver);
       // get all the project in a list
        List<WebElement> projectElements = driver.findElements(By.className("thumbnail"));
       // find the project that has a project name like the parameter project name
        projectElements.stream()
                .filter(projet -> projet.findElement(By.className("caption")).getText().equals(projectName))
                .findFirst()
                .ifPresent(projectElement -> // if we found the project then we click to open
                projectElement.findElement(By.tagName("a")).sendKeys(Keys.ENTER));
    }

    /**
     *  delete all the papers for the current open project
     * @param driver the web driver
     */
    public void deleteAllPapers(WebDriver driver){

        openAllPaper(driver);
        // click the delete all button
        driver.findElement(By.linkText(ProjectUtils.LK_DELETE_ALL_PAPERS_BUTTON)).click();
        // confirm the deletion
        driver.findElement(By.linkText(ProjectUtils.LK_CONFIRM_DELETE_ALL_PAPERS)).click();
    }

    /**
     * @param driver
     * @param fileName the file where the bibtex is located
     *                 This procedure implement the function for uploading a paper
     *                 using Bibtex
     */
    public void uploadFromBibTeXPaper(WebDriver driver, String fileName) {


        String bib_msg = FileUtils.getLinesFrom(fileName);

        // open the all paper menu
        openAllPaper(driver);
        // push to bibtex button
        driver.findElement(By.linkText(PaperUtils.BIBTEX_MODE)).click();
        driver.findElement(By.name(PaperUtils.NAME_BIBTEX_TEXT_AREA)).
                sendKeys(bib_msg);
        driver.findElement(By.cssSelector(PaperUtils.CSS_SUBMIT_BUTTON)).click();
        // close the bibtex mode
        driver.findElement(By.className(PaperUtils.CLASS_CLOSE_PAPER_BUTTON)).click();

    }


/**
 * this method open all the paper page for the project
 * @param driver
 */
    public static void openAllPaper(WebDriver driver) {

        // go to the paper
        driver.findElement(By.className(PaperUtils.CLASS_NAME_OPEN_PAPER)).click();
        // go to all paper
        driver.findElement(By.linkText(PaperUtils.LINK_TEXT_ALL_PAPER)).click();

    }


    /**
     * importing papers from bibtex file
     *
     * @param driver
     * @param fileName
     */
    public void importBibTexPapers(WebDriver driver, String fileName) {

        importPapers(driver, LK_BIBTEX_IMPORT_MODE,fileName);
     
    }

    public void addVenue(WebDriver driver, String venuename){

        Views.openMenuFrom(driver,LK_VENUES);
        driver.findElement(By.cssSelector(CSS_ADD_VENUE)).click();
        driver.findElement(By.id(ID_VENUE_FULL_NAME)).sendKeys(venuename);
        driver.findElement(By.id(ID_VENUE_YEAR_INPUT)).sendKeys("2021");

        driver.findElement(By.className(CLASS_SUCCESS_BUTTON)).click();
       assert  find_venue(driver, venuename);

    }

    public static final WebElement getValueFrom(WebDriver driver, List<WebElement> values, Object subject, int pos){

        values.remove(0); // remove the header

        String value_looking_for = (String) subject;
        System.out.println(" le subject => " + subject);

        WebElement value = values.stream()
                .filter( val ->{
                    boolean is_true = false;

                    List<WebElement> tds = val.findElements(By.tagName("td"));
                    System.out.println("entry => " + tds.get(pos).getText());
                    if(tds.get(pos).getText().equals(value_looking_for))
                        is_true = true;
                    return is_true;
                })
                .findFirst().orElse(null);

        return value;
    }

    /**
     * delete a venue
     * @param driver the web driver
     * @param venuName the venu name to delete
     * @return boolean if venue exists and it's deleted
     */
    public static boolean delete_venue(WebDriver driver, String venuName){


        Functions func = (FourthParamsFunctions) ProjectController::_delete_venue;
        int x =  Utility.find_element_table_id(driver, func, null,venuName );
        return x ==1;

    }
    private static int _delete_venue(WebDriver driver,List<WebElement>
            values, Object subject, Object object){


        WebElement value = getValueFrom(driver, values, subject,1);


        if(value != null){
            List<WebElement> tds = value.findElements(By.tagName("td"));
            tds.get(0).findElement(By.linkText(LK_DELETE_VAL)).click();
            driver.switchTo().alert().accept();

            return 1;
        }

        return 0;
    }
    public static boolean find_venue(WebDriver driver, String venue_full_name){

        Functions func = (FourthParamsFunctions) ProjectController::_find_venue;
        int x =  Utility.find_element_table_id(driver, func, null,venue_full_name );
        return x ==1;
    }

    private static int _find_venue(WebDriver driver,List<WebElement>
            values, Object subject, Object object){


        WebElement value = getValueFrom(driver,values,subject,1);

        return (value != null)? 1 : 0;



    }


    private static int _find_author(WebDriver driver,List<WebElement>
            values, Object author, Object object){

        WebElement value = getValueFrom(driver,values,author,0);

        return (value != null)? 1 : 0;



    }

    public static int find_author(WebDriver driver, String author_name){

        Functions functions = (FourthParamsFunctions) ProjectController::_find_author;
        return Utility.find_element_table_id(driver,functions,null,author_name);
    }

    /**
     * this function will add a new author to a project
     * @param driver the web driver
     * @param author_full_name the author full name
     */
    public static void add_authors(WebDriver driver, String author_full_name){

        Views.openSuBMenuFrom(driver,LK_AUTHORS_MENU,LK_ALL);
        driver.findElement(By.cssSelector(CSS_ADD_NEW_AUTHOR_BTN)).click();

        driver.findElement(By.id(ID_INPUT_AUTHOR_FULL_NAME)).sendKeys(author_full_name);
        driver.findElement(By.className(CLASS_BTN_SUCCES)).click();
        int x = find_author(driver, author_full_name);
        assert x != 3;
    }

    /**
     * this function will return a venue from the browser
     * @param driver
     * @return
     */
    public static String getA_venue(WebDriver driver){
        try {
            Views.openMenuFrom(driver,LK_VENUES);
        } catch (Exception e){

        }
        Functions functions = (FourthParamsFunctions) (driver1, webElementList, user, obj) -> {
            webElementList.remove(0); // remove the header

            int x = Utility.nextInt(0, webElementList.size());
            if (webElementList.size() > 0) {

                WebElement elem = webElementList.get(x);
                List<String> data = (List<String>) obj;
                data.add( elem.findElements(By.tagName("td")).get(1).getText());
            }
            return 1;
        };
        ArrayList<String> res = new ArrayList<>();
        Utility.find_element_table_id(driver,functions,res,null);

        return res.get(0);


    }

    /**
     * import papers from csv file
     * @param driver the web driver
     * @param fileName the file name
     */
    public void importPapersFromCSV(WebDriver driver, String fileName){
        importPapers(driver, LK_CV_IMPORT_MODE,fileName);
    }

    /**
     * import papers from endnote file
     * @param driver the web driver
     * @param fileName the file name
     */
    public void importPapersFromEndNote(WebDriver driver, String fileName){
        importPapers(driver, LK_END_NOTE_IMPORT_MODE, fileName);
    }

    private void importPapers(WebDriver driver, String mode, String fileName){

        // push the import menu
        driver.findElement(By.linkText(PaperUtils.LK_IMPORT_PAPER)).click();
        //choose using Bibtex option
        new WebDriverWait(driver, 5).until(ExpectedConditions.elementToBeClickable(
                By.linkText(LK_BIBTEX_IMPORT_MODE))).click();
        File bib_file = new File(fileName);
        // we gotta choose the file and import all the papers from it
        driver.findElement(By.name(PaperUtils.NAME_BIBTEX_FILE_CHOOSE_ELEM)).sendKeys(bib_file.getAbsolutePath());
        driver.findElement(By.className(PaperUtils.CLASS_UPLOAD_IMPORTED_PAPERS_BUTTON)).click();

        // now we  commit the imported papers
        driver.findElement(By.className(PaperUtils.CLASS_UPLOAD_IMPORTED_PAPERS_BUTTON)).click();
        // back to the all papers
        //driver.findElement(By.className(utils.PaperUtils.CLASS_BACK_FROM_PAPERS_IMPORT_BUTTON)).click();

    }

    /**
     * adding a reviewer to a project
     *
     * @param driver
     * @param user
     */
    private void addRoleForProject(WebDriver driver, RelisUser user,String role) {
        // go to the users page;
        WebElement menu_user = Views.getSideBarMenuOptionsOf(driver,LK_USERS_MENU);

        menu_user.findElement(By.tagName("a")).click();


        // push the + button
        driver.findElement(By.className(ProjectUtils.CLASS_ADD_USER_BUTTON)).click();
        // now we show all the users
        driver.findElement(By.className(ProjectUtils.CLASS_SHOW_ALL_USERS)).click();
        WebElement users_ul = driver.findElement(By.id(ProjectUtils.ID_RELIS_ALL_USERS));
        // we have the list that contains all the users
        List<WebElement> links = users_ul.findElements(By.tagName("li"));
        // choose the user to assign as a reviewer
        WebElement webElement = Utility.chooseWebElement(links, user.getFull_name());
        if(webElement != null) webElement.click();
        // show the  all users role
        driver.findElement(By.id(ProjectUtils.ID_USER_ROLES_OPTIONS)).click();
       // we gotta choose a specific role
        WebElement list_of_roles = driver.findElement(By.id(ProjectUtils.ID_ALL_USER_ROLE_FOR_A_PROJECT));
        List<WebElement> roles = list_of_roles.findElements(By.tagName("li"));
        // we choose the user role and click it
        roles.stream()
                .filter(p -> role.equals(p.getText()))
                .findFirst()
                .ifPresent(WebElement::click);

        driver.findElement(By.className(ProjectUtils.CLASS_SUCCESS_BUTTON)).click();

    }

    /**
     * add a user as a reviewer for the current project
     * @param driver
     * @param user
     */
    public void addReviewer(WebDriver driver, RelisUser user){


        this.addRoleForProject(driver,user,ProjectUtils.REVIEWER_ROLE);
    }

    /**
     * assign a user as a project manager
     * @param driver
     * @param user
     */
    public void addProjectManager( WebDriver driver, RelisUser user){
        this.addRoleForProject(driver,user,ProjectUtils.PROJECT_MANAGER_ROLE);
    }

    /**
     * adding a user as a validator for the current project
     * @param driver
     * @param user
     */
    public void addValidator(WebDriver driver, RelisUser user){
        this.addRoleForProject(driver,user,ProjectUtils.VALIDATOR_ROLE);
    }

    /**
     *  add a user as a guest for the current open project
     * @param driver
     * @param user
     */
    public void addUserAsGuest(WebDriver driver, RelisUser user){
        this.addRoleForProject(driver,user,ProjectUtils.GUEST_ROLE);
    }


    /**
     * remove a reviewer from a project
     *
     * @param driver
     * @param reviewer
     */
    public void removeUserRole(WebDriver driver, RelisUser reviewer) {
        // go to the users page
        driver.findElement(By.className(ProjectUtils.CLASS_PROJECT_USERS)).click();

        WebElement table = driver.findElement(By.id(ProjectUtils.ID_PROJECT_TABLE_USERS));
        List<WebElement> users = table.findElements(By.tagName("tr"));
        WebElement user_manager = getUserWebElement(users, reviewer.getFull_name());
        if (user_manager != null){
            user_manager.findElement(By.className(ProjectUtils.CLASS_REMOVE_PROJECT_USER)).click();
            Alert alert = driver.switchTo().alert();
            alert.accept();
        }

    }

    public void removeAllProjectUsers(WebDriver driver){
        // go to the users page
        driver.findElement(By.className(ProjectUtils.CLASS_PROJECT_USERS)).click();
        WebElement table = driver.findElement(By.id(ProjectUtils.ID_PROJECT_TABLE_USERS));
        Utility.sleep(5);
        WebElement elements = table.findElement(By.tagName("tbody"));
        List<WebElement> users = elements.findElements(By.tagName("tr"));
        //users.remove(0);
        users.forEach(p-> System.out.println("User " + p.getText()));

        for (int i = 0; i < users.size(); i++) {
            WebElement u = users.get(i);
            List<WebElement> k = u.findElements(By.tagName("td"));
            removeUserRole(driver, Utility.getUserByFullName(k.get(1).getText()));
        }


    }


    /**
     *  get all the papers from a project
     * @param driver
     * @return
     */
    private static int work_through_paper(WebDriver driver, Functions work,
                                          String COUNT_MODE, String subject){
        openAllPaper(driver);
        return work_through_table(driver, work,COUNT_MODE, subject, "");
    }

    /**
     *
     * get the length of the papers for the current project
     * @param driver
     * @return
     */
    public  int getProjectPapersLength(WebDriver driver){

        return work_through_paper(driver,null, PaperUtils.COUNT_PAPER_MODE,"");
    }

    /**
     * delete a specific paper using his key
     * @param driver web driver
     * @param papers the list of the papers
     * @return  1 if the paper is deleted or not existing
     */
    private static int deletePaper(WebDriver driver, List<WebElement> papers,Object papier){

        String paper = (String) papier;
        // get the web element for the paper to delete
        WebElement user_manager = getUserWebElement(papers, paper);
        if (user_manager != null){ // do we found the paper?
            // if so , we delete it
            user_manager.findElement(By.className(ProjectUtils.CLASS_REMOVE_PROJECT_USER)).click();
            Alert alert = driver.switchTo().alert();
            alert.accept();
            return 1;
        }
        return 0;

    }

    /**
     *  delete a given paper identified by his key
     *
     * @param driver the web driver
     * @param key the id of the paper
     *
     */
    public  void deletePaperByKey(WebDriver driver, String key){

        work_through_paper(driver,deletePaper_func,"",key);
    }

    /**
     * check if a specific paper exist in a project
     * @param driver the current web driver
     * @param key the paper identified by the key
     * @return true if the paper exist otherwise false
     */
    public boolean isPresentPaper(WebDriver driver, String key){
        int papers = work_through_paper(driver,paperIsPresent,"",key);

        return papers == 1;
    }


    private static int checkPaperPresence(WebDriver driver, List<WebElement> papers,Object papier){

        String paper = (String) papier;
        // get the web element for the paper to delete
        WebElement user_manager = getUserWebElement(papers, paper);
        return (user_manager == null) ? 0:1;
    }





    /**
     * get the specific user from a project users
     * @param users_data the list of the users
     * @param user_name the user that we're looking for
     * @return the web element for the user passed if exists otherwise null
     */
    private static WebElement getUserWebElement( List<WebElement> users_data, String user_name){


        for(int i=0; i< users_data.size(); i++){
            WebElement user = users_data.get(i);
            List<WebElement> user_info= user.findElements(By.tagName("td"));

            for (int j = 0; j < user_info.size(); j++)
                if(user_info.get(1).getText().equals(user_name))
                    return user;
        }
        return null;
    }


    /***
     *
     * @param users the list of users
     * @param user_name_obj the username that we are looking for
     * @param role_obj the user role
     * @return the user web element if exist otherwise null
     */
    private static WebElement checkUserRoleWebElement( List<WebElement> users,
                                                       Object user_name_obj, Object role_obj){

        String user_name = (String) user_name_obj, role = (String) role_obj;
        for(int i=0; i< users.size(); i++){

            WebElement user = users.get(i);
            List<WebElement> user_info= user.findElements(By.tagName("td"));
            String current_user_name = user_info.get(1).getText();
            String current_user_role = user_info.get(2).getText();
            if(current_user_name.equals(user_name) && current_user_role.equals(role))
               return user;
        }
        return null;

    }

    /**
     *
     * @param users the list of the users
     * @param user the current user we're looking for
     * @param role the user role
     * @return true if the user exist and has the role(param role) otherwise false
     */
    private static int HasARole( WebDriver driver,List<WebElement> users, Object user,Object role){

       return   (checkUserRoleWebElement(users,user,role) != null)? 1: 0;

    }







    /**
     *
     * @param driver the wen driver
     * @param user the user
     * @return true if the user has a reviewer role
     */
    public static boolean isAReviewer(WebDriver driver,@NonNull RelisUser user){

        int x = work_through_userRoles(driver,hasARole,
                user.getFull_name()
        ,ProjectUtils.REVIEWER_ROLE);
        return x ==1;
    }

    /**
     *
     * @param driver the wen driver
     * @param user the user
     * @return true if the user has a project manager role
     */
    public static boolean isAProjectManager(WebDriver driver, @NonNull RelisUser  user){
        int x = work_through_userRoles(driver,hasARole,
                user.getFull_name()
                ,ProjectUtils.PROJECT_MANAGER_ROLE);
        return x ==1;

    }

    /**
     *
     * @param driver the wen driver
     * @param user the user
     * @return true if the user has a validator role
     */
    public static boolean isAValidator(WebDriver driver, @NonNull RelisUser  user){
        int x = work_through_userRoles(driver,hasARole,
                user.getFull_name()
                ,ProjectUtils.VALIDATOR_ROLE);
        return x ==1;
    }
    /**
     *
     * @param driver the wen driver
     * @param user the user
     * @return true if the user has a guest role
     */
    public static boolean isAGuestUser(WebDriver driver, @NonNull RelisUser  user){
        int x = work_through_userRoles(driver,hasARole,
                user.getFull_name()
                ,ProjectUtils.GUEST_ROLE);
        return x ==1;

    }


    private static int work_through_userRoles(WebDriver driver, Functions action,
                                              String subject, String role){

        // go to the users page
        driver.findElement(By.className(ProjectUtils.CLASS_PROJECT_USERS)).click();
        return work_through_table(driver,action, "",subject,role);
    }
    /***
     *
     * @param driver the web driver
     * @param action a function
     * @param COUNT_MODE do we count the items of the table??
     * @param subject the item we're looking for
     * @param role the user role
     * @return 1 if the everything went normally otherwise 0
     */
    public static int work_through_table(WebDriver driver, Functions action, String COUNT_MODE,
                                          String subject, String role){

        try {
            // select the table that contains the papers
            WebElement table = driver.findElement(By.id(ProjectUtils.ID_PROJECT_TABLE_USERS));

            WebElement element ;
            int result =0;

            while (true){
                try{
                    // get web element for the next click link
                    element = driver.findElement(By.id(ProjectUtils.ID_NEXT_PAPERS_PAGE));
                    // get all the papers present from the current table
                    List<WebElement> other_papers = table.findElements(By.tagName("tr"));
                    // we remove the first web element which is the table header
                    other_papers.remove(0);
                    result = (COUNT_MODE.equals(PaperUtils.COUNT_PAPER_MODE))?
                            (result+other_papers.size()):
                            (!role.equals("")) ?((FourthParamsFunctions) action).apply(driver,other_papers,subject,role)
                                    :((ThirdParamsFunctions) action).apply(driver,other_papers,subject);
                    // do we did the change that we want??
                    if(!COUNT_MODE.equals(PaperUtils.COUNT_PAPER_MODE) && result == 1)
                        return result; // so we return

                    // there is no next table  ?
                    if(Utility.hasClass(element,"disabled")) break;
                    element.findElement(By.linkText("Next")).click();
                } catch (Exception e){
                    System.out.println("ERROR " + e.getMessage());
                    return 0;
                }
            }
            return  result;

        } catch (Exception e){
            return  0;
        }
    }








    public void assginReviewerForScreening(WebDriver driver, RelisUser connectUser){

        if(!connectUser.getUser_usergroup().equals("1")) return;
        // we go to the project phases
        driver.findElement(By.className(ProjectUtils.CLASS_HOME_PROJECT)).click();
        ScreeningView.assign_papers(driver, 1);

    }






    public ScreeningPhase startScreeningPhase(WebDriver driver, Project project){

      String openedPhaseName = screening_controller.setUpCurrentPhase(driver,project);

      return   project.getScreening().getphaseByName(openedPhaseName);


    }


    /**
     * this function will go to the project list web page
     * @param driver web driver
     */
    public static void openProjectListPage(WebDriver driver){

        WebElement menu = driver.findElement(By.className(ProjectUtils.CLASS_SIDEBAR_FOOTER_MENU));
        List<WebElement> links = menu.findElements(By.tagName("a"));
        links.get(0).click();
    }

    private static void addUserToReLis(WebDriver driver, RelisUser user){

        // get the users menu element
        WebElement menu_user = Views.getSideBarMenuOptionsOf(driver,LK_USERS_MENU);
        menu_user.findElement(By.tagName("a")).sendKeys(Keys.ENTER);
        WebElement add_new_user_btn =   driver.findElement(By.cssSelector(CSS_ADD_NEW_USER));
        Views.waitAndClick(driver, add_new_user_btn);

        // fill all the inputs
        driver.findElement(By.name(NAME_USER_FULL_NAME_INPUT)).sendKeys(user.getFull_name());
        driver.findElement(By.name(NAME_USER_NAME_INPUT)).sendKeys(user.getUsername());
        if(user.getUser_email() != null )
            driver.findElement(By.name(NAME_USER_EMAIL_INPUT)).sendKeys(user.getUser_email());
        driver.findElement(By.name(NAME_USER_PASSWORD_INPUT)).sendKeys(user.getPassword());
        driver.findElement(By.name(NAME_USER_PASSWORD_CONFIRMATION)).sendKeys(user.getPassword());
        // choose the user group
        WebElement user_group_elem = driver.findElement(By.className(CLASS_USER_GROUP_ELEMENT));
        user_group_elem.click();
        WebElement ul_elem = driver.findElement(By.id(ID_USER_GROUP_UL));
        int index = Integer.parseInt(user.getUser_usergroup());
        List<WebElement> li  = ul_elem.findElements(By.tagName("li"));
        li.get(index-1).click();

        driver.findElement(By.className(CLASS_UPLOAD_IMPORTED_PAPERS_BUTTON)).click();

    }

    /**
     * this function will add users to ReLis
     * so we can use for the test
     * @param driver web driver
     */
    public static void addUsersToReLis(WebDriver driver){

        ArrayList<RelisUser> users = Utility.getMocksRelisUser();
       // System.out.println("NOUS AVONS " + users.size()+" USERES ");
       openProjectListPage(driver);
       List<RelisUser> data =  users.stream()
                        .filter(u -> !u.getUsername().equals("admin"))
               .collect(Collectors.toList());
        System.out.println("NOUS AVONS " + data.size()+" USERES ");
       for(RelisUser user : data){

           System.out.println("ADDING => " + user);
          ProjectController.addUserToReLis(driver,user);
       }


    }








}
