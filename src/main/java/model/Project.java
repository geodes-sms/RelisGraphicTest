package model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import model.RelisUser;

import java.io.FileWriter;
import java.util.ArrayList;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Project {


    // the project name
    private String project_name;
    // the project owner;
    private RelisUser project_owner;
    // the all reviewers for the current project
    private ArrayList<RelisUser> reviewer = new ArrayList<>();

    private FileWriter output_file;
    private Screening screening;

    private String projectId;
    private Classification classification;
    private QualityAssement qa;


    /**
     *
     * @param relisUser the user to compare with the project owner
     * @return  true if relisUser is equals to this current project owner
     * otherwise false;
     */
    public boolean isProjectFor(RelisUser relisUser){
        return relisUser != null && relisUser.equals(project_owner);

    }


    public void setQA(QualityAssement qa) {
        this.qa  = qa;
    }
}
