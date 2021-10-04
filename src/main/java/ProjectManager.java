import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.Reader;
import java.util.List;
import java.util.function.Predicate;

public class ProjectManager {


    private static String BIBTEX_FILE1 = "src/main/resources/bibtex/my_pub.bib";
    protected File bib_file = new File(BIBTEX_FILE1);

    public void createProject(WebDriver driver, String fileName) {

        // open the file and check if exist
        File file = FileUtils.openFile(fileName);
        // return if the file doesn't exist
        if (file == null) return;
        // push the  create new project button
        driver.findElement(By.linkText(ProjectUtils.LK_ADD_NEW_PROJECT_BUTTON)).click();
        // go to upload mode
        driver.findElement(By.linkText(ProjectUtils.LINK_TEST_UPLOAD_MODE)).click();

        // upload the '.php' file for the new project
        driver.findElement(By.name(ProjectUtils.NAME_CHOOSE_FILE_ELEMENT)).sendKeys(file.getAbsolutePath());
        MainTest.sleep(4);
        // then create the project by clicking the submit button
        driver.findElement(By.cssSelector("button[type='submit']")).click();

    }


    public void openProject(WebDriver driver, String projectName) {
        //TODO a completer !!!
        driver.findElement(By.className("fa-paper-plane")).click();

    }

    /**
     * @param driver
     * @param fileName the file where the bibtex is located
     *                 This procedure implement the function for uploading a paper
     *                 using Bibtex
     */
    public void uploadFromBibTeXPaper(WebDriver driver, String fileName) {

        MainTest.sleep(5);
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


    private void openAllPaper(WebDriver driver) {

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


        // push the import menu
        driver.findElement(By.linkText(PaperUtils.LK_IMPORT_PAPER)).click();
        //choose using Bibtex option
        //driver.findElement(By.linkText(PaperUtils.LK_BIBTEX_IMPORT_MODE)).click();
        // driver.findElement(By.xpath(".//ul/li[2]/ul/li[2]/a")).click();
        new WebDriverWait(driver, 5).until(ExpectedConditions.elementToBeClickable(
                By.linkText(PaperUtils.LK_BIBTEX_IMPORT_MODE))).click();

        // we gotta choose the file and import all the papers from it
        driver.findElement(By.name(PaperUtils.NAME_BIBTEX_FILE_CHOOSE_ELEM)).sendKeys(bib_file.getAbsolutePath());
        driver.findElement(By.className(PaperUtils.CLASS_UPLOAD_IMPORTED_PAPERS_BUTTON)).click();

        // now we  commit the imported papers
        driver.findElement(By.className(PaperUtils.CLASS_UPLOAD_IMPORTED_PAPERS_BUTTON)).click();
        // back to the all papers
        driver.findElement(By.className(PaperUtils.CLASS_BACK_FROM_PAPERS_IMPORT_BUTTON)).click();

    }

    /**
     * adding a reviewer to a project
     *
     * @param driver
     * @param user
     */
    private void addRoleForProject(WebDriver driver, RelisUser user,String role) {

        // go to the users page
        driver.findElement(By.className(ProjectUtils.CLASS_PROJECT_USERS)).click();
        // push the + button
        driver.findElement(By.className(ProjectUtils.CLASS_ADD_USER_BUTTON)).click();
        // now we show all the users
        driver.findElement(By.className(ProjectUtils.CLASS_SHOW_ALL_USERS)).click();
        WebElement users_ul = driver.findElement(By.id(ProjectUtils.ID_RELIS_ALL_USERS));
        // we have the list that contains all the users
        List<WebElement> links = users_ul.findElements(By.tagName("li"));
        // choose the user to assign as a reviewer
        chooseWebElement(links, user.getFull_name());
        // show the  all users role
        driver.findElement(By.id(ProjectUtils.ID_USER_ROLES_OPTIONS)).click();
       // we gotta choose a specific role
        WebElement list_of_roles = driver.findElement(By.id(ProjectUtils.ID_ALL_USER_ROLE_FOR_A_PROJECT));
        List<WebElement> roles = list_of_roles.findElements(By.tagName("li"));
        roles.stream()
                .filter(p -> role.equals(p.getText()))
                        .findFirst().get().click();

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





    /**
     * get the specific user from a project users
     * @param users_data
     * @param user_name
     * @return
     */
    private WebElement getUserWebElement( List<WebElement> users_data, String user_name){


        for(int i=0; i< users_data.size(); i++){
            WebElement user = users_data.get(i);
            System.out.println(user.getText());
            List<WebElement> user_info= user.findElements(By.tagName("td"));

            for (int j = 0; j < user_info.size(); j++)
                if(user_info.get(1).getText().equals(user_name))
                    return user;
        }
        return null;
    }


    /**
     * choose and click a web element if the list item text is equal the the param cond
     * @param data
     * @param cond
     */
    public void chooseWebElement( List<WebElement> data, String cond){

        for (int i = 0; i < data.size(); i++)
        {
            if(data.get(i).getText().equals( cond)){
                data.get(i).click();
                break;
            }
        }

    }



}
