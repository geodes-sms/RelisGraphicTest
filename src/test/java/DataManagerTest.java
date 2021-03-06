import controller.ProjectController;
import controller.ScreeningController;
import databases.DataBase;
import model.Project;

import model.RelisUser;
import model.ScreeningPhase;
import model.relis_parser.RelisParser;

import model.relis_parser.XslTransformer;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;

import org.testng.annotations.AfterGroups;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import utils.Initialiazer;
import utils.ProjectUtils;
import utils.Utility;
import view.Views;


import javax.print.attribute.standard.MediaSize;
import java.util.ArrayList;

import static org.testng.Assert.*;


public class DataManagerTest {


    private static WebDriver driver;
    private static ScreeningController sc = new ScreeningController();
    private final ProjectController projectManager = new ProjectController();

    private static Project project;

    @BeforeTest
    public static void runSetUp(){

        Initialiazer init = new Initialiazer();
        init.init();
        driver = init.getWebDriver();

        if(driver == null)
            System.out.println("The driver is null @@@!!");
        XslTransformer.transformToXml();
        project = XslTransformer.project();

        ProjectTest.ConnexionTest(driver);
    }

    @AfterTest
    public void closeDriver(){
        driver.quit();
    }





    @Test
    public void create_project_test(){

        System.out.println("Creating the project");
        projectManager.createProject(driver, project.getProjectId());
        System.out.println("Project created ");
        ProjectController.open_project_all_phases(driver, project.getProject_name());
    }
    @Test(priority = 1)
    public void addUsersToReLisTest(){


        try {
            ProjectController.addUsersToReLis(driver);
        } catch ( Exception  e){
            System.out.println("EXCEPTION DANS  addUsersToReLisTest");
            e.printStackTrace();
        }
    }


    @Test(priority = 2)
    public void open_project_test(){

        ProjectController.openProject(driver, project.getProject_name());
    }
    @Test(priority = 3)
    public void deleteAllPaper(){
        ProjectController.openProject(driver, project.getProject_name());
        projectManager.deleteAllPapers(driver);
        int papersLength = projectManager.getProjectPapersLength(driver);
        assertEquals(papersLength,0);
    }




    @Test(priority = 4)
    public void importBibTexTest(){
        projectManager.importBibTexPapers(driver, ProjectUtils.BIBTEX_FILE1);

        int imported_papers_length = projectManager.getProjectPapersLength(driver);
        assertTrue(imported_papers_length > 0);

    }



    @Test(priority = 5)
    public void deleteProjectPaperByKey(){
        String key = "Syriani2008";
        projectManager.deletePaperByKey(driver,key);

        boolean is_deleted = projectManager.isPresentPaper(driver,key);

        assertFalse(is_deleted);

    }

     @Test(priority = 6)
    public void addVenueTest(){
        projectManager.addVenue(driver,"The world after 2021");

    }
    @Test(priority = 7)
    public void deleteVenueTest(){

        String venue = ProjectController.getA_venue(driver);
        System.out.println("VENUE TO DELETE => " + venue);
        ProjectController.delete_venue(driver, venue);

    }


    //
   @Test(priority = 8)
    public void addReviewerTest(){

        RelisUser reviewer =  Utility.getRandomUser();
        System.out.println("Reviewer choosed " + reviewer);
        projectManager.addReviewer(driver,reviewer);
        boolean is_a_reviewer = ProjectController.isAReviewer(driver, reviewer);
        assertTrue(is_a_reviewer);
        DataBase.getInstance().addReviewer(reviewer);

    }

  @Test(priority = 9)
    public void addProjectManagerTest(){
        RelisUser pm =  Utility.getRandomUser();

        projectManager.addProjectManager(driver,pm);
        boolean projectManager = ProjectController.isAProjectManager(driver, pm);
        assertTrue(projectManager);
        DataBase.getInstance().addProjectManager(pm);
    }

    @Test(priority = 11)
    public void addValidatorTest(){
        RelisUser validator =  Utility.getRandomUser();

        projectManager.addValidator(driver,validator);
        boolean is_a_validator = ProjectController.isAValidator(driver, validator);
        assertTrue(is_a_validator);
        DataBase.getInstance().addValidator(validator);
    }

    @Test(priority = 12)
    public void addGuestRoleTest(){
        RelisUser guest =  Utility.getRandomUser();

        projectManager.addUserAsGuest(driver,guest);
        boolean guestUser = ProjectController.isAGuestUser(driver, guest);
        assertTrue(guestUser);
        DataBase.getInstance().addGuestUser(guest);

    }

    @Test(priority = 13)
    public void removeReviewer(){

        RelisUser reviewer = DataBase.getInstance().GetAReviewer();
        projectManager.removeUserRole(driver,reviewer);
        boolean not_reviewer = !ProjectController.isAReviewer(driver, reviewer);
        assertTrue(not_reviewer);
        DataBase.getInstance().deleteReviewer(reviewer);
    }

    @Test(priority = 14)
    public void removeProjectManager(){

        RelisUser pm = DataBase.getInstance().getAProjectManager();
        projectManager.removeUserRole(driver,pm);
        boolean not_pm = !ProjectController.isAProjectManager(driver, pm);
        assertTrue(not_pm);
        DataBase.getInstance().deleteProjectManager(pm);
    }
    @Test(priority = 15)
    public void removeValidator(){

        RelisUser validator = DataBase.getInstance().getAValidator();
        projectManager.removeUserRole(driver,validator);
        boolean not_validator = !ProjectController.isAValidator(driver, validator);
        assertTrue(not_validator);
        DataBase.getInstance().deleteValidator(validator);
    }

    @Test(priority = 16)
    public void removeGuestUserRole(){

        RelisUser guest = DataBase.getInstance().getAGuestUser();
        projectManager.removeUserRole(driver,guest);
        boolean not_guest = !ProjectController.isAGuestUser(driver, guest);
        assertTrue(not_guest);
        DataBase.getInstance().addGuestUser(guest);
    }


    @Test(priority = 17)
    public  void addSomeReviewers(){
        int i = 0;
        ArrayList<RelisUser> relisUsers = new ArrayList<>();
        Views.getSideBarMenuOptionsOf(driver, "Papers").findElement(By.tagName("a"))
                .click();

        do relisUsers.add(Utility.getRandomUser()); while( i++ < 4);
        relisUsers.forEach( user -> projectManager.addReviewer(driver,user)

        );
        assertTrue(true);
    }



    @Test(priority = 18)
    public void add_author_test(){


        String author_name = "AUTHOR 1";

        ProjectController.add_authors(driver,author_name);
    }
    @Test(priority = 19)
    public void modifyScreeningExclusionCriteria(){

        assertTrue(true);
    }

    @Test(priority = 20)
    public void reconfiguration_project_test(){

        ProjectTest.openProjectTest(driver, project);

        Views.reconfigurate(driver, project);


    }








}
