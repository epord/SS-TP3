import java.text.DecimalFormat;

public class Utils {
    private static DecimalFormat format = new DecimalFormat("#.###");
    public static String formatDouble(double num){
        return  format.format(num);
    }
}
