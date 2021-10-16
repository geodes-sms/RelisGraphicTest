package model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Classification {

    private ArrayList<Paper> papersToClassify;

    private ArrayList<RelisUser> classificators;
    private int number_of_classifier = 1;


    public String toString(){

        String papers="\n", users="\n";

        if(classificators != null) for(RelisUser u : classificators) users += u +" ,\n";
        else users = " ";
        if(papersToClassify != null)
        for(Paper p : papersToClassify) papers += p +" ,\n";
        else papers = " ";
        papers += "]\n";
        return "[Papers => [" + papers +"\n], \n" + " classificators => ["+ users + "]\n]";
    }


}
