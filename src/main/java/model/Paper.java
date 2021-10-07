package model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class Paper {

    String key;

    String title;
    String year;
    String criteria;


    @Override
    public String toString(){
        return "{key="+key+",title="+ title+", year="+ year+", criteria=" + criteria+"}";
    }

}
