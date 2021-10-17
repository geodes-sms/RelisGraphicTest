import controller.*;
import controller.ScreeningPhaseController;
import databases.DataBase;
import databases.PapersDataBase;
import model.*;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import utils.*;
import view.ScreeningView;

import java.util.ArrayList;

import static org.testng.Assert.*;



public class ProjectTest {

    WebDriver driver;
    Project project;

    private static final ProjectController projectManager = new ProjectController();
    private static final  ConnexionController connexion = new ConnexionController();
    private static final ClassificationController classControler = new ClassificationController();
    //private static final ScreeningPhaseController screening_controller= new ScreeningPhaseController();
    private static final ScreeningController sc = new ScreeningController();


    @BeforeTest
    public void runSetUp(){

        Initialiazer init = new Initialiazer();
        init.init();
        driver = init.getWebDriver();
        project = new Project();
        project.setProject_name("Model Transformation");
        Screening screening = new Screening();
        Classification classification = new Classification();
        project.setClassification(classification);

        project.setScreening(screening);

    }


  @Test(priority = 1)
    public void ConnexionTest(){

        RelisUser user = Utility.getUserByUserName("youssouf1");
        connexion.connect(driver, user);
        String user_name = driver.findElement(By.className(
                ConnexionUtils.CLASS_CONNECTED_USER_PROFILE_NAME)).getText();

        assertEquals(user_name,user.getFull_name());

    }

    @Test(priority = 2)
    public void openProjectTest(){

        projectManager.openProject(driver, ProjectUtils.model_transformation_project);
        String title  = driver.findElement(By.cssSelector(ProjectUtils
                .CSS_OPENED_PROJECT_NAME)).getText();

        sc.getScreeningPhaseData(driver,project.getScreening());
        assertEquals(title, "Project : " +ProjectUtils.model_transformation_project);


    }
    //@Test(priority = 3)
    public void deleteAllPaper(){

        projectManager.deleteAllPapers(driver);
        int papersLength = projectManager.getProjectPapersLength(driver);
        assertEquals(papersLength,0);
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
    //@Test(priority = 6)
    public void setDaya() throws CloneNotSupportedException {

        ArrayList<Paper> p = ProjectController.getAllPapers(driver);
        PapersDataBase.getInstance().setMockCriteria(); // TODO criteria extraction

        //PapersDataBase.getInstance().setData(p);
        assertTrue(true);

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

  // @Test(priority = 15)
    private void addSomeReviewers(){
        int i = 0;
        ArrayList<RelisUser> relisUsers = new ArrayList<>();
        do relisUsers.add(Utility.getRandomUser()); while( i++ < 4);
        relisUsers.forEach(
                user-> projectManager.addReviewer(driver,user)
        );
        assertTrue(true);
    }
   //@Test(priority = 16)
    public void assignReviewers(){


      ScreeningPhase phase = sc.assignReviewers(driver,project);
      phase.printInfo();
      assertTrue(true);




    }



   //@Test(priority = 17)
    public void startScreening(){

        ScreeningPhase screeningPhase  = projectManager.startScreeningPhase(driver, project);

        while (screeningPhase != null){

            // start the screening phase
            screeningPhase.startThisPhaseScreening();
            // now the screening phase is finish
            // get the screening result of the website
            String result = ScreeningView.extractScreeningResult(driver);
            // compare with the data that we have here
            boolean correct = screeningPhase.correctResultOfScreeningPhase(result);

            assertTrue(correct);

            sc.resolveConflict(driver, screeningPhase);
            result = ScreeningView.extractScreeningResult(driver);
            correct = screeningPhase.correctResultOfScreeningPhase(result);
            assertTrue(correct);
            projectManager.openProjectListPage(driver);
            projectManager.openProject(driver,project.getProject_name());
            screeningPhase  = projectManager.startScreeningPhase(driver, project);
        };




    }


//    @AfterTest
//    public void deleteAllUserRole(){
//        RelisUser connected = Utility.getCurrentConnectedUser(driver);
//        connexion.disConnect(driver,Utility.getCurrentConnectedUser(driver));
//        projectManager.removeAllProjectUsers(driver);
//        connexion.connect(driver,connected);
//        driver.quit();
//    }
  // @Test(priority = 18)
    public void Next(){
//
//      screening_controller.openCurrentScreeningPhase(driver);
//      PapersDataBase.getInstance().resolveAllConflicts();
//      screening_controller.resolveConflict(driver);
//        assertTrue(true);
    }

    @Test(priority = 20)
    public void test(){

        classControler.openClassificationPhase(driver);
        classControler.setUpAClassification(driver,project.getClassification());

        classControler.finishValidationPhase(driver,project.getClassification());
       //driver.quit();

    }


}
