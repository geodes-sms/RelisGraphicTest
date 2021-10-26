package model;

import lombok.Data;

import java.util.ArrayList;
import java.util.HashMap;

@Data
public class QuestionAnswesPaper {


    private ArrayList<String> questions = new ArrayList<>();

    private HashMap<String,Float> answers = new HashMap<>();





    public void addQuestions(String question){
        questions.add(question);
    }

    public void addAnswer(String answer, float score){
        answers.put(answer,score);
    }

    public float getScoreOf(String answer){
        try {
            return answers.get(answer);
        } catch (Exception e){
            return 0.f;
        }

    }


    public void setRejectedAnswer(QA_Paper paper){

        questions.forEach( question ->{

            paper.addQuestionAnswer(question,"No");
        });

    }


    public void acceptPapers(QA_Paper qa_paper) {
        questions.forEach( question ->{

            qa_paper.addQuestionAnswer(question,"Yes");
        });
    }
}
