package model.relis_type;

import utils.Utility;

import java.util.Random;

public class RealType extends TypeOf {


    public Object getMockValue(){

        return Utility.nextDouble(0,1999);
    }



}
