import controller.*;
import model.*;
import model.relis_parser.RelisParser;
import model.relis_parser.XslTransformer;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import utils.*;
import view.ClassificationView;
import view.QAViews;
import view.ScreeningView;
import view.Views;

import javax.swing.text.View;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;
import static utils.QAUtils.LK_PROGRESS_QA;

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

       XslTransformer.transformToXml();
        project = XslTransformer.project();
        project.getScreening().setReviewersPerPaper(2);
        ConnexionTest(driver);
    }

    @AfterTest
    public void closeDriver(){

        driver.close();
    }

    @AfterMethod
    public void go_to_main_menu(){

        ProjectController.openProjectListPage(driver);
        openProjectTest(driver,project);
    }




    @Test
    public void open_project_test(){



        ProjectController.openProject(driver, project.getProject_name());
    }



    @Test(priority = 1)
    public void assign_screening_reviewers(){
        System.out.println("Debut de 'assign reviewer test'");
        String openedPhaseName = sc.openNextPhase(driver);
        ScreeningPhase phase = project.getScreening().getScreeningphaseByName(openedPhaseName);
        if(phase != null){
            sc.assignReviewers(driver, phase);
        }

        assertTrue(phase == null || phase.getParticipantNumbers() > 0 );
        System.out.println("fin de 'assign reviewer test'");

    }

    @Test(priority = 2)
    public void screen_test(){

        sc.openNextPhase(driver); // opening the screening phase
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


    @Test(priority = 4)
    public void assign_reviewer_qa_test(){

        try {
            QualityAssementController.openQA_page(driver);
            QualityAssementController.assign_roles(driver, project.getQa());
            assertTrue(project.getQa().getNumberOfParticipants() > 0);

        } catch (Exception e ){

            return;
        }



    }


    @Test(priority = 5)
    public void asses_qa_test(){

        // open the qa phase
        try {
            QualityAssementController.openQA_page(driver);
            qa_controller.do_qa_assess(driver, project.getQa());
            boolean no_pending= Views.noPendingPapers(driver);
            assertTrue(no_pending);

        } catch (Exception e ) {
            // if there's no qa phase return!
            return;
        }


    }

    @Test(priority = 6)
    public void validate_qa_test(){

        // open the qa phase
        try {
            QualityAssementController.openQA_page(driver);
            QualityAssementController.validate_qa(driver, project);

        } catch (Exception e ) {
            // if there's no qa phase return!
            return;
        }

    }



    @Test(priority = 8)
    public void assign_classificators_test(){

        classControler.openClassificationPhase(driver);
        classControler.assignClassificators(driver, project.getClassification());
        assertTrue(project.getClassification().getNumber_of_classifier() > 0);
    }

    @Test(priority = 9)
    public void classify_test(){

        classControler.openClassificationPhase(driver);
        classControler.finishClassificationPhase(driver, project);

        assertTrue(true);
    }



    @Test(priority = 10)
    public void validate_classification_test(){

        openProjectTest(driver, project);
        ClassificationView.enable_validation(driver);

        classControler.openClassificationPhase(driver);
        classControler.finishValidationPhase(driver,project.getClassification());
    }

    @Test(priority = 14)
    public void current_test(){
        QualityAssementController.openQA_page(driver);
        RelisUser admin = Utility.getAdminUser();
        QAViews.finish_validation_phase(driver,project,admin);
    }




    @Test(priority = 15)
    public void add_dynamic_val_test(){


        classControler.openClassificationPhase(driver);
        String ref = project.getClassification().getDynamicRefName();
       // ClassificationView.add_dynamic_list_entry(driver, ref, "Today20");
    }

    @Test(priority = 16)
    public void delete_dynamic_val_test(){
        classControler.openClassificationPhase(driver);
        String ref =  project.getClassification().getDynamicRefName();
        String val = project.getClassification().getValueOf(ref);
        if(val !=null)
        ClassificationView.delete_dynamic_entry_val(driver,ref,val);
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