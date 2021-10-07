package controller;

import model.RelisUser;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import utils.ConnexionUtils;
import utils.Utility;

public class ConnexionController {



    // LOG IN USERNAME AND PASSWORD
    private static final String MOCK_USER_NAME = "youssouf1";
    private static final String MOCK_USER_PASS = "azerty10";


    /**
     *
     *
     * @param driver the current tabs
     * @param user the user that wanna connect!
     *
     */
    public void connect(WebDriver driver, RelisUser user) {

        // on navigue vers la page de login
        driver.findElement(By.className(ConnexionUtils.CONNECT_INIT_PATH)).click();
        // on remplie le champs username
        driver.findElement(By.name(ConnexionUtils.name_userNameField)).sendKeys(user.getUsername());
        // remplir le champs du mot de passe
        driver.findElement(By.name(ConnexionUtils.name_userPassField)).sendKeys(user.getPassword());

        // on se connecter
        driver.findElement(By.className(ConnexionUtils.class_LogInButton)).click();
        Utility.sleep(3);

    }

    /**
     *
     * @param driver the current web tabs
     * @param user the user who wanna sign up
     */
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
        Utility.sleep(4);
        // submit the user information and proceed the registration
        driver.findElement(By.className(ConnexionUtils.class_LogInButton)).click();
    }


    /**
     * run the different connexion test
     * 1 . when is connected
     * 2. when is log out
     * 3. when new user sign up
     *
     * @param driver
     */
    public void runLogInTest(WebDriver driver){
        // TODO :: RUN TEST HERE

    }



}
