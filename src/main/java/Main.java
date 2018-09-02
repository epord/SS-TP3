import java.io.*;
import java.util.ArrayList;
import java.util.Collection;

public class Main {

    public static void main(String[] args) throws Exception {
        generateRandomWorld("p5/simulation-animator/random.txt", 24, 9, 200, 0.15, 0.15, 3.0, 5.0);

		File savedWorld = new File("p5/simulation-animator/random.txt");
		System.out.println(savedWorld.getAbsolutePath());
        GasSimulator2D simulator = getWorldFromFile(savedWorld);

        System.out.println("Starting Simulation");
        simulator.simulate(100, 5000);
        System.out.println("Ending Simulation");
//        Particle p1 = new ParticleImpl(0, 0, 1, 1, 1, 0);
    }

    private static GasSimulator2D getWorldFromFile(File file) throws Exception {
        BufferedReader br = new BufferedReader(new FileReader(file));
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

        return new GasSimulator2D(particles, worldWidth, worldHeight);
    }

    private static void generateRandomWorld(String filename, double worldHeight, double worldWidth, int particlesAmount, double minRadius, double maxRadius, double minSpeed, double maxSpeed) throws Exception{
        // Generate random initial state
        RandomParticleGenerator randomParticleGenerator = new RandomParticleGenerator();
        Collection<Particle> generatedParticles = randomParticleGenerator.generateParticles(worldHeight, worldWidth, particlesAmount, minRadius, maxRadius, minSpeed, maxSpeed);

        BufferedWriter bw = new BufferedWriter(new FileWriter("p5/simulation-animator/random.txt"));
		bw.write(worldHeight + " " + worldWidth + " " + particlesAmount + "\n");
		for (Particle p: generatedParticles) {
			bw.write(p.getX() + " " + p.getY() + " " + p.getRadius() + " " + p.getVelocity().get(0) + " " + p.getVelocity().get(1) +  "\n");
        }
        bw.close();

    }

}
