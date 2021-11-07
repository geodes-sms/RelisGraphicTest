package model;

import lombok.Data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Optional;

@Data
public class QuestionAnswesPaper {

    // array of questions
    private ArrayList<String> questions = new ArrayList<>();
    // array of answers
    private HashMap<String,Double> answers = new HashMap<>();



    public void addQuestions(String question){
        questions.add(question);
    }

    public void addAnswer(String answer, double score){
        answers.put(answer,score);
    }

    public double getScoreOf(String answer){
        try {
            return answers.get(answer);
        } catch (Exception e){
            return 0.f;
        }

    }


    /**
     * this function take paper as a parameter and
     * chooses some random answers such as the
     * sum of the score is less than the minimum score
     * @param paper paper
     */
    public void setRejectedAnswer(QA_Paper paper){

        System.out.println("trying the best thing in Canada so i can be more valuable");
        questions.forEach( question ->{

            paper.addQuestionAnswer(question,"No");
        });

    }



//    private String getRandomAnswer(){
//
//        double x =0;
//        String response  = "";
////.

////        do{
////            response =
////
////        } while (x <= 3.5);
//    }
    public void acceptPapers(QA_Paper qa_paper) {

        String answer = "";

        questions.forEach( question ->{

            qa_paper.addQuestionAnswer(question,answer);
        });
    }
}
