import controller.*;
import model.*;
import model.relis_parser.RelisParser;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import utils.ConnexionUtils;
import utils.Initialiazer;
import utils.ProjectUtils;
import utils.Utility;
import view.QAViews;
import view.ScreeningView;
import view.Views;

import javax.swing.text.View;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

public class ProjectTest {




    private  static WebDriver driver;
    private static Project project;

    private static final String Project2 = "Project 2";
    private static final String project_demo = "Mocel transformation";

    private static final ProjectController projectManager = new ProjectController();
    private static final ConnexionController connexion = new ConnexionController();
    private static final ClassificationController classControler = new ClassificationController();
    private static final QualityAssementController qa_controller= new QualityAssementController();
    private static final ScreeningController sc = new ScreeningController();


    @BeforeTest
    public static void runSetUp(){

        Initialiazer init = new Initialiazer();
        init.init();
        driver = init.getWebDriver();
        project = RelisParser.getProjectFromFiles();
        //project.setProject_name(project_demo);

        ConnexionTest(driver);
        openProjectTest(driver,project);

    }

    @AfterMethod
    public void go_to_main_menu(){

        ProjectController.openProjectListPage(driver);
        openProjectTest(driver,project);
    }






    @Test(priority = 1)
    public void assign_reviewers(){

        // assign reviewers
        ScreeningPhase phase = sc.assignReviewers(driver, project);

        assertTrue(phase== null || phase.getParticipantNumbers() > 0 );

    }

    @Test(priority = 2)
    public void screen_test(){
        ScreeningPhase phase;
        while ((phase = sc.getCurrentScreeningPhase(driver, project.getScreening())) != null){

            sc.doScreenPhase(driver, phase);
            boolean done = Views.noPendingPapers(driver);
            assertTrue(done);
            // check conflicts
            boolean correct = sc.resolveIfConflicts(driver, phase);
            assertTrue(correct);
            ProjectController.openProjectListPage(driver);
            ProjectController.openProject(driver,project.getProject_name());
        }

    }

    //@Test(priority = 3)
    public void validate_test(){


    }
//
//    @Test(priority = 1)
    public void startScreeningTest(){

        ScreeningPhase screeningPhase =  sc.getCurrentScreeningPhase(driver, project.getScreening());

        while ( screeningPhase != null){


            // start the screening phase
            sc.startTheCurrentPhase(driver,project);
            // now the screening phase is finish
            // get the screening result of the website
            String result = ScreeningView.extractScreeningResult(driver);
            // compare with the data that we have here
            boolean correct = screeningPhase.correctResultOfScreeningPhase(result);

            assertTrue(correct);
            // do we have more than one reviewer per paper?
            if(screeningPhase.getParticipantNumbers() > 1){
                // if so we check out if we have conflicts
                // we'll resolve them
                sc.resolveConflict(driver, screeningPhase);
                result = ScreeningView.extractScreeningResult(driver);
                correct = screeningPhase.correctResultOfScreeningPhase(result);
                assertTrue(correct);

            }
            screeningPhase.quitWebBrowser();
            ProjectController.openProjectListPage(driver);
            ProjectController.openProject(driver,project.getProject_name());
            screeningPhase  = sc.getCurrentScreeningPhase(driver, project.getScreening());


        }

    }

  // @Test(priority = 3)
    public void testQA(){

        QualityAssementController.openQA_page(driver);

        QualityAssement assement = project.getQa();
        assement.setNumberOfParticipants(2);
        qa_controller.setUpQualityAssements(driver,assement);
        assement.startParticipantsQA_session();

        assement.closeAllWebDriver();


    }
    @Test(priority = 4)
    public void qa_validation_test(){

        QualityAssementController.openQA_page(driver);
        QAViews.enable_validation(driver);
    }


    // @Test(priority = 4)
    public void classifyPapersTest(){
       classControler.finishClassificationPhase(driver,project.getClassification());
        //classControler.extractDOM_classificationValues(driver,project.getClassification());
        //classControler.finishClassificationPhase(driver,project.getClassification());
    }



    /**
     *  connect a user to relis
     *  for the tests
     * @param driver webdriver
     */

    public static void ConnexionTest(WebDriver driver){

        RelisUser user = Utility.getUserByUserName("admin");

        ConnexionController.connect(driver, user);
        String user_name = driver.findElement(By.className(
                ConnexionUtils.CLASS_CONNECTED_USER_PROFILE_NAME)).getText();

        assertEquals(user_name,user.getFull_name());

    }


    /**
     * open a project
     * @param driver the webdriver
      * @param project the project to open
     */
    public static void openProjectTest(WebDriver driver, Project project){

        ProjectController.openProject(driver, project.getProject_name());
        String title  = driver.findElement(By.cssSelector(ProjectUtils
                .CSS_OPENED_PROJECT_NAME)).getText();

        assertEquals(title, "Project : " +project.getProject_name());

    }



}