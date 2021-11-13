package model;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.HashMap;
@Data
@NoArgsConstructor
public class QA_Paper  {


    private String paper_key;
    private String paper_title;
    private HashMap<String,String> questions_answer_db = new HashMap<>();
    private boolean eliminated= false;

    private String validation_response;
    private String validation_response_note;

    public QA_Paper(String paper_key){
        this.paper_key = paper_key;
    }
    public String getPaperAnswerFor(String question){

        return questions_answer_db.get(question);
    }

    public void addQuestionAnswer(String question, String answer){
        questions_answer_db.put(question,answer);
    }


    public String toString(){
        String qa = questions_answer_db.toString();
        return " [key="+paper_key +", title="+ paper_title +", eliminated="+ eliminated+", qa= "+ qa+"\n]";
    }




}
