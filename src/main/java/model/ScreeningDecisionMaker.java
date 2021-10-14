package model;
;
import databases.PapersDataBase;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Random;


@AllArgsConstructor
@NoArgsConstructor
@Data
public class ScreeningDecisionMaker {


    private int UpperBoundPaperLength;

    private int INCLUDED;

    private int EXCLUDED;

    private int IN_CONFLICT;

    private Object model;

    Random random = new Random();
    public void makeDecision(){

        int rest = UpperBoundPaperLength;

        INCLUDED  = random.nextInt(random.nextInt(
                Math.min(30, (int) Math.floor(rest/4)),(int) Math.floor(rest/3)
        ), (int) rest/2);
        rest -= INCLUDED;
        EXCLUDED = random.nextInt(Math.min((int)Math.floor(rest/3), (int) Math.floor(rest/4)),
                (int) Math.floor(rest/3) +1);
        IN_CONFLICT = UpperBoundPaperLength - EXCLUDED - INCLUDED;
        System.out.println("INCLUDE = " + INCLUDED);
        System.out.println("EXCLUDED " + EXCLUDED);
        System.out.println("IN_CONFLICT = " + IN_CONFLICT);

    }

    public ArrayList<Paper> applyDecisionForPapers(ArrayList<Paper> papers) throws CloneNotSupportedException {
        ArrayList<Paper> result = new ArrayList<>();
        for (int i = 0; i < INCLUDED; i++) {

            Paper p = papers.remove(random.nextInt(0, papers.size()-1));
            p.setDecision(PaperDecision.INCLUDED);
            result.add(p);

        }
        for (int i = 0; i < EXCLUDED; i++) {
            Paper p = papers.remove(random.nextInt(0, papers.size()-1));
            p.setDecision(PaperDecision.EXCLUDED);
            p.setCriteria((Criteria) PapersDataBase.getInstance().nextCriteriaValue().clone());
            result.add(p);

        }

        for (int i = 0; i < IN_CONFLICT; i++) {
            Paper p = papers.remove(random.nextInt(0, papers.size()));
            p.setDecision(PaperDecision.IN_CONFLICT);
            result.add(p);
        }
        return result;
    }





}
