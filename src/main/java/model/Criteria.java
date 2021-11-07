package model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class Criteria implements Cloneable{

    private String name;

    private int count = 0;


    public Criteria(String name){
        this.name = name;
    }

    public boolean equals(Object o){

        Criteria criteria = (Criteria) o;
      System.out.println(this +" VS " + criteria);
        return this.name.equals(criteria.name);
    }

    @Override
    public String toString(){
        return "{name="+name+" , count= "+count+"}";
    }


    public void increment(int i) {
        count += i;
    }

    public Object clone() throws CloneNotSupportedException {
      return super.clone();
    }
}
