package model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import model.user_work.ScreeningPhaseWork;
import utils.Utility;

import java.util.ArrayList;
import java.util.Random;

@AllArgsConstructor
@NoArgsConstructor
@Data
public  class Paper implements Observable ,Cloneable{
    // papers attributes
    protected String key;

    protected String title;
    protected String year;


    // criteria of exclusion
    private Criteria criteria;
    // paper decision (included? excluded? in conflict?)
    private PaperDecision decision;

    // all the users those we gave a decision to this paper
    ArrayList<ScreeningPhaseWork> reviewers = new ArrayList<>();
    // last users decision
    private ScreeningPhaseWork last_decision_user;
    // default decision
    private PaperDecision lastDecision = PaperDecision.NO_DECISION_YET;
    // count of differents papers 
    private int include_count;
    private int exclude_count;
    private int conflict_count;


    private String validation_note;

    private String valiation_response;
/**
 * this method will be call after the end of a screening phase so we
 * can merge all the decision for this paper
 * and check if theres's a  conflict
 */
    @Override
    public void notifyChange() {


        for (int i = 0; i < reviewers.size(); i++) {
            ScreeningPhaseWork phaseWork = reviewers.get(i);
            Paper p1 = phaseWork.getPaperByKey(key);
            for (int j = 0; j < reviewers.size(); j++) {
                Paper p2 = reviewers.get(j).getPaperByKey(key);
                if ((i != j) && (p1.isIn_a_conflict() || p2.isIn_a_conflict()))
                    phaseWork.update(p2);
            }
        }
    }


    public void propageConflictResolutionChange(){

        for(ScreeningPhaseWork user : reviewers){

            Paper paper = user.getPaperByKey(key);
            if(paper.getLastDecision() != decision){
                paper.setDecision(decision);

                paper.setLastDecision(decision);
                if(decision == PaperDecision.EXCLUDED)
                    paper.setCriteria(criteria);

            } else if(paper.getLastDecision() == decision
                    && paper.criteria != criteria){
                paper.setCriteria(criteria);
            }
        }
    }
    private boolean isIn_a_conflict() {
        return decision == PaperDecision.IN_CONFLICT;
    }

    public boolean IsIncluded(){
        return decision == PaperDecision.INCLUDED;
    }
    public boolean IsExcluded(){
        return decision== PaperDecision.EXCLUDED;
    }




    @Override
    public String toString(){
        return "{key="+key+",title="+ title+", year="+ year+", decision=" + decision +", criteria="+criteria+" lastDec" +
                "ison=" + lastDecision+" }";
    }


    public boolean isFirstUser(){

        return this.exclude_count == 0 && this.include_count ==0;
    }

    public void incrementIncludeCount(int c ){ include_count +=c;}
    public void incrementExcludeCount(int c ){ exclude_count +=c;}
    public void incrementConflictCount(int c){ conflict_count += c;}

    public void addReviewers(ScreeningPhaseWork user){
        reviewers.add(user);
    }

    public PaperDecision getLastReviewerDecision(){

        if(last_decision_user != null)
            return last_decision_user.getPaperByKey(key).getDecision();
        return PaperDecision.NO_DECISION_YET;
    }

    public boolean isIncluded(){
        return this.decision == PaperDecision.INCLUDED;
    }

    public boolean isExcluded(){
        return this.decision == PaperDecision.EXCLUDED;
    }
    public boolean isInConflict(){
        return this.decision == PaperDecision.NO_DECISION_YET;
    }

    @Override
    public void addObserver(Object o) {

        ScreeningPhaseWork reviewer = (ScreeningPhaseWork) o;
        addReviewers(reviewer);

    }

    @Override
    public boolean removeObserver(Object o) {
        ScreeningPhaseWork relisUser = (ScreeningPhaseWork) o;
        ScreeningPhaseWork exist = reviewers.stream()
                .filter(relisUser::equals)
                .findFirst()
                .orElse(null);
        if(exist != null){
            reviewers.remove(exist);
            return true;
        }
        return false;
    }

    @Override
    public Object clone(){

        try {
            return (Paper) super.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return new Paper();
    }

    /***
     * Resolve the papers decision conflict
     by choosing a random decision from all the user decision
     */
    public void resolveConflict(){
        ArrayList<PaperDecision> decisions = new ArrayList<>();

        this.reviewers.forEach(
                user->{
                    decisions.add(user.getPaperByKey(key).getLastDecision());
                }
        );

        int index = Utility.nextInt(0,decisions.size());
        this.decision = decisions.get(index);
        if(decision == PaperDecision.EXCLUDED){
            criteria = reviewers.get(index).getPaperByKey(key).getCriteria();
        }

        //propageConflictResolutionChange();

    }
    @Override
    public boolean equals(Object o){
        Paper p = (Paper) o;
        return this.key.equals(p.getKey());

    }

    public void addValidation(String correct, String not) {
        valiation_response = correct;
        validation_note = not;
    }
}
