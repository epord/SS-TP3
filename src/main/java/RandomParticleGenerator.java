import java.util.ArrayList;
import java.util.Collection;
import java.util.Random;

public class RandomParticleGenerator {

    public Collection<Particle> generateParticles(double worldHeight, double worldWidth, int particlesAmount, double minRadius, double maxRadius, double speedModule) {
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
                double speedX = random.nextDouble();
                speedX = random.nextBoolean() ? speedX * -1 : speedX;
                double speedY = random.nextDouble();
                speedY = random.nextBoolean() ? speedY * -1 : speedY;
                double speed = Math.sqrt(speedX * speedX + speedY * speedY);
                Particle newParticle = new ParticleImpl(id++, radius, x, y, speedX/speed * speedModule, speedY/speed * speedModule);
                speed = Math.sqrt(Math.pow(newParticle.getVelocity().get(0), 2) + Math.pow(newParticle.getVelocity().get(1), 2));
                newParticle.getVelocity().norm();
                if (isInBounds(newParticle, worldHeight, worldWidth) && !isCollidingWithExistingParticles(newParticle, particles)) {
                    generated = true;
                    particles.add(newParticle);
                }
            } while (--tries != 0 && !generated);

        }
        System.out.println(particles.size() + " particles generated\n");
        return particles;
    }

    private boolean isInBounds(Particle particle, double worldHeight, double worldWidth) {
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
