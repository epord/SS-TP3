import org.la4j.vector.dense.BasicVector;

public interface Particle {

    int getId();
    double getRadius();
    double getX();
    double getY();
    boolean isCollidingWith(Particle p);
    BasicVector getVelocity();
    void setVelocity(BasicVector velocity);
}
