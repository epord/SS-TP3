
public class ParticleImpl implements Particle{

    private int id;
    private double radius;
    private double x;
    private double y;
    private Velocity velocity;

    public ParticleImpl(int id, double radius, double x, double y, Velocity velocity) {
        this.id = id;
        this.radius = radius;
        this.x = x;
        this.y = y;
        this.velocity = velocity;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getRadius() {
        return radius;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public Velocity getVelocity() {
        return velocity;
    }

    public boolean isColliding(Particle particle) {
        double distance = Math.sqrt(Math.pow(particle.getX() - getX(), 2) + Math.pow(particle.getY() - getY(), 2));
        return distance < particle.getRadius() + getRadius();
    }

    @Override
    public String toString() {
        return id + " " + x + " " + y + " " + radius;
    }
}
