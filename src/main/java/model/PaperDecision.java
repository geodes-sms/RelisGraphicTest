package model;

public enum PaperDecision {

    INCLUDED("Included"),
    EXCLUDED("Excluded"),
    IN_CONFLICT("IN CONFLICT"),
    NO_DECISION_YET("NO DECISION YET");
    private final String name;
    PaperDecision(String name){
      this.name = name;
    }

  @Override
  public String toString() {
    return name;
  }
}
