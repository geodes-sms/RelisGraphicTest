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

public class ProjectManager {


    private static String BIBTEX_FILE1 = "src/main/resources/bibtex/my_pub.bib";
    protected File bib_file = new File(BIBTEX_FILE1);
    public void createProject(WebDriver driver, String fileName){

        // open the file and check if exist
        File file = FileUtils.openFile(fileName);
        // return if the file doesn't exist
        if(file == null) return;
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


    public void openProject(WebDriver driver, String projectName){
        //TODO a completer !!!
        driver.findElement(By.className("fa-paper-plane")).click();

    }

    /**
     *
     * @param driver
     * @param fileName the file where the bibtex is located
     * This procedure implement the function for uploading a paper
     * using Bibtex
     *
     */
    public  void uploadFromBibTeXPaper(WebDriver driver, String fileName){

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


    private void openAllPaper(WebDriver driver){

        // go to the paper
        driver.findElement(By.className(PaperUtils.CLASS_NAME_OPEN_PAPER)).click();
        // go to all paper
        driver.findElement(By.linkText(PaperUtils.LINK_TEXT_ALL_PAPER)).click();

    }


    public void importBibTexPapers(WebDriver driver, String fileName){


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

    public void addReviewer( WebDriver driver, RelisUser user){

        // go to the users page
        driver.findElement(By.className(ProjectUtils.CLASS_PROJECT_USERS)).click();
        // push the + button
        driver.findElement(By.className(ProjectUtils.CLASS_ADD_USER_BUTTON)).click();
        // now we show all the users
        driver.findElement(By.className(ProjectUtils.CLASS_SHOW_ALL_USERS)).click();
        WebElement users_ul = driver.findElement(By.id(ProjectUtils.ID_RELIS_ALL_USERS));
        List<WebElement> links = users_ul.findElements(By.tagName("li"));
        for (int i = 1; i < links.size(); i++)
        {
            System.out.println("USER: -> " + links.get(i).getText());
            System.out.println("USER: -> " + links.get(0).getText());
        }

    }



}
