package model;

import controller.ConnexionController;
import controller.ProjectController;
import databases.PapersDataBase;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import utils.*;

import java.util.ArrayList;
import java.util.Optional;
import java.util.Random;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class Screening {


    private int excluded_papers =0;
    private int included_papers = 0;
    private int in_conflict_papers = 0;
    private RelisUser current_reviewer = null;


    private ArrayList<Paper> my_assignments = new ArrayList<>();



    public void setUp(){

        Initialiazer initialiazer = new Initialiazer();
        initialiazer.init();
        current_reviewer.setDriver(initialiazer.getWebDriver());
        ConnexionController connexion = new ConnexionController();
        connexion.connect(current_reviewer.getDriver(), current_reviewer);
        ProjectController projectManager  = new ProjectController();

        projectManager.openProject(current_reviewer.getDriver(), "");

    }

    private Paper getPaper(Paper p){

        return my_assignments.stream()
                        .filter(p::equals)
                        .findFirst()
                .orElse(null);
    }

    synchronized public String getNextDecision(Paper paper, PaperDecision decision){

        Paper p = Utility.getPaperByKey(my_assignments,paper.getKey());
        assert p != null;
        if(decision == PaperDecision.INCLUDED){
            p.incrementIncludeCount(1);
            return "TRUE";

        } else if(decision == PaperDecision.EXCLUDED){
            p.incrementExcludeCount(1);
            p.getCriteria().increment(1);
            return p.getCriteria().getName();
        } else{
            p.setDecision(PaperDecision.IN_CONFLICT);
            paper.addObserver(this.getCurrent_reviewer());


            PaperDecision lastDecision = paper.getLastDecision();
            if(lastDecision == PaperDecision.INCLUDED){

                p.setCriteria(PapersDataBase.getInstance().nextCriteriaValue());
                p.setLastDecision(PaperDecision.EXCLUDED);
                paper.setLastDecision(PaperDecision.EXCLUDED);
                paper.setLast_decision_user(current_reviewer);
                p.getCriteria().increment(1);
                //System.out.println("EXCLUDE CASE " + p.getCriteria());


                return p.getCriteria().getName();

            } else if(lastDecision == PaperDecision.EXCLUDED){

                RelisUser u = paper.getLast_decision_user();

                Criteria c = PapersDataBase.getInstance().getNextCriteriaFrom(u.getExclusionCriteria(paper));
                if (c == null) {

                    paper.setLastDecision(PaperDecision.INCLUDED);
                    paper.setLast_decision_user(current_reviewer);

                    p.setLastDecision(PaperDecision.INCLUDED);
                   // paper.notifyChange();

                    return "TRUE";
                }
                else {
                    p.setCriteria(c);
                    p.setLastDecision(PaperDecision.EXCLUDED);
                    paper.setLastDecision(PaperDecision.EXCLUDED);
                    paper.setLast_decision_user(current_reviewer);
                    //paper.notifyChange();
                    p.getCriteria().increment(1);
                    return p.getCriteria().getName();
                }

            } else {

                boolean nextDecision = new Random().nextBoolean();
                    if (nextDecision) {
                        paper.setLastDecision(PaperDecision.INCLUDED);
                        p.setLastDecision(PaperDecision.INCLUDED);
                        paper.setLast_decision_user(current_reviewer);
                        return "TRUE";

                    }  else {

                        Criteria c = PapersDataBase.getInstance().nextCriteriaValue();
                        p.setCriteria(c);
                        p.setLastDecision(PaperDecision.EXCLUDED);
                        paper.setLastDecision(PaperDecision.EXCLUDED);
                        paper.setLast_decision_user(current_reviewer);
                        p.getCriteria().increment(1);
                        return p.getCriteria().getName();

                    }



            }


        }

    }

    public PaperDecision getDecisionFor(Paper paper) {

        Optional<Paper> p = Optional.of(this.my_assignments.stream()
                .filter(p1 -> p1.equals(paper))
                .findFirst()
                .get());

        return p.get().getDecision();


    }


//    private void showTableItem(WebElement tr){
//        List<WebElement> elements= tr.findElements(By.tagName("td"));
//        elements.forEach(p-> System.out.print(p.getText() +" "));
//        System.out.println();
//
//    }


    public void updatePapersDecisionMetric(Paper p){


        Paper my_paper = getPaperByKey(p.getKey());
        assert  my_paper != null;
        my_paper.incrementConflictCount(1);
        if(my_paper.getLastDecision() == PaperDecision.INCLUDED){
            my_paper.incrementIncludeCount(1);
        } else{
            my_paper.incrementExcludeCount(1);
            my_paper.getCriteria().increment(1);
        }
    }


    public Criteria getPaperExclusionCriteria( Paper paper){

        return Utility.getPaperByKey(my_assignments,paper.getKey()).getCriteria();
    }

    public void printInfo(){
    int x= 0,y=0,z=0;


        for (int i = 0; i < my_assignments.size(); i++) {

            x += my_assignments.get(i).getInclude_count();
            y += my_assignments.get(i).getExclude_count();
            z += my_assignments.get(i).getConflict_count();
        }
        System.out.println("["+current_reviewer);
        System.out.print("include paper=" + x);
        System.out.print(", exclude paper=" + y) ;
        System.out.println(", in conflict=" + z+" ]");

    }


    public Paper getPaperByKey(String key) {

        return Utility.getPaperByKey(my_assignments,key);
    }


    public int CountExcludeByCriteria(Criteria c1) {


        return   my_assignments
                .stream()
                .filter(p -> p.getCriteria() != null)
                .filter(p -> p.getCriteria().equals(c1))
                .mapToInt(p->  1)
                .sum();

    }
}
