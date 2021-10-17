package view;

import controller.ProjectController;
import model.Paper;
import model.RelisUser;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import utils.*;

import java.util.*;
import java.util.function.BiFunction;

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


    public static void clickLiWebElement(WebElement ul, String name){
        Utility.sleep(1);
        for (WebElement li : ul.findElements(By.tagName("li"))){
            if(li.getText().equals(name)){
                li.findElement(By.tagName("a")).sendKeys(Keys.ENTER);
            }

        }
    }


    public static WebElement work_through_table(WebDriver driver, BiFunction<List<WebElement>,String,WebElement>
            action, String params){

        try {
            // select the table that contains the papers
            WebElement table = driver.findElement(By.id(ProjectUtils.ID_PROJECT_TABLE_USERS));

            WebElement element ;
            WebElement result = null;

            while (true){
                try{
                    // get web element for the next click link
                    element = driver.findElement(By.id(ProjectUtils.ID_NEXT_PAPERS_PAGE));
                    // get all the papers present from the current table
                    List<WebElement> other_papers = table.findElements(By.tagName("tr"));
                    // we remove the first web element which is the table header
                    other_papers.remove(0);

                    result = action.apply(other_papers,params);
                    if( result != null) return result;
                    // there is no next table  ?
                    if(Utility.hasClass(element,"disabled")) break;
                    element.findElement(By.linkText("Next")).click();
                } catch (Exception e){
                    return null;
                }
            }
            return  result;

        } catch (Exception e){
            return  null;
        }


    }



}
