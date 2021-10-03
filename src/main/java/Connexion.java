import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class Connexion {




    // LOG IN USERNAME AND PASSWORD
    private static final String MOCK_USER_NAME = "youssouf1";
    private static final String MOCK_USER_PASS = "azerty10";



    public void connect(WebDriver driver, RelisUser user) {

        // on navigue vers la page de login
        driver.findElement(By.className(ConnexionUtils.CONNECT_INIT_PATH)).click();
        // on remplie le champs username
        driver.findElement(By.name(ConnexionUtils.name_userNameField)).sendKeys(user.getUsername());
        // remplir le champs du mot de passe
        driver.findElement(By.name(ConnexionUtils.name_userPassField)).sendKeys(user.getPassword());

        MainTest.sleep(4);

        // on se connecter
        driver.findElement(By.className(ConnexionUtils.class_LogInButton)).click();

        MainTest.sleep(3);

    }

    public void registerUser(WebDriver driver, RelisUser user){
        // first go the sign up page
        driver.findElement(By.className(ConnexionUtils.REGISTER_INIT_PATH)).click();
        // fill the all the input fields
        driver.findElement(By.name(ConnexionUtils.name_userName)).sendKeys(user.getFull_name());
        driver.findElement(By.name(ConnexionUtils.name_userNameField)).sendKeys(user.getUsername());
        driver.findElement(By.name(ConnexionUtils.name_userEmail)).sendKeys(user.getUser_email());
        driver.findElement(By.name(ConnexionUtils.name_userPassField)).sendKeys(user.getPassword());
        driver.findElement(By.name(ConnexionUtils.name_validate_pass)).sendKeys(user.getPassword());
        driver.findElement(By.className(ConnexionUtils.class_Captcha_Check_Box)).click();
        // wait for 4 seconds
        MainTest.sleep(4);
        // submit the user information and proceed the registration
        driver.findElement(By.className(ConnexionUtils.class_LogInButton)).click();

    }


    public void runLogInTest(WebDriver driver){
        // TODO :: RUN TEST HERE

    }



}
