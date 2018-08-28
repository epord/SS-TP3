import java.util.*;

/**
 * Created by Mariano on 28/8/2018.
 */
public class CollisionsHandler {

	public static void calculateCollisions(Collection<Particle> particles, Collection<Obstacle> obstacles,
									PriorityQueue<Collision> collisions, Map<Particle, Set<Collision>> particleCollisions) {

		for (Particle particle : particles) {
			for (Particle p2 : particles) {
				Double collisionTime = particle.getCollisionTime(p2);
				if (collisionTime == null) continue;
				Collision collision = new Collision(collisionTime, particle, p2);
				if (!particleCollisions.containsKey(particle)) {
					particleCollisions.put(particle, new HashSet<>());
				}
				if (!particleCollisions.get(particle).contains(collision)) {
					particleCollisions.get(particle).add(collision);
					collisions.add(collision);
				}

				if (!particleCollisions.containsKey(p2)) {
					particleCollisions.put(p2, new HashSet<>());
				}
				if (!particleCollisions.get(p2).contains(collision)) {
					particleCollisions.get(p2).add(collision);
				}
			}

			for (Obstacle obstacle: obstacles) {
				Double collisionTime = obstacle.getCollisionTime(particle);
				if (collisionTime == null) continue;
				Collision collision = new Collision(collisionTime, particle, obstacle);
				if (!particleCollisions.containsKey(particle)) {
					particleCollisions.put(particle, new HashSet<>());
				}
				if (!particleCollisions.get(particle).contains(collision)) {
					particleCollisions.get(particle).add(collision);
					collisions.add(collision);
				}
			}
		}
	}
}
