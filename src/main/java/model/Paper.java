package model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class Paper implements Observable ,Cloneable{

    private String key;

    private String title;
    private String year;
    private Criteria criteria;
    private PaperDecision decision;

    ArrayList<RelisUser> reviewers = new ArrayList<>();
    private RelisUser last_decision_user;
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

    public void addReviewers(RelisUser user){
        reviewers.add(user);
    }

    public PaperDecision getLastReviewerDecision(){

        if(last_decision_user != null)
            return last_decision_user.getPaperDecision(this);
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

        RelisUser reviewer = (RelisUser) o;
        addReviewers(reviewer);

    }

    @Override
    public boolean removeObserver(Object o) {
        RelisUser relisUser = (RelisUser) o;
        RelisUser exist = reviewers.stream()
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
            RelisUser relisUser = reviewers.get(i);
            Paper p1 = relisUser.getPaper(key);
            for (int j = 0; j < reviewers.size(); j++) {
                Paper p2 = reviewers.get(j).getPaper(key);
                if ((i != j) && (p1.isIn_a_conflict() || p2.isIn_a_conflict()))
                    relisUser.update(p2);
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

    public int takeExcludedWithCriteria(Criteria c1) {
        return reviewers.stream().mapToInt(user -> user.CountByExcludedCriteria(c1)).sum();
    }
}
