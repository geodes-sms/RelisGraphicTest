package model.relis_parser;

import lombok.Data;
import utils.Utility;

import java.util.ArrayList;

@Data
public class StringParser  {

    private int index=0;
    private String data;


    private String _last_word = "";



    public void updateData(int index ){

        data = data.substring(index);

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
        _last_word  =result;
        return result;
    }


    public String getNextWordSeparedBySpace(){


        int i=0;

        while (( i < data.length()) &&data.charAt(i) == '\n' || data.charAt(i) ==' ') i++;

        String nextWordSepared = "";
         while ( (i < data.length()) && data.charAt(i) != ' ' && data.charAt(i) != '\n')
             nextWordSepared += data.charAt(i++);


         updateData(i);
         _last_word = nextWordSepared;
         return nextWordSepared;
    }





    public void back() {

        data = _last_word +" " + data;
    }

    public void backTo(String nextWord) {

        data = nextWord +" " + data;
    }


    public void skipCurrentWord(){

        getNextWord();
    }

    public  String getNextWord(){

        int pos=0;

        String word = "", data = getData();
        while( (pos < data.length())&& ( data.charAt(pos) == '\n' || data.charAt(pos) == ' '
        || data.charAt(pos) == '\t')) pos ++;

        boolean startWithSeparator = pos < data.length() && Utility.isRelisPunctuation(data.charAt(pos));
        while( (pos < data.length()) &&  (((data.charAt(pos)) != '\n' && data.charAt(pos) != ' ')
                || data.charAt(pos) == '_'))
        {
            word += data.charAt(pos++);
            if((pos < data.length()) && Utility.isRelisPunctuation(data.charAt(pos)) || startWithSeparator) break;
        }
        updateData(pos);
        _last_word = word;
        return word;


    }


    public ArrayList<String> getNextListElements(){

        String nextWord = getNextWord();
        ArrayList<String> list = new ArrayList<>();
        while (!nextWord.equals("]")){
            String val = gextNextQuotedWord();
            list.add(val);
            nextWord = getNextWord();
        }
        return list;
    }
}
