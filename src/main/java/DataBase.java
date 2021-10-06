import java.awt.geom.Area;
import java.awt.geom.RectangularShape;
import java.util.ArrayList;
import java.util.Random;

public class DataBase {

    private static DataBase db = null;

    private static final ArrayList<RelisUser> reviewers = new ArrayList<>();
    private static final ArrayList<RelisUser> project_managers =new ArrayList<>();
    private static final ArrayList<RelisUser> validators = new ArrayList<>();
    private static final ArrayList<RelisUser> guests = new ArrayList<>();

    /**
     *  we use a single instance for this classe
     * @return the single instance
     */
    public static DataBase getInstance(){

        if(db == null)
            db = new DataBase();
        return db;
    }



    public RelisUser GetAReviewer(){

        return reviewers.get(new Random().nextInt(0, reviewers.size()));
    }
    public RelisUser getAValidator(){

        return validators.get(new Random().nextInt(0, validators.size()));
    }
    public RelisUser getAProjectManager(){

        return project_managers.get(new Random().nextInt(0, project_managers.size()));
    }
    public RelisUser getAGuestUser(){

        return guests.get(new Random().nextInt(0, guests.size()));
    }

    public void addReviewer( RelisUser user){
        reviewers.add(user);
    }
    public void deleteReviewer(RelisUser reviewer){
        reviewers.remove(reviewer);
    }
    public void addProjectManager(RelisUser user){
        project_managers.add(user);
    }
    public void deleteProjectManager(RelisUser pm){
        project_managers.remove(pm);
    }
    public void addValidator(RelisUser user){
        validators.add(user);
    }
    public void deleteValidator(RelisUser validator){
        validators.remove(validator);
    }
    public void addGuestUser(RelisUser user){
        guests.add(user);
    }
    public void deleteGuest(RelisUser guest){
        guests.remove(guest);
    }


}
