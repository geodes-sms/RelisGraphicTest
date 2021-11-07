package model.relis_type;

import java.util.Random;

public class RealType extends TypeOf {


    public Object getMockValue(){
        double d = new Random().nextDouble(0,1999);

        return String.format("%.2f",d);
    }



}
