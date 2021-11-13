package model;

import lombok.Data;
import utils.Utility;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.function.Predicate;


@Data
public class QuestionAnswesPaper {

    // array of questions
    private ArrayList<String> questions = new ArrayList<>();
    // array of answers
    private HashMap<String,Double> answers = new HashMap<>();
    private double min_score;

    // predicate for papers to eliminate for qa phase
    private Predicate<Double> is_eliminate = current_score -> current_score < min_score;
    // predicate for papers to include
    private Predicate<Double> is_included = score ->   score >= min_score || min_score > score;

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
    public void setRejectedAnswer(QA_Paper paper, double min_score){
        this.min_score = min_score;
        double score = getRandomAnswer(is_eliminate, paper);
        System.out.println("score =>" + score);

    }



    private double getRandomAnswer(Predicate<Double> predicate, QA_Paper qa_paper){

        double x =0;
        String response  = "";
        // get all the answers
        ArrayList<String> options = new ArrayList<>(answers.keySet());
        System.out.println("MIN SCORE = "  );
        System.out.println("question + " + questions.size());
        questions.forEach( p -> System.out.println(p));
        answers.forEach( (k, v) -> System.out.println(" key=" + k +" value=" +v));
        // choose a response for a question
        for (String question : questions){

//            System.out.println("Question :=>" + question);

            // choose a answer
            do{
                int i = Utility.nextInt(0, options.size());
                System.out.println("i = " + i );
                System.out.println("response=" + options.get(i));
                System.out.println("x=" + x +", new socre=" + answers.get(options.get(i)));
                double score = answers.get( options.get(i));
                if(predicate.test(x+score)){
                    x += score;
                    response = options.get(i);
                    break;
                }

                if(questions.size() == 0 || answers.size() == 0)
                    return 9;
            } while (true);
            // we have a answer
            System.out.println("q : " + question +" => a: " + response);
            qa_paper.addQuestionAnswer(question, response);
        }
        return  x;

    }
    public void acceptPapers(QA_Paper qa_paper, double min_score) {

        this.min_score = min_score;
        double score;

        do {
            score =  getRandomAnswer(is_included, qa_paper);
            System.out.println("score = " + score);
            if(questions.size() == 0 || answers.size() == 0)
                return;

        } while ( score < min_score);



    }
}
