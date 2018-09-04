import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.*;
import java.util.stream.Collectors;

import experiments.ExperimentStatsHolder;
import experiments.ExperimentsStatsAgregator;
import org.la4j.vector.dense.BasicVector;

public class GasSimulator2D {

	private Collection<Particle> particles;
	private Collection<Obstacle> obstacles;
	private double worldWidth;
	private double worldHeight;
	private double temperature = 0;
	private String outFile = "p5/simulation-animator/output.txt";
	private ExperimentStatsHolder<GasMetrics> statsHolder;
	private Double simulationStaleTime = 0.0;

	public GasSimulator2D(Collection<Particle> particles, double worldWidth, double worldHeight) {
		this.particles = particles;
		this.worldWidth = worldWidth;
		this.worldHeight = worldHeight;
		// generate obstacles
		this.obstacles = new LinkedList<>();
		this.obstacles.add(new HorizontalWall(0, worldWidth, 0,false));
		this.obstacles.add(new HorizontalWall(0, worldWidth, worldHeight,false));
		this.obstacles.add(new VerticalWall(0, 0, worldHeight,false));
		this.obstacles.add(new VerticalWall(worldWidth, 0, worldHeight,false));
//		this.obstacles.add(new VerticalWall(worldWidth/2, 0, worldHeight/2));
		this.obstacles.add(new VerticalWall(worldWidth/2, 0, worldHeight/3 - .3,true));
		this.obstacles.add(new VerticalWall(worldWidth/2, worldHeight*2/3, worldHeight + .3,true));
		// calculate temperature
		for (Particle p: particles) {
			temperature += Math.pow(p.getVelocityNorm(), 2);
		}
		temperature /= particles.size();
		//Multiply by boltzmans constant
		temperature *= 1.38064852e10-23;
		statsHolder = new ExperimentStatsHolder<>();
	}

	public Double getPressureMean(Double timeElapsed){
		Double sum = 0.0;
		for (Obstacle o: obstacles) {
			sum += getPressure((Wall) o,timeElapsed);
		}
		return sum/obstacles.size();
	}

	public Double getPressureDifference(Double timeElapsed){
		List<Double> pressures = obstacles.stream()
				.map( o -> getPressure((Wall) o,timeElapsed)).collect(Collectors.toList());
		return ExperimentsStatsAgregator.getStandardDeviation(pressures);
	}

	public double getPressure(Wall w, double currentTime) {
		return w.getPressureAt(currentTime);
	}

	public double getBalance(){
		int countParticlesInSecondHalf = 0;
		for (Particle p: particles) {
			if (p.getX() > this.worldWidth / 2) countParticlesInSecondHalf++;
		}
		return (1.0*countParticlesInSecondHalf) / particles.size();
	}

	public double getTemperature() {
		return temperature;
	}

	public ExperimentStatsHolder<GasMetrics> simulate(Double timeLimit, Double timeStep, Integer frameLimit, Boolean equilibriumLimit) throws Exception{
		//Creamos la priority queue con todos los tiempos de colision
		PriorityQueue<Collision> collisions = new PriorityQueue<>(Comparator.comparing(Collision::getCollisionTime));
		Map<Particle, Set<Collision>> particleCollisions = new HashMap<>();

		CollisionsHandler.calculateCollisions(particles, obstacles, collisions, particleCollisions);

		double currentTime = 0;

		BufferedWriter bw = new BufferedWriter(new FileWriter("p5/simulation-animator/simulationInfo.txt"));
		bw.write(obstacles.size() + "\n");
		for (Obstacle o: obstacles) {
			if (o instanceof Wall) {
				bw.write(((Wall) o).getX1() + " " + ((Wall) o).getY1() + " " + ((Wall) o).getX2() + " " + ((Wall) o).getY2() + "\n");
			}
		}
		bw.close();

		bw = new BufferedWriter(new FileWriter("p5/simulation-animator/output.txt"));
		bw.write(worldWidth + " " + worldHeight + " " + particles.size() + "\n");
		bw.close();
		int printedFrameCount = 1;
		Double nextFrameTime = printedFrameCount * timeStep;
		writeParticlesToFile(particles,currentTime);

		// Simulation Loop
		while (printedFrameCount < frameLimit && currentTime < timeLimit && (!equilibriumLimit || currentTime < simulationStaleTime + timeStep *10)) {
			// Tomamos la colision de menor tiempo
			Collision collision = collisions.poll();
			Double nextCollisionTime = collision.getCollisionTime();

			Double deltaToNextCollision = nextCollisionTime - currentTime;

			//Mientras el tiempo de la proxima colision sea menor al tiempo de frame
			while (nextCollisionTime > nextFrameTime){
				//Obtener el delta hasta cuando habria que dibujar el next frame
				Double deltaToNextFrame = nextFrameTime - currentTime;
				evolveSystem(particles,deltaToNextFrame);
				writeParticlesToFile(particles,currentTime);

				currentTime = nextFrameTime;
				deltaToNextCollision = nextCollisionTime - currentTime;

				printedFrameCount++;
				nextFrameTime = printedFrameCount * timeStep;

				statsHolder.addDataPoint(GasMetrics.DY_PRESSURE, currentTime, getPressureMean(currentTime));
				statsHolder.addDataPoint(GasMetrics.P_DIFF, currentTime, getPressureDifference(currentTime));
				statsHolder.addDataPoint(GasMetrics.BALANCE, currentTime, getBalance());
			}

			// Evolucionamos el sistema hasta ese tiempo
			evolveSystem(particles, deltaToNextCollision);

			// Cambiamos la / las particulas según la colision originada
			calculateCollision(collision.getObject1(), collision.getObject2());

			//Ahora el tiempo actual es el nuevo
			currentTime = nextCollisionTime;

			// Calculamos nuevamente las colisiones de aquellas partículas que chocaron, con todas las demás y con las paredes.
			List<Particle> collisionedParticles = new LinkedList<>();
			if (collision.getObject1() instanceof Particle) collisionedParticles.add((Particle) collision.getObject1());
			if (collision.getObject2() instanceof Particle) collisionedParticles.add((Particle) collision.getObject2());
			CollisionsHandler.updateCollisions(particles, obstacles, collisions, particleCollisions, collisionedParticles, currentTime);

			if(!isInEquilibrium()){
				simulationStaleTime = currentTime;
			}
		}
		currentTime = (printedFrameCount - 1)*timeStep;
		statsHolder.addDataPoint(GasMetrics.TEMPERATURE,currentTime,temperature);
		statsHolder.addDataPoint(GasMetrics.EQ_TIME,currentTime,currentTime);
		statsHolder.addDataPoint(GasMetrics.EQ_PRESSURE,currentTime,getPressureMean(currentTime));

		System.out.println("Temperature: " + this.getTemperature());
		for (Obstacle o: obstacles) {
			System.out.println("Pressure: " + this.getPressure((Wall) o, currentTime));
		}
		System.out.println("Printed frames: " + printedFrameCount);
		System.out.println("Total duration: " + currentTime);
		return statsHolder;
	}

	public void writeParticlesToFile(Collection<Particle> particles, Double time){
		FileManager.appendParticlesTo(outFile, particles, time);
	}

	public boolean isInEquilibrium() {
		return getBalance() > 0.49;
	}

	public void calculateCollision(PhysicalObject o1, PhysicalObject o2) {
		if (o1 instanceof Particle && o2 instanceof Particle) {
			calculateCollision((Particle) o1, (Particle) o2);
		} else  if(o1 instanceof Wall || o2 instanceof Wall) {
			Wall wall = (Wall) (o1 instanceof Wall ? o1 : o2);
			Particle particle = (Particle) (o1 instanceof Wall ? o2 : o1);
			calculateCollision(particle, wall);
		}
	}

	public void calculateCollision(Particle p1, Wall w) {
		w.addImpulse(p1);
		if (w.isHorizontal()) {
			p1.setVelocity(new BasicVector(new double[]{p1.getVelocity().get(0), -p1.getVelocity().get(1)}));
		} else {
			p1.setVelocity(new BasicVector(new double[]{-p1.getVelocity().get(0), p1.getVelocity().get(1)}));
		}
	}

	public void calculateCollision(Particle p1, Particle p2) {
		double alpha = Math.atan((p2.getY() - p1.getY()) / (p2.getX() - p1.getX()));
		double[][] collisionMatrix = new double[2][2];
		double Cn = 1.0, Ct = 1.0;
		collisionMatrix[0][0] = -Cn * Math.pow(Math.cos(alpha), 2) + Ct * Math.pow(Math.sin(alpha), 2);
		collisionMatrix[0][1] = -(Cn + Ct) * Math.sin(alpha) * Math.cos(alpha);
		collisionMatrix[1][0] = -(Cn + Ct) * Math.sin(alpha) * Math.cos(alpha);
		collisionMatrix[1][1] = -Cn * Math.pow(Math.sin(alpha), 2) + Ct * Math.pow(Math.cos(alpha), 2);

		BasicVector newVelocity = new BasicVector(new double[]{
				collisionMatrix[0][0] * p1.getVelocity().get(0) + collisionMatrix[0][1] * p1.getVelocity().get(1),
				collisionMatrix[1][0] * p1.getVelocity().get(0) + collisionMatrix[1][1] * p1.getVelocity().get(1)
		});
		p1.setVelocity(newVelocity);

		alpha = Math.atan((p1.getY() - p2.getY()) / (p1.getX() - p2.getX()));
		collisionMatrix = new double[2][2];
		collisionMatrix[0][0] = -Cn * Math.pow(Math.cos(alpha), 2) + Ct * Math.pow(Math.sin(alpha), 2);
		collisionMatrix[0][1] = -(Cn + Ct) * Math.sin(alpha) * Math.cos(alpha);
		collisionMatrix[1][0] = -(Cn + Ct) * Math.sin(alpha) * Math.cos(alpha);
		collisionMatrix[1][1] = -Cn * Math.pow(Math.sin(alpha), 2) + Ct * Math.pow(Math.cos(alpha), 2);

		newVelocity = new BasicVector(new double[]{
				collisionMatrix[0][0] * p2.getVelocity().get(0) + collisionMatrix[0][1] * p2.getVelocity().get(1),
				collisionMatrix[1][0] * p2.getVelocity().get(0) + collisionMatrix[1][1] * p2.getVelocity().get(1)
		});
		p2.setVelocity(newVelocity);
	}

	private void evolveSystem(Collection<Particle> particles, Double deltaT) {
		particles.stream().forEach(p -> {
			p.setX(p.getX() + (p.getVelocity().get(0) * deltaT));
			p.setY(p.getY() + (p.getVelocity().get(1) * deltaT));
		});
	}
}
