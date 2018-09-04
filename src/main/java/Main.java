import experiments.ExperimentStatsHolder;
import experiments.ExperimentsStatsAgregator;
import experiments.Operation;

import javax.xml.ws.Holder;
import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

public class Main {

    public static void main(String[] args) throws Exception {
        Integer simulations = 5;
        ExperimentsStatsAgregator<GasMetrics> agregator = new ExperimentsStatsAgregator<>();
        ExperimentStatsHolder<GasMetrics> pressureTemperatureHolder = new ExperimentStatsHolder<>();
        for (int i = 0; i < simulations; i++) {
            generateRandomWorld("p5/simulation-animator/random.txt", 20, 20, 300, 0.15, 0.15, 1.0+i, 1.0+i);

            File savedWorld = new File("p5/simulation-animator/random.txt");
            System.out.println(savedWorld.getAbsolutePath());
            GasSimulator2D simulator = getWorldFromFile(savedWorld);

            System.out.println("Starting Simulation: " + i);
            ExperimentStatsHolder<GasMetrics> holder = simulator.simulate(500.0, 1.0,10000,true);

            Double temperature = holder.getDataSeries(GasMetrics.EQ_TEMPERATURE).get(0).getValue();
            Double pressure = holder.getDataSeries(GasMetrics.EQ_PRESSURE).get(0).getValue();
            pressureTemperatureHolder.addDataPoint(GasMetrics.FINAL_PRESSURES,i*1.0,pressure);
            pressureTemperatureHolder.addDataPoint(GasMetrics.FINAL_TEMPERATURE,i*1.0,temperature);
//            agregator.addStatsHolder(holder);
            System.out.println("Ending Simulation: " + i);
        }

        agregator.addStatsHolder(pressureTemperatureHolder);
        StringBuilder stringBuilder = agregator.buildStatsOutput(Arrays.asList(Operation.STD_LOW, Operation.MEAN, Operation.STD_HIGH, Operation.STD));
        FileManager.writeString("output.csv",stringBuilder.toString());
        System.out.println(stringBuilder.toString());
    }

    private void runSimulationForPressureTemperature() throws Exception {
        Integer simulations = 5;
        Integer speedIncrements = 10;
        ExperimentsStatsAgregator<GasMetrics> agregator = new ExperimentsStatsAgregator<>();
        for (int i = 0; i < simulations; i++) {
            ExperimentStatsHolder<GasMetrics> pressureTemperatureHolder = new ExperimentStatsHolder<>();
            for (int j = 0; j < speedIncrements; j++) {
                generateRandomWorld("p5/simulation-animator/random.txt", 20, 20, 300, 0.15, 0.15, 1.0 + j, 1.0 + j);

                File savedWorld = new File("p5/simulation-animator/random.txt");
                System.out.println(savedWorld.getAbsolutePath());
                GasSimulator2D simulator = getWorldFromFile(savedWorld);

                System.out.println("Starting Simulation: " + i);
                ExperimentStatsHolder<GasMetrics> holder = simulator.simulate(500.0, 1.0, 10000, true);

                Double temperature = holder.getDataSeries(GasMetrics.EQ_TEMPERATURE).get(0).getValue();
                Double pressure = holder.getDataSeries(GasMetrics.EQ_PRESSURE).get(0).getValue();
                pressureTemperatureHolder.addDataPoint(GasMetrics.FINAL_TP_RATIO, i * 1.0, temperature / pressure);
                pressureTemperatureHolder.addDataPoint(GasMetrics.FINAL_PRESSURES, i * 1.0, pressure);
                pressureTemperatureHolder.addDataPoint(GasMetrics.FINAL_TEMPERATURE, i * 1.0, temperature);
//            agregator.addStatsHolder(holder);
                System.out.println("Ending Simulation: " + i);
            }
            agregator.addStatsHolder(pressureTemperatureHolder);
        }
        StringBuilder stringBuilder = agregator.buildStatsOutput(Arrays.asList(Operation.STD_LOW, Operation.MEAN, Operation.STD_HIGH));
        FileManager.writeString("output.csv",stringBuilder.toString());
        System.out.println(stringBuilder.toString());
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
        Collection<Particle> generatedParticles = randomParticleGenerator.generateParticles(worldHeight, worldWidth/2, particlesAmount, minRadius, maxRadius, minSpeed, maxSpeed);

        BufferedWriter bw = new BufferedWriter(new FileWriter("p5/simulation-animator/random.txt"));
		bw.write(worldHeight + " " + worldWidth + " " + particlesAmount + "\n");
		for (Particle p: generatedParticles) {
			bw.write(p.getX() + " " + p.getY() + " " + p.getRadius() + " " + p.getVelocity().get(0) + " " + p.getVelocity().get(1) +  "\n");
        }
        bw.close();

    }

}
