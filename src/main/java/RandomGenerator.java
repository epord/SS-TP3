import java.util.ArrayList;
import java.util.Collection;
import java.util.Random;

public class RandomGenerator {

    public Collection<Particle> generateParticles(int worldHeight, int worldWidth, int particlesAmount, double minRadius, double maxRadius) {
        Collection<Particle> particles = new ArrayList<Particle>();
        Random random = new Random();
        int id = 0;
        for (int i = 0; i < particlesAmount; i++) {
            int tries = 10000000;
            boolean generated = false;
            do {
                double x = random.nextDouble() * (worldWidth);
                double y = random.nextDouble() * (worldHeight);
                double radius = random.nextDouble() * (maxRadius - minRadius) + minRadius;
                Particle newParticle = new ParticleImpl(id++, radius, x, y);
                if (isInBounds(newParticle, worldHeight, worldWidth) && !isCollidingWithExistingParticles(newParticle, particles)) {
                    generated = true;
                    particles.add(newParticle);
                }
            } while (--tries != 0 && !generated);

        }
        return particles;
    }

    private boolean isInBounds(Particle particle, int worldHeight, int worldWidth) {
        return particle.getX() - particle.getRadius() > 0
                && particle.getX() + particle.getRadius() < worldWidth
                && particle.getY() - particle.getRadius() > 0
                && particle.getY() + particle.getRadius() < worldHeight;
    }

    private boolean isCollidingWithExistingParticles(Particle particle, Collection<Particle> particles) {
        for (Particle p: particles) {
            if (p.isCollidingWith(particle)) {
                return true;
            }
        }
        return false;
    }

}
