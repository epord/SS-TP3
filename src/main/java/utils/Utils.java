package utils;

import java.text.DecimalFormat;

public class Utils {
    private static DecimalFormat format = new DecimalFormat("#.###");
    public static String formatDouble(double num){
        return  format.format(num);
    }
    public static String formatDouble(Double num){
        return  format.format(num);
    }
}
