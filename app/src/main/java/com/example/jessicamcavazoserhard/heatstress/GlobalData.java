package com.example.jessicamcavazoserhard.heatstress;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by jessicamcavazoserhard on 4/28/17.
 */

public class GlobalData {

    final double c1=16.923,c2=0.185212,c3=5.37941,c4=-0.100254,c5=0.00941695,c6=0.00728898,c7=0.000345372,c8=-0.000814971,c9=0.0000102102,c10=-0.000038646,c11=0.0000291583,c12=0.00000142721,c13=0.000000197483,c14=-0.0000000218429,c15=0.000000000843296,c16=-0.0000000000481975;


    public double getRisk (double temperature, String humidity ){

        humidity = humidity.replaceAll("%", "");
        int Rhumidity = Integer.parseInt(humidity);


        double heatIndex = c1 + c2*temperature +c3*Rhumidity + c4*temperature*Rhumidity + c5*(Math.pow(temperature,2)) + c6*(Math.pow(Rhumidity,2)) + c7*(Math.pow(temperature,2))*Rhumidity + c8*temperature*(Math.pow(Rhumidity,2)) + c9*(Math.pow(temperature,2))*(Math.pow(Rhumidity,2)) + c10*(Math.pow(temperature,3)) + c11*(Math.pow(Rhumidity,3)) + c12*(Math.pow(temperature,3))*Rhumidity + c13*temperature*(Math.pow(Rhumidity,3)) + c14*(Math.pow(temperature,3))*(Math.pow(Rhumidity,2)) + c15*(Math.pow(temperature,2))*(Math.pow(Rhumidity,3)) + c15*(Math.pow(temperature,3))*(Math.pow(Rhumidity,3));

        return heatIndex;

    }

}
