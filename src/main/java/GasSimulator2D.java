import java.util.Collection;

public class GasSimulator2D {

    private Collection<Particle> particles;
    private  Collection<Obstacle> obstacles;

    public GasSimulator2D(Collection<Particle> particles, double worldHeight, double worldWidth) {
        this.particles = particles;
        // generate obstacles
    }
}
