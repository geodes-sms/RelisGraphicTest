package model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Random;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class Paper implements Observable ,Cloneable{

    private String key;

    private String title;
    private String year;
    private Criteria criteria;
    private PaperDecision decision;

    ArrayList<PhaseWork> reviewers = new ArrayList<>();
    private PhaseWork last_decision_user;
    private PaperDecision lastDecision = PaperDecision.NO_DECISION_YET;

    private int include_count;
    private int exclude_count;
    private int conflict_count;



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

    public void addReviewers(PhaseWork user){
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

        PhaseWork reviewer = (PhaseWork) o;
        addReviewers(reviewer);

    }

    @Override
    public boolean removeObserver(Object o) {
        PhaseWork relisUser = (PhaseWork) o;
        PhaseWork exist = reviewers.stream()
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
    public void notifyChange() {


        for (int i = 0; i < reviewers.size(); i++) {
            PhaseWork phaseWork = reviewers.get(i);
            Paper p1 = phaseWork.getPaperByKey(key);
            for (int j = 0; j < reviewers.size(); j++) {
                Paper p2 = reviewers.get(j).getPaperByKey(key);
                if ((i != j) && (p1.isIn_a_conflict() || p2.isIn_a_conflict()))
                    phaseWork.update(p2);
            }
        }
    }


    public void propageConflictResolutionChange(){

        for(PhaseWork user : reviewers){

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
    public Object clone(){

        try {
            return (Paper) super.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return new Paper();
    }

    @Override
    public boolean equals(Object o){
        Paper p = (Paper) o;
        return this.key.equals(p.getKey());
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

      int index =  new Random().nextInt(0,decisions.size());
      this.decision = decisions.get(index);
      if(decision == PaperDecision.EXCLUDED){
        criteria = reviewers.get(index).getPaperByKey(key).getCriteria();
      }

      //propageConflictResolutionChange();

    }
}
