package view;

import model.RelisUser;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import utils.Utility;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class Views {


    /**
     * this fonction chooses random users
     * for the screening phase or the classification phase
     * @param driver the web driver
     * @param number the number of user that we wanna choose
     *   if  number is greather than the user of the project
     *   we choose all the project users
     * @return an array of all the choosed users
     */
    public   static ArrayList<RelisUser> chooseUserforScreening(WebDriver driver, int number){

        Random random = new Random();
        ArrayList<RelisUser> reviewers = new ArrayList<>();

        // list of every users web element
        List<WebElement> users = driver.findElements(By.className("form-group"));
        // we remove the first two elements which is not important
        users.remove( users.size()-1);
        users.remove(users.size()-1);
        // we don't have enough user to choose ?
        if(users.size() <= number){ // if so we choose all the users
            users.forEach( elem ->
                    {
                        RelisUser user  = Utility.getUserByFullName(elem.getText());
                        reviewers.add(user);
                        elem.findElement(By.tagName("span")).click();
                    }
            );
        // otherwise we choose n users
        } else{

            do{
                // choose a random user
                int size = users.size();
                int i = random.nextInt(0,size);
                WebElement userWebELment = users.remove(i);
                RelisUser user  = Utility.getUserByFullName(userWebELment.getText());
                reviewers.add(user);
                userWebELment.findElement(By.tagName("span")).click();
            } while (reviewers.size() < number);
        }

        return reviewers;
    }


    /**
     *
     * @param driver
     * @return
     */
    public static ArrayList<RelisUser> extractUsers(WebDriver driver){
        WebElement element =  driver.findElement(By.className("x_content"));
        ArrayList<String> userName= new ArrayList<>(Arrays.asList(element.getText().split("\n")));
        userName.remove(0);
        userName.remove(userName.size()-1);
        ArrayList<RelisUser> reviewer = new ArrayList<>();
        userName.stream()
                .map(p -> {
                    int i = p.indexOf(":");
                    return p.substring(0,i);
                })
                .forEach(full_name
                        -> reviewer.add(Utility.getUserByFullName(full_name))
                );
        return reviewer;
    }



}
