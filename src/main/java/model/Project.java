package model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import model.RelisUser;

import java.util.ArrayList;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Project {


    // the project name
    private String project_name;
    // the project owner;
    private RelisUser project_owner;
    // the all reviewers for the current project
    private ArrayList<RelisUser> reviewer = new ArrayList<>();


    private Screening screening;

    private Classification classification;


    /**
     *
     * @param relisUser the user to compare with the project owner
     * @return  true if relisUser is equals to this current project owner
     * otherwise false;
     */
    public boolean isProjectFor(RelisUser relisUser){
        return relisUser != null && relisUser.equals(project_owner);

    }


}
