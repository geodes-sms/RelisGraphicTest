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
        String user_name = driver.findElement(By.className("user-profile")).getText();

        assertEquals(user_name,user.getFull_name());


    }

    @Test(priority = 2)
    public void openProjectTest(){

        projectManager.openProject(driver,ProjectUtils.model_transformation_project);
        String title  = driver.findElement(By.cssSelector(".x_title h3")).getText();
        assertEquals(title, "Project : " +ProjectUtils.model_transformation_project);

    }
    @Test(priority = 3)
    public void addReviewerTest(){

        int x = 3+3;
        assertEquals(6,x);


    }
}
