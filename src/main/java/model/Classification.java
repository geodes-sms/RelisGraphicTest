package model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import utils.Utility;

import java.util.ArrayList;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Classification {

    private ArrayList<Paper> papersToClassify;

    private ArrayList<RelisUser> classificators;
    private ArrayList<RelisUser> validators;
    private int number_of_classifier = 1;
    private int number_of_validator = 1;


    public String toString(){

        StringBuilder papers= new StringBuilder("\n");
        StringBuilder users= new StringBuilder("\n");
        StringBuilder validator_user = new StringBuilder("\n");
        if(classificators != null) for(RelisUser u : classificators)
            users.append("\t").append(u).append(" ,\n");
        else users = new StringBuilder(" ");
        if( validators != null) for(RelisUser u : validators)
            validator_user.append("\t").append(u).append(" ,\n");
        else users = new StringBuilder(" ");
        if(papersToClassify != null)
            for(Paper p : papersToClassify)
                papers.append("\t").append(p).append(" ,\n");
        else papers = new StringBuilder(" ");

        return "[Papers =>[" + papers +"\n], \n" + " classificators => \n["+ users + "]\n]"
                +"\n Validators => \t [" + validator_user +"\n \t ]\n ]";
    }


    public Paper getPaper(String  key){
        return Utility.getPaperByKey(papersToClassify,key);
    }



}
