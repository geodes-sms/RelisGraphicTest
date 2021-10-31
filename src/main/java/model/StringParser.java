package model;

import lombok.Data;

@Data
public class StringParser  {

    private int index=0;
    private String data;



    public void updateData(int index ){

        data = data.substring(index+1);

    }

    public String gextNextQuotedWord(){

        String result ="";
        int from = 0;


        if((2+index) >= data.length() || !(data.contains("\""))) return result;

        int start, end;
        start = data.indexOf("\"",from)+1;
        end = data.indexOf("\"", start);
        result = data.substring(start,end);
        data = data.substring(end+1);
        return result;
    }


}
