package model;
import databases.PapersDataBase;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import utils.Utility;

import java.util.ArrayList;
import java.util.Random;


@AllArgsConstructor
@NoArgsConstructor
@Data
public class ScreeningDecisionMaker {


    private int UpperBoundPaperLength;

    private int INCLUDED;

    private int EXCLUDED;

    private int IN_CONFLICT=0;

    private Object model;

    Random random = new Random();
    public void makeDecision(int number){

        if(number == 1){
            makeSingLeUserDecision();
            return;
        }
        int rest = UpperBoundPaperLength;

        EXCLUDED = 1;

        IN_CONFLICT = 2;
        INCLUDED = rest - EXCLUDED - IN_CONFLICT;
        System.out.println("INCLUDE = " + INCLUDED);
        System.out.println("EXCLUDED " + EXCLUDED);
        System.out.println("IN_CONFLICT = " + IN_CONFLICT);

    }

    public void makeSingLeUserDecision(){

        int rest = UpperBoundPaperLength;
        INCLUDED  = Utility.nextInt(UpperBoundPaperLength/2, rest);
        EXCLUDED = rest - INCLUDED;
        System.out.println("INCLUDE = " + INCLUDED);
        System.out.println("EXCLUDED " + EXCLUDED);

    }

    public ArrayList<Paper> applyDecisionForPapers(ArrayList<Paper> papers) throws CloneNotSupportedException {
        ArrayList<Paper> result = new ArrayList<>();
        for (int i = 0; i < INCLUDED; i++) {

            Paper p = papers.remove(Utility.nextInt(0, papers.size()-1));
            p.setDecision(PaperDecision.INCLUDED);
            p.setInclude_count(1);
            result.add(p);

        }
        for (int i = 0; i < EXCLUDED; i++) {
            Paper p = papers.remove(Utility.nextInt(0, papers.size()));
            p.setDecision(PaperDecision.EXCLUDED);
            p.setCriteria((Criteria) PapersDataBase.getInstance().nextCriteriaValue().clone());
            p.setExclude_count(1);
            result.add(p);

        }

        for (int i = 0; i < IN_CONFLICT; i++) {
            Paper p = papers.remove(Utility.nextInt(0, papers.size()));
            p.setDecision(PaperDecision.IN_CONFLICT);
            p.setConflict_count(1);
            result.add(p);
        }
        return result;
    }





}
