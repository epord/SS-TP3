import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Collection;

public class Main {

    public static void main(String[] args) throws Exception {
//        generateRandomWorld("p5/simulation-animator/random.txt", 0.09, 0.24, 1000, 0.0015, 0.0015, 0.05, 0.5);

        GasSimulator2D simulator = getWorldFromFile("p5/simulation-animator/random.txt");

        Particle p1 = new ParticleImpl(0, 0, 1, 1, 1, 0);
    }

    private static GasSimulator2D getWorldFromFile(String filename) throws Exception {
        BufferedReader br = new BufferedReader(new FileReader(filename));
        String[] l = br.readLine().split(" ");
        Double worldHeight = Double.parseDouble(l[0]);
        Double worldWidth = Double.parseDouble(l[1]);
        int particleCount = Integer.parseInt(l[2]);

        Collection<Particle> particles = new ArrayList<>();
        for (int i = 0; i < particleCount; i++) {
            l = br.readLine().split(" ");
            particles.add(new ParticleImpl(i, Double.parseDouble(l[2]), Double.parseDouble(l[0]), Double.parseDouble(l[1]),
                    Double.parseDouble(l[3]), Double.parseDouble(l[4])));
        }

        return new GasSimulator2D(particles, worldHeight, worldWidth);
    }

    private static void generateRandomWorld(String filename, double worldHeight, double worldWidth, int particlesAmount, double minRadius, double maxRadius, double minSpeed, double maxSpeed) throws Exception{
        // Generate random initial state
        RandomGenerator randomGenerator = new RandomGenerator();
        Collection<Particle> generatedParticles = randomGenerator.generateParticles(worldHeight, worldWidth, particlesAmount, minRadius, maxRadius, minSpeed, maxSpeed);

        BufferedWriter bw = new BufferedWriter(new FileWriter("p5/simulation-animator/random.txt"));
        bw.write(0.09 + " " + 0.24 + " " + 1000 + "\n");
        for (Particle p: generatedParticles) {
            bw.write(p.getX() + " " + p.getY() + " " + p.getRadius() + " " + p.getVelocity().get(0) + " " + p.getVelocity().get(1) +  "\n");
        }
        bw.close();

    }

}
