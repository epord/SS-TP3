
import java.io.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.stream.Stream;

public class FileManager {

    public void writeString(BufferedWriter bw, String s) {
        try {
            bw.write(s + "\n");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void appendParticlesTo(String file, Collection<Particle> particles, Double time) {
        try {
            appendParticlesTo(new File(file),particles,time);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static void appendParticlesTo(File file, Collection<Particle> particles, Double time) throws IOException {
        FileWriter bw = new FileWriter(file,true);
        StringBuilder sb = new StringBuilder();
        bw.write(sb.toString());
        for(Particle particle : particles) {
            sb.setLength(0);
            sb.append(particle. getId())
            .append(",")
            .append(Utils.formatDouble(particle.getX()))
            .append(",")
            .append(Utils.formatDouble(particle.getY()))
            .append(",")
            .append(Utils.formatDouble(particle.getVelocity().get(0)))
            .append(",")
            .append(Utils.formatDouble(particle.getVelocity().get(1)))
            .append(",")
            .append(Utils.formatDouble(particle.getRadius()))
            .append("\n");
            bw.write(sb.toString());
        }
        bw.close();
    }

}