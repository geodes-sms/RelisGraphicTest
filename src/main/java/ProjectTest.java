import controller.*;
import controller.ScreeningPhaseController;
import databases.DataBase;
import databases.PapersDataBase;
import model.*;

import model.relis_parser.RelisParser;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import utils.*;
import view.ScreeningView;
import view.Views;

import java.util.ArrayList;

import static org.testng.Assert.*;
import static utils.ScreeningUtils.*;


public class ProjectTest {

    WebDriver driver;
    Project project;

    private static final String Project2 = "Project 2";
    private static final String project_demo = "Mocel transformation";

    private static final ProjectController projectManager = new ProjectController();
    private static final  ConnexionController connexion = new ConnexionController();
    private static final ClassificationController classControler = new ClassificationController();
    private static final QualityAssementController qa_controller= new QualityAssementController();
    private static final ScreeningController sc = new ScreeningController();


    @BeforeTest
    public void runSetUp(){

        Initialiazer init = new Initialiazer();
        init.init();
        driver = init.getWebDriver();
        project = RelisParser.getProjectFromFiles();
        //project.setProject_name(project_demo);

    }


  @Test(priority = 1)
    public void ConnexionTest(){

        RelisUser user = Utility.getUserByUserName("admin");
        ConnexionController.connect(driver, user);
        String user_name = driver.findElement(By.className(
                ConnexionUtils.CLASS_CONNECTED_USER_PROFILE_NAME)).getText();

        assertEquals(user_name,user.getFull_name());

    }

   @Test(priority = 2)
    public void openProjectTest(){

        ProjectController.openProject(driver, project.getProject_name());
        String title  = driver.findElement(By.cssSelector(ProjectUtils
                .CSS_OPENED_PROJECT_NAME)).getText();

        assertEquals(title, "Project : " +project.getProject_name());

    }
   //@Test(priority = 3)
    public void deleteAllPaper(){

        projectManager.deleteAllPapers(driver);
        int papersLength = projectManager.getProjectPapersLength(driver);
        assertEquals(papersLength,0);
    }

   // @Test(priority = 3)
    public void addVenueTest(){
        projectManager.addVenue(driver,"The world after covid19");
        assertTrue(true);
    }
    //@Test(priority = 3)
    public void deleteAllUserRole(){

       // connexion.disConnect(driver,Utility.getCurrentConnectedUser(driver));
        projectManager.removeAllProjectUsers(driver);
       // connexion.connect(driver,connected);
       // driver.quit();
        assertTrue(true);
    }



   //@Test(priority = 4)
    public void deleteProjectPaperByKey(){
        String key = "Syriani2008";
        projectManager.deletePaperByKey(driver,key);

        boolean is_deleted = projectManager.isPresentPaper(driver,key);
        assertFalse(is_deleted);

    }
 // @Test(priority = 5)
    public void importBibTexTest(){
        projectManager.importBibTexPapers(driver,ProjectUtils.BIBTEX_FILE1);

        int imported_papers_length = projectManager.getProjectPapersLength(driver);
        assertEquals(imported_papers_length,98);

    }

//
//    @Test(priority = 7)
//    public void addReviewerTest(){
//
//        RelisUser reviewer =  Utility.getRandomUser();
//        System.out.println("Reviewer choosed " + reviewer);
//        projectManager.addReviewer(driver,reviewer);
//        boolean is_a_reviewer = ProjectController.isAReviewer(driver, reviewer);
//        assertTrue(is_a_reviewer);
//        DataBase.getInstance().addReviewer(reviewer);
//
//    }
//
//   @Test(priority = 8)
//    public void addProjectManagerTest(){
//        RelisUser pm =  Utility.getRandomUser();
//
//        projectManager.addProjectManager(driver,pm);
//        boolean projectManager = ProjectController.isAProjectManager(driver, pm);
//        assertTrue(projectManager);
//        DataBase.getInstance().addProjectManager(pm);
//    }
//
//    @Test(priority = 9)
//    public void addValidatorTest(){
//        RelisUser validator =  Utility.getRandomUser();
//
//        projectManager.addValidator(driver,validator);
//        boolean is_a_validator = ProjectController.isAValidator(driver, validator);
//        assertTrue(is_a_validator);
//        DataBase.getInstance().addValidator(validator);
//    }
//
//    @Test(priority = 10)
//    public void addGuestRoleTest(){
//        RelisUser guest =  Utility.getRandomUser();
//
//        projectManager.addUserAsGuest(driver,guest);
//        boolean guestUser = ProjectController.isAGuestUser(driver, guest);
//        assertTrue(guestUser);
//        DataBase.getInstance().addGuestUser(guest);
//
//    }
//
//    @Test(priority = 11)
//    public void removeReviewer(){
//
//        RelisUser reviewer = DataBase.getInstance().GetAReviewer();
//        projectManager.removeUserRole(driver,reviewer);
//        boolean not_reviewer = !ProjectController.isAReviewer(driver, reviewer);
//        assertTrue(not_reviewer);
//        DataBase.getInstance().deleteReviewer(reviewer);
//    }
//
//    @Test(priority = 12)
//    public void removeProjectManager(){
//
//        RelisUser pm = DataBase.getInstance().getAProjectManager();
//        projectManager.removeUserRole(driver,pm);
//        boolean not_pm = !ProjectController.isAProjectManager(driver, pm);
//        assertTrue(not_pm);
//        DataBase.getInstance().deleteProjectManager(pm);
//    }
//    @Test(priority = 13)
//    public void removeValidator(){
//
//        RelisUser validator = DataBase.getInstance().getAValidator();
//        projectManager.removeUserRole(driver,validator);
//        boolean not_validator = !ProjectController.isAValidator(driver, validator);
//        assertTrue(not_validator);
//        DataBase.getInstance().deleteValidator(validator);
//    }
//    @Test(priority = 14)
//    public void removeGuestUserRole(){
//
//        RelisUser guest = DataBase.getInstance().getAGuestUser();
//        projectManager.removeUserRole(driver,guest);
//        boolean not_guest = !ProjectController.isAGuestUser(driver, guest);
//        assertTrue(not_guest);
//        DataBase.getInstance().addGuestUser(guest);
//    }
//

   //@Test(priority = 15)
    private void addSomeReviewers(){
        int i = 0;
        ArrayList<RelisUser> relisUsers = new ArrayList<>();
        do relisUsers.add(Utility.getRandomUser()); while( i++ < 4);
        relisUsers.forEach(
                user-> projectManager.addReviewer(driver,user)
        );
        assertTrue(true);
    }
    //  @Test(priority = 16)
    public void assignReviewers(){


      ScreeningPhase phase = sc.assignReviewers(driver,project);
      phase.printInfo();
      assertTrue(true);

    }



  @Test(priority = 17)
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
            if(screeningPhase.getParticipantNumbers() > 1){
                sc.resolveConflict(driver, screeningPhase);
                result = ScreeningView.extractScreeningResult(driver);
                correct = screeningPhase.correctResultOfScreeningPhase(result);
                assertTrue(correct);

            }
            screeningPhase.quitWebBrowser();
            projectManager.openProjectListPage(driver);
            ProjectController.openProject(driver,project.getProject_name());
           screeningPhase  = sc.getCurrentScreeningPhase(driver, project.getScreening());


        }

    }

     @Test(priority = 20)
    public void testQA(){

        QualityAssementController.openQA_page(driver);
        QualityAssement assement = new QualityAssement();
        assement.setNumberOfParticipants(3);
        qa_controller.setUpQualityAssements(driver,assement);
        assement.startParticipantsQA_session();

        assement.closeAllWebDriver();


    }
    // @Test(priority = 21)
    public void classifyPapersTest(){
       // classControler.finishClassificationPhase(driver,project.getClassification());
        //classControler.extractDOM_classificationValues(driver,project.getClassification());
        //classControler.finishClassificationPhase(driver,project.getClassification());
    }

  //  @Test(priority = 22)
    public void validateClassificationTest(){

        classControler.finishValidationPhase(driver,project.getClassification());

    }

//    @AfterTest
//    public void deleteAllUserRole(){
//        RelisUser connected = Utility.getCurrentConnectedUser(driver);
//        connexion.disConnect(driver,Utility.getCurrentConnectedUser(driver));
//        projectManager.removeAllProjectUsers(driver);
//        connexion.connect(driver,connected);
//        driver.quit();
//    }
   //@Test(priority = 22)
    public void Next(){
//
//      screening_controller.openCurrentScreeningPhase(driver);
//      PapersDataBase.getInstance().resolveAllConflicts();
//      screening_controller.resolveConflict(driver);
//        assertTrue(true);

        projectManager.assginReviewerForScreening(driver,Utility.getCurrentConnectedUser(driver));
    }


    //@Test(priority = 24)
    public void tester(){

//        ArrayList<Paper> papers = new ArrayList<>();
//        ProjectController.openAllPaper(driver);
//        Utility.getAllPaperFromTable_id(driver,papers);
//        System.out.println("papers size=" + papers.size());

    }






}
