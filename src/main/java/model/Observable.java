package model;

public interface Observable {

    public void addObserver( Object o);
    public boolean removeObserver(Object o);
    public void notifyChange();


}
