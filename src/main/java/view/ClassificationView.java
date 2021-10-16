package view;

import jdk.jshell.execution.Util;
import model.Classification;
import model.Paper;
import model.QualityAssement;
import model.RelisUser;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import utils.*;

import java.util.ArrayList;
import java.util.List;

public class ClassificationView {


    public void openClassification(WebDriver driver){

        driver.findElement(By.linkText(ProjectUtils.LK_CURRENT_PROJECT)).click();
        driver.findElement(By.className(ProjectUtils.CLASS_HOME_PROJECT)).click();

        openClassificationPhase(driver, ClassificationUtils.CLASSIFICATION_NAME);
    }

    /**
     * open the classification phase
     * @param driver the web driver
     * @param phaseName the word classification
     */
    private void openClassificationPhase(WebDriver driver, String phaseName){

        // select the table that contains the screening phaseas
        WebElement table = driver.findElement(By.className(ScreeningUtils.CLASS_SCREENING_PHASES_TABLE));

        System.out.println(table.getText());
        // get all the systematic review phases
        List<WebElement> trs = table.findElements(By.tagName("tr"));
        trs.remove(0);
        // we remove the first web element which is the table header

        for(WebElement element : trs){

            List<WebElement> tds = element.findElements(By.tagName("td"));

            WebElement phase = tds.get(0);
            if((phase !=null) && phase.getText().equals(phaseName)){
                WebElement gotoPhae;

                try{
                    gotoPhae = tds.get(4).
                            findElement(By.cssSelector(ClassificationUtils.CSS_GOTO_CLASSIFICATION));
                    gotoPhae.sendKeys(Keys.ENTER);
                } catch (Exception e){
                    element.findElement(By.cssSelector(ClassificationUtils.CSS_UNLOCK_CLASSIFICATION))
                            .sendKeys(Keys.ENTER);
                    openClassificationPhase(driver, phaseName);
                }

                return;
            }
        }
    }


    /**
     * open the 'my pending assignments' page
     * @param driver the web driver
     */
    public void openMyPendingPapersToClassify(WebDriver driver){

        driver.findElement(By.linkText(ScreeningUtils.LK_DASHBORD_LINK)).click();
        try{

            driver.findElement(By.linkText(ClassificationUtils.LK_MY_PENDING_PAPERS_TO_CLASSIFY));
        } catch (Exception e){

            driver.findElement(By.linkText(ClassificationUtils.LK_CLASSIFY_MENU)).click();
        }

        new WebDriverWait(driver,
                4).until(ExpectedConditions.presenceOfElementLocated(
                By.linkText(ClassificationUtils.LK_MY_PENDING_PAPERS_TO_CLASSIFY)
        )).sendKeys(Keys.ENTER);
        Utility.sleep(5);
        System.out.println("Opened user pending classification");


    }


    /**
     * Open the all assignments papers to classify papge
     * @param driver the web driver
     */
    public void openAllPaperToCLassifyPage(WebDriver driver){

        driver.findElement(By.linkText(ScreeningUtils.LK_DASHBORD_LINK)).click();
        driver.findElement(By.linkText(ClassificationUtils.LK_CLASSIFY_MENU)).click();
        new WebDriverWait(driver,
                4).until(ExpectedConditions.presenceOfElementLocated(
                By.linkText(ClassificationUtils.LK_ALL_PAPERS_TO_CLASSIFY)
        )).sendKeys(Keys.ENTER);

    }

    public ArrayList<Paper> getUserPapersToCLassify(WebDriver driver){


        openMyPendingPapersToClassify(driver);
        WebElement table = driver.findElement(By.id(ProjectUtils.ID_PROJECT_TABLE_USERS));
        return getAllPapersFrom(driver, table);
    }


    private static ArrayList<Paper> getAllPapersFrom(WebDriver driver, WebElement table){


        WebElement element;
        ArrayList<Paper> assigments= new ArrayList<>();
        while (true){
            try{
                // get all the papers present from the current table
                List<WebElement> papers = table.findElements(By.tagName("tr"));
                // we remove the first web element which is the table header
                papers.remove(0);
                papers.forEach(paper ->{
                    Paper paper_key = extracPaperFrom(paper);
                    assigments.add(paper_key);

                });
                // get web element for the next click link
                element = driver.findElement(By.id(ProjectUtils.ID_NEXT_PAPERS_PAGE));
                // there is no next table  ?
                if(Utility.hasClass(element,"disabled")) break;
                element.findElement(By.linkText("Next")).click();
            } catch (Exception e){
                e.printStackTrace();
                break;

            }
        }
        return assigments;
    }

    /**
     * this method open the classification phase
     * @param driver the web driver
     */
    public void openAssignClassificatorPage(WebDriver driver){

        driver.findElement(By.linkText(ScreeningUtils.LK_DASHBORD_LINK)).click();
        driver.findElement(By.cssSelector(ClassificationUtils.CSS_ASSIGN_CLASSIFICATORS_PAGE))
                .sendKeys(Keys.ENTER);

    }

    /**
     * this method assigns the user who participate in the classifaction phase
     * or if it's alredy assigned, it return the assigned users
     * @param driver the web driver
     * @param classification the classificator object
     */
    public void assign_classificator(WebDriver driver, Classification classification){

        openAssignClassificatorPage(driver);

        WebElement rest_papers = driver.findElement(By.cssSelector("#home b"));
        if(rest_papers.getText().equals("Number of papers to assign :0")){
             getClassificator(driver, classification);
             return;
        }
        // we select some random users
        ArrayList<RelisUser> reviewers = Views.chooseUserforScreening(
                driver,classification.getNumber_of_classifier());
        // we submit the assaignment
        driver.findElement(By.className(ScreeningUtils.CLASS_SUCCESS_BUTTON)).click();
        classification.setClassificators(reviewers);



    }

    /**
     * this method get the users who were assign for the classification phase
     * @param driver the web driver
     * @param classification the classificatin objet
     */
    private void getClassificator(WebDriver driver, Classification classification) {
        showProgressBarForClassificators(driver);
        // we assign as classificators
        classification.setClassificators(Views.extractUsers(driver));

    }


    /**
     * open the progress bar page of every classificator
     * @param driver the webdriver
     */
    public void showProgressBarForClassificators(WebDriver driver){

        driver.findElement(By.linkText(ScreeningUtils.LK_DASHBORD_LINK)).click();
        try{

            driver.findElement(By.linkText(ClassificationUtils.LK_CLASSIFICATION_PROGRESS));
        } catch (Exception e){

            driver.findElement(By.linkText(ClassificationUtils.LK_CLASSIFY_MENU)).click();
        }

        new WebDriverWait(driver,
                2).until(ExpectedConditions.presenceOfElementLocated(
                By.linkText(ClassificationUtils.LK_CLASSIFICATION_PROGRESS)
        )).sendKeys(Keys.ENTER);
    }



































    /**************************************************************************************************
     *
     *
     *
     *
     *
     ****************************************************************************************************/
    private static Paper extracPaperFrom(WebElement trs) {

        Paper paper1 = new Paper();

        List<WebElement> tds = trs.findElements(By.tagName("td"));

        String key_title = tds.get(0).getText();

        String key = key_title.substring(0, key_title.indexOf(" "));
        String title = key_title.substring(key_title.indexOf(" - ")+3);
        paper1.setTitle(title);
        paper1.setKey(key);
        return paper1;
    }

}


