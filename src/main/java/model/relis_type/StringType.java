package model.relis_type;

import lombok.Data;
import model.relis_type.TypeOf;
import utils.Utility;

import java.util.Random;
@Data
public class StringType extends TypeOf {

    private int maxCharacter;
    public String getMockValue(){

        return "string "+ Utility.nextInt(2,123);
    }
}
