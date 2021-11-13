package model.user_work;

import controller.ConnexionController;
import controller.ProjectController;
import controller.QualityAssementController;
import lombok.Data;
import model.QA_Paper;
import model.QualityAssement;
import model.RelisUser;
import org.openqa.selenium.WebDriver;
import utils.Initialiazer;
import view.QAViews;
import view.Views;

import java.util.ArrayList;

@Data
public class QualityAssementSessionWork {

    private WebDriver driver;
    private RelisUser participant;
    QualityAssement qualityAssement;

    // the users assignement arrays
    private ArrayList<QA_Paper> qa_papers = new ArrayList<>();

    public void setUp(){

        Initialiazer initialiazer = new Initialiazer();
        initialiazer.init();
        driver = initialiazer.getWebDriver();
        ConnexionController.connect(driver, participant);
        ProjectController.openProject(driver, "Project 2");
        QualityAssementController.openQA_page(driver);
        // get papers assigned to the current user
        retrievePapers();
        // start the qa phase
        runQA();
    }



    public String toString(){

        String qa = qa_papers.toString();
        return "participant => " + participant +", paper_Qa =>" + qa;
    }

    /**
     * this function get every user paper for the QA phase
     *
     */
    public void retrievePapers(){

         qa_papers  = QAViews.extractParticipantPapersTitle(driver,qualityAssement);
    }



    public String getAnswerForQuestion(String key, String question){

        return "No answer yet!";
    }

    /**
     *
     * @param key the paper key
     * @return return qa paper
     */
    public QA_Paper getQA_result(String key){

        return     qa_papers.stream()
                    .filter(p-> p.getPaper_key().equals(key))
                    .findFirst()
                    .orElse(null);

    }

    /**
     *
     * @param question the question
     * @param title the title of a paper
     * @return the answer of question
     */
   public String getAnswerFor(String question, String title){

        QA_Paper paper_qa = qa_papers.stream()
                .filter(paper-> paper.getPaper_title().equals(title))
                .findFirst()
                .orElse(null);
        assert  paper_qa != null;
        return paper_qa.getPaperAnswerFor(question);


   }

   public void runQA(){
        QualityAssementController.startQA_phase(driver,this);

   }


}
