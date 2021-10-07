package model;

import controller.ConnexionController;
import controller.ProjectManager;
import databases.PapersDataBase;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.openqa.selenium.*;
import utils.*;
import view.ScreeningView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class Screening {



    private RelisUser current_reviewer = null;


    private ArrayList<String> my_assignments = new ArrayList<>();



    public void setUp(){

        Initialiazer initialiazer = new Initialiazer();
        initialiazer.init();
        current_reviewer.setDriver(initialiazer.getWebDriver());
        ConnexionController connexion = new ConnexionController();
        connexion.connect(current_reviewer.getDriver(), current_reviewer);
        ProjectManager projectManager  = new ProjectManager();
        projectManager.openProject(current_reviewer.getDriver(), "");

    }


    public String getNextDecision(String key){
        boolean includ = new Random().nextBoolean();
        //Paper paper = PapersDataBase.getInstance().getPaper(key);
        if(includ){

            return "TRUE";
        } else {
            return   PapersDataBase.getInstance().nextCriteria();
        }

    }






//    private void showTableItem(WebElement tr){
//        List<WebElement> elements= tr.findElements(By.tagName("td"));
//        elements.forEach(p-> System.out.print(p.getText() +" "));
//        System.out.println();
//
//    }

















}
