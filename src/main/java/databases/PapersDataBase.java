package databases;

import model.DatabaseInfo;
import model.Paper;
import org.openqa.selenium.WebDriver;
import utils.Utility;
import view.ScreeningView;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Random;

public class PapersDataBase  {



    private static PapersDataBase db_instance =null;
    ArrayList<Paper> papers_db =new ArrayList<>();
    ArrayList<String> criteria = new ArrayList<>();


    /**
     *
     * @return the single instance for this class
     */
    public static PapersDataBase getInstance() {
        if(db_instance == null)
            db_instance = new PapersDataBase();
        return  db_instance;
    }


    /**
     * adding a paper to the paper db
     * @param paper
     */
    public void addPaper(Paper paper){
        papers_db.add(paper);
    }

    /**
     * remove a paper
     * @param paper the paper to remove
     */
    public void removePaper(Paper paper){
        papers_db.remove(paper);
    }

    /**
     * remove a paper by his key which it's his id
     * @param key
     */
    public void removePaperByKey(String key){
        papers_db.removeIf(p -> p.getKey().equals(key));
    }

    public void setData(WebDriver driver){

        ScreeningView.showAllAssigmentsPage(driver);
        Utility.work_through_table(driver);
    }

    /**
     *
     * @param key paper bibtex key
     * @return the paper if exist otherwise null
     */
    public Paper getPaper(String key) {
        return papers_db.stream()
                .filter(p-> p.getKey().equals(key))
                .findFirst()
                .orElse(null);
    }

    public void setMockCriteria(){
        criteria.add("Criteria 1");
        criteria.add("Criteria 2");
    }


    public boolean isCriteria(String cr){
        return criteria.stream()
                .filter(critere-> critere.equals(cr))
                .findFirst()
                .orElse(null) != null;
    }

    public String nextCriteria() {

        return criteria.get(new Random().nextInt(0,criteria.size()));
    }
}
