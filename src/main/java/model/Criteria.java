package model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class Criteria {

    private String name;

    private int count = 0;



    public boolean equals(Object o){

        Criteria criteria = (Criteria) o;
        return this.name.equals(criteria.name);
    }

    @Override
    public String toString(){
        return "{name="+name+" , count= "+count+"}";
    }


    public void increment(int i) {
        count += i;
    }
}
