package databases;
import model.*;
import org.openqa.selenium.WebDriver;
import view.ScreeningView;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import java.util.function.BiFunction;


/***
 * @author Mahamat Youssouf Issa
 *  9/10/2021
 *
 */
public class PapersDataBase  {



    private static PapersDataBase db_instance =null;
    ArrayList<Paper> papers_db =new ArrayList<>();
    ArrayList<Criteria> criteria = new ArrayList<>();

    ScreeningDecisionMaker decisionMaker = new ScreeningDecisionMaker();




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


    public void setData(ArrayList<Paper> papers) throws CloneNotSupportedException {

        papers_db.addAll(papers);
        decisionMaker.setUpperBoundPaperLength(papers.size());
        decisionMaker.makeDecision();
        papers_db = decisionMaker.applyDecisionForPapers(papers_db);
        papers_db.forEach(p -> System.out.println("Data : " + p));
      //v  papers_db.addAll(Utility.work_through_table(driver));
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
        criteria.add(new Criteria( "Criteria 1",0));
        criteria.add(new Criteria("Criteria 2",0));
    }


    public boolean isCriteria(String cr){
        return criteria
                .stream()
                .filter(critere-> critere.getName().equals(cr))
                .findFirst()
                .orElse(null) != null;
    }

    public String nextCriteria() {

        return criteria.get(new Random().nextInt(0,criteria.size())).getName();
    }

    public Criteria nextCriteriaValue(){
        return criteria.get(new Random().nextInt(0,criteria.size()));
    }
    public Criteria getNextCriteriaWithout(Criteria c){

        if(criteria.size() == 1) return null;
        return criteria.stream()
                .sorted()
                .filter( c1 -> !c1.getName().equals(c.getName()))
                .findFirst()
                .get();

    }

    public Criteria getNextCriteriaFrom(Criteria r){

        int pos = criteria.indexOf(r);
        assert pos != -1;
        return ((pos+1) < criteria.size())?
               criteria.get(pos+1):
                null;
    }

    public void commitChanges(){


        papers_db.stream()
                .forEach(Paper::notifyChange);
    }

    public void showCriteriaPercentage(){

        criteria.forEach(cr ->{
          int count = ScreeningPhase.getCriteriaStatitics(cr);
          cr.setCount(count);
          });
        criteria.forEach(System.out::println);
    }




  public ArrayList<Paper> getPapers() {
        return papers_db;
  }
}
