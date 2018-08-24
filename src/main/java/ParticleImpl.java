
public class ParticleImpl implements Particle{

    private int id;
    private double radius;
    private double x;
    private double y;

    public ParticleImpl(int id, double radius, double x, double y) {
        this.id = id;
        this.radius = radius;
        this.x = x;
        this.y = y;
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

    public boolean isCollidingWith(Particle particle) {
        double distance = Math.sqrt(Math.pow(particle.getX() - getX(), 2) + Math.pow(particle.getY() - getY(), 2));
        return distance < particle.getRadius() + getRadius();
    }

    @Override
    public String toString() {
        return "<id: " + id + " x: " + x + " y: " + y + " r: " + radius + ">";
    }
}
