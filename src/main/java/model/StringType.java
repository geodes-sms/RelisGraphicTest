package model;

import lombok.Data;

import java.util.Random;
@Data
public class StringType extends TypeOf{

    private int maxCharacter;
    public String getMockValue(){

        return "string "+ new Random().nextInt(2,123);
    }
}
