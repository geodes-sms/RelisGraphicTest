package model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import utils.Utility;

import java.util.ArrayList;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RelisUser implements Comparable<RelisUser> {

    // relis user fields
    private String full_name;
    private String username;
    private String user_email;

    private String password;
    private String user_usergroup;
    private String creation_time;
    private String create_by;
    private String user_state;
    private String user_active;

    private WebDriver driver= null;
    private ArrayList<Screening> my_screening= new ArrayList<>();

    /**
     * Redefinition of the equals method
     * in order to compare two relis user
     * @param o
     * @return
     */
    @Override
    public boolean equals(Object o){
        RelisUser user = (RelisUser) o;
        if(user != null){

            return user.username.equals(this.username)
                    && user.password.equals(this.password)
                    && user.user_email.equals(this.user_email);
        }
        return false;
    }

    /**
     * redifintion of the toString method for pretty print of model.RelisUser
     * @return
     */
    @Override
    public String toString(){

        return "fullName: " + full_name +" , user_name: " + username
                +" user_email " + user_email +" password: "+password +" usergroup " + user_usergroup;

    }


    /**
     * this procedure return the sql code
     *in order to insertion users into the relis user databses
     *
     * @return
     */
    public String getRelisUserDBFormatSQL(){

        // we encript the user password using MD5 as relis is using.
        String encriptedPassword = Utility.encriptWiht_MD5(password);
        // return the specific format for the insertion
        return String.format("(\"%s\",\"%s\",\"%s\",%s,\"%s\",\"%s\",%s,%s,%s)", full_name,username,user_email,
                user_usergroup,encriptedPassword,creation_time,create_by,user_state,user_active);

    }

    @Override
    public int compareTo(RelisUser o) {

        if(o != null) return this.full_name.compareTo(o.full_name);
        return 0;
    }

    public void addScreening( Screening screening){

        this.my_screening.add(screening);
    }

    public void removeScreeningPhase(Screening screening){
        this.my_screening.remove(screening);
    }


    public void setUpCurrentScreeningPhase(){

        if(my_screening.size() == 0) return;
        Screening screening = my_screening.get(0);
        screening.setCurrent_reviewer(this);
        screening.setUp();
    }

    public Screening getCurrentScreeningPhase(){
        return (my_screening.size() == 0) ? null: my_screening.get(0);
    }

    public int getAssignmentsLength(){
        return getCurrentScreeningPhase().getMy_assignments().size();
    }

    public String getPaperDecision(String key){
        return getCurrentScreeningPhase().getNextDecision(key);
    }



}

