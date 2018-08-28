import javafx.stage.FileChooser;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.Collection;

public class Main {

    public static void main(String[] args) throws Exception {
        RandomGenerator randomGenerator = new RandomGenerator();
        Collection<Particle> generatedParticles = randomGenerator.generateParticles(0.09, 0.24, 1000, 0.0015, 0.0015, 0.05, 0.5);

        BufferedWriter bw = new BufferedWriter(new FileWriter("p5/simulation-animator/random.txt"));
        bw.write(0.09 + " " + 0.24 + "\n");
        for (Particle p: generatedParticles) {
            bw.write(p.getX() + " " + p.getY() + " " + p.getRadius() + "\n");
        }
        bw.close();

        FileManager.appendParticlesTo("data.txt",generatedParticles,0.4);

    }

}
