package model.relis_type;

import utils.Utility;

import java.util.Random;

public class RealType extends TypeOf {


    public Object getMockValue(){
        double d = Utility.nextDouble(0,1999);

        return String.format("%.2f",d);
    }



}
