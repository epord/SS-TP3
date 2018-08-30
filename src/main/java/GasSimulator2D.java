import java.util.*;

import org.la4j.vector.dense.BasicVector;

public class GasSimulator2D {

	private Collection<Particle> particles;
	private Collection<Obstacle> obstacles;

	public GasSimulator2D(Collection<Particle> particles, double worldWidth, double worldHeight) {
		this.particles = particles;
		// generate obstacles
		this.obstacles = new LinkedList<>();
		this.obstacles.add(new HorizontalWall(0, worldWidth, 0));
		this.obstacles.add(new HorizontalWall(0, worldWidth, worldHeight));
		this.obstacles.add(new VerticalWall(0, 0, worldHeight));
		this.obstacles.add(new VerticalWall(worldWidth, 0, worldHeight));

	}

	public void simulate(double timeLimit) {
		//Creamos la priority queue con todos los tiempos de colision
		PriorityQueue<Collision> collisions = new PriorityQueue<>(Comparator.comparing(Collision::getCollisionTime));
		Map<Particle, Set<Collision>> particleCollisions = new HashMap<>();

		CollisionsHandler.calculateCollisions(particles, obstacles, collisions, particleCollisions);

		double currentTime = 0;

		while(currentTime < timeLimit) {
			// Tomamos la colision de menor tiempo
			Collision collision = collisions.poll();
			System.out.println("Collision: " + collision.toString());
			Double newTime = collision.getCollisionTime();

			Double deltaT = newTime - currentTime;

			// Evolucionamos el sistema hasta ese tiempo
			evolveSystem(particles, deltaT);

			// Cambiamos la / las particulas según la colision originada
			calculateCollision(collision.getObject1(), collision.getObject2());

			// Calculamos nuevamente las colisiones de aquellas partículas que chocaron, con todas las demás y con las paredes.
			List<Particle> collisionedParticles = new LinkedList<>();
			if (collision.getObject1() instanceof Particle) collisionedParticles.add((Particle) collision.getObject1());
			if (collision.getObject2() instanceof Particle) collisionedParticles.add((Particle) collision.getObject2());
			CollisionsHandler.updateCollisions(particles, obstacles, collisions, particleCollisions, collisionedParticles);
		}
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
