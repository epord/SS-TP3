import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.Collection;

public class Main {

    public static void main(String[] args) throws Exception {
        RandomGenerator randomGenerator = new RandomGenerator();
        Collection<Particle> generatedParticles = randomGenerator.generateParticles(10, 10, 100, 1.0, 2.0);

        BufferedWriter bw = new BufferedWriter(new FileWriter("p5/simulation-animator/random.txt"));
//        bw.write(generatedParticles.size() + "\n");
        for (Particle p: generatedParticles) {
            bw.write(p.getX() + " " + p.getY() + " " + p.getRadius() + "\n");
        }
        bw.close();
    }

}
