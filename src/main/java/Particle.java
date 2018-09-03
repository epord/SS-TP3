import org.la4j.vector.dense.BasicVector;

public interface Particle extends PhysicalObject {

    Integer getId();
    Double getRadius();
    Double getX();
    Double getY();
    void setX(double x);
    void setY(double y);
    boolean isCollidingWith(Particle p);
    BasicVector getVelocity();
    void setVelocity(BasicVector v);
    Double getCollisionTime(Particle p);
    double getVelocityNorm();
}
