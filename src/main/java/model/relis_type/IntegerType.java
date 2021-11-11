package model.relis_type;

import lombok.Data;
import utils.Utility;

import java.util.Random;

@Data
public class IntegerType extends TypeOf {

    private int maxCharacter;

    public  Object getMockValue(){

        return Utility.nextInt(2,13);
    }
}
