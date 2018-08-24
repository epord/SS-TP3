
import java.io.*;
import java.util.Collection;

public class FileManager {

    public void writeString(BufferedWriter bw, String s) {
        try {
            bw.write(s + "\n");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}