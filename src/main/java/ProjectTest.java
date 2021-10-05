import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import static org.testng.Assert.*;

public class ProjectTest {

    WebDriver driver;
    private static final ProjectManager projectManager = new ProjectManager();
    @BeforeTest
    public void runSetUp(){

        Initialiazer init = new Initialiazer();
        init.init();
        driver = init.getWebDriver();
    }


    @Test(priority = 1)
    public void ConnexionTest(){

        RelisUser user = Utility.getUserByUserName("admin");
        Connexion connexion = new Connexion();
        connexion.connect(driver, user);
        String user_name = driver.findElement(By.className(
                ConnexionUtils.CLASS_CONNECTED_USER_PROFILE_NAME)).getText();

        assertEquals(user_name,user.getFull_name());

    }

    @Test(priority = 2)
    public void openProjectTest(){

        projectManager.openProject(driver,ProjectUtils.model_transformation_project);
        String title  = driver.findElement(By.cssSelector(ProjectUtils
                .CSS_OPENED_PROJECT_NAME)).getText();
        assertEquals(title, "Project : " +ProjectUtils.model_transformation_project);

    }
    @Test(priority = 3)
    public void importBibTexTest(){
        projectManager.importBibTexPapers(driver,ProjectUtils.BIBTEX_FILE1);

        int imported_papers_length = projectManager.getProjectPapersLength(driver);
        assertEquals(imported_papers_length,98);

    }

    @Test(priority = 4)
    public void deleteProjectPaperByKey(){
        String key = "Syriani2008";
        projectManager.deletePaperByKey(driver,key);

        boolean is_deleted = projectManager.isPresentPaper(driver,key);
        assertFalse(is_deleted);

    }
    @Test(priority = 5)
    public void deleteAllPaper(){

       projectManager.deleteAllPapers(driver);
        int papersLength = projectManager.getProjectPapersLength(driver);
        assertEquals(papersLength,0);

    }
}
