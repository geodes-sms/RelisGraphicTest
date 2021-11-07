package model.relis_type;

import lombok.Data;

import java.util.Random;

@Data
public class IntegerType extends TypeOf {

    private int maxCharacter;

    public  Object getMockValue(){

        return new Random().nextInt(2,13);
    }
}
