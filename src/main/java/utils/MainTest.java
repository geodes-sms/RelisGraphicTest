package utils;

import model.*;
import model.relis_categories.*;
import model.relis_parser.RelisParser;
import model.relis_type.TypeOf;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;

public class MainTest {




    public static void main(String[] args) {
//        TestListenerAdapter tla = new TestListenerAdapter();
//        TestNG testng = new TestNG();
//        testng.setTestClasses(new Class[] {ProjectTest.class});
//        testng.addListener(tla);
//        testng.run();

        QualityAssement qa = new QualityAssement();

        qa.setMin_score(3.5);
        QuestionAnswesPaper questionAnswesPaper = new QuestionAnswesPaper();
        questionAnswesPaper.addQuestions("Question 1");
        questionAnswesPaper.addQuestions("Question 2");
        questionAnswesPaper.addQuestions("Question 3");

        questionAnswesPaper.addAnswer("answer1", 3);
        questionAnswesPaper.addAnswer("answer2", 1.5);
        questionAnswesPaper.addAnswer("answer3", 1);
        questionAnswesPaper.addAnswer("answer4", 0);
        questionAnswesPaper.setMin_score(3.5);
        QA_Paper paper = new QA_Paper();

        questionAnswesPaper.setRejectedAnswer(paper,3.5);

    }











}
