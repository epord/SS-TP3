import java.util.ArrayList;
import java.util.Collection;
import java.util.Random;

public class RandomGenerator {

    public Collection<Particle> generateParticles(int worldHeight, int worldWidth, int particlesAmount, double minRadius, double maxRadius) {
        Collection<Particle> particles = new ArrayList<Particle>();
        Random random = new Random();
        int id = 0;
        for (int i = 0; i < particlesAmount; i++) {
            double x = random.nextDouble() * (worldWidth);
            double y = random.nextDouble() * (worldHeight);
            double radius = random.nextDouble() * (maxRadius - minRadius) + minRadius;
            Particle newParticle = new ParticleImpl(id++, radius, x, y);


        }
        return particles;
    }

    private boolean isColliding(Particle particle, Collection<Particle> particles) {
        for (Particle p: particles) {
            if (p.isColliding(particle)) {
                return true;
            }
        }
        return false;
    }

}
