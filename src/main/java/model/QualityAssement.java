package model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import model.user_work.QualityAssementSessionWork;

import java.util.ArrayList;

@AllArgsConstructor
@NoArgsConstructor
@Data

public class QualityAssement {


    private String name ="Quality Assement";

    private double min_score;
    private int score = 0;



    private QuestionAnswesPaper questionAnswesPaper = null;
    private ArrayList<QualityAssementSessionWork> participants= new ArrayList<>();

    private ArrayList<QA_Paper> qa_papers = new ArrayList<>();

    private int numberOfParticipants =1;


    public void initQa_Papers(ArrayList<Paper> papers){

        papers.forEach(paper ->{
            QA_Paper paper_qa = new QA_Paper(paper.getKey());
            paper_qa.setPaper_title(paper.getTitle());
            qa_papers.add(paper_qa);
        });
        takeDecision();
    }



    public String toString(){

        String users = "", papers_info= "";
        for(QualityAssementSessionWork x : participants) users += x+"\n";
        for(QA_Paper y : qa_papers) papers_info += y +" \n";

        return "[\n \t Users => [ \n \t " +users +"\n\t]"
                +"\n Papers => [\n\t" + papers_info +"\n\t]"
                +"\n]";
    }


    public boolean hasParticipant(){

        return participants != null && participants.size() != 0;
    }

    public void setPhaseWorkSession(ArrayList<RelisUser> reviewers) {

        reviewers.forEach(user -> {

            QualityAssementSessionWork qa_session = new QualityAssementSessionWork();
            qa_session.setParticipant(user);
            if(participants == null)
                participants = new ArrayList<>();
            participants.add(qa_session);
        });
    }


    /**
     * set up the qa 
     */
    public void makeReadyQASession(){

        participants.forEach( p -> p.setQualityAssement(this));
    }

    /**
     * this method take all decisions for the qa
     * like choosing random papers to eliminante
     */
    public void takeDecision(){

        // choose a percentange between 1 and 30% of papers to eliminate
        int eliminate_percent = (int) (Math.random()*30);
        ArrayList<QA_Paper> qa_papers_copy = new ArrayList<>();
        int numb = qa_papers.size()*eliminate_percent/100;
        for (int i =0; i  < numb ; i ++){
            int nextIndexToEliminate = (int) Math.floor(Math.random()* qa_papers.size());
            QA_Paper qa_paper = qa_papers.remove(nextIndexToEliminate);
            qa_paper.setEliminated(true);
            qa_papers_copy.add(qa_paper);
        }
        qa_papers_copy.addAll(qa_papers);
        this.qa_papers = qa_papers_copy;

    }


    /**
     * this method will choose a combination of answer of every questions
     * such as the eliminate paper will have a score less than the min_scor
     * and the included paper will have score greather or equal to the min_score
     */
    public void applyDecision(){

        qa_papers.stream()
                .filter(QA_Paper::isEliminated)
                .forEach(paper -> questionAnswesPaper.setRejectedAnswer(paper, min_score));
        qa_papers.stream()
                .filter(p -> !p.isEliminated())
                .forEach( qa_paper -> questionAnswesPaper.acceptPapers(qa_paper, min_score));
    }

    /**
     * this function will start the qa session of every implicated user
     * like opening a new browser connecting ...
     */
    public void startParticipantsQA_session(){
        makeReadyQASession();
        participants.parallelStream().forEach(QualityAssementSessionWork::setUp);
    }

    /**
     * this function will close all thwe browser for
     * every user implicated at the qa phase
     */
    public void closeAllWebDriver(){
        participants.forEach( p -> p.getDriver().quit());
    }


    public void setQuestionsForOA(String[] questions){

        if(questionAnswesPaper == null){
            questionAnswesPaper = new QuestionAnswesPaper();

        }
        for (String question : questions) questionAnswesPaper.addQuestions(question);
    }

    public void addResponse(String answers, double score) {
        if(questionAnswesPaper == null){
            questionAnswesPaper = new QuestionAnswesPaper();

        }
        questionAnswesPaper.addAnswer(answers,score);
    }
}
