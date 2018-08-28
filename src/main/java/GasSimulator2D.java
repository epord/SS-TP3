import org.la4j.vector.dense.BasicVector;

import java.util.Collection;

public class GasSimulator2D {

    private Collection<Particle> particles;
    private  Collection<Obstacle> obstacles;

    public GasSimulator2D(Collection<Particle> particles, double worldHeight, double worldWidth) {
        this.particles = particles;
        // generate obstacles
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

        BasicVector newVelocity = new BasicVector(new double[] {
                collisionMatrix[0][0] * p1.getVelocity().get(0) + collisionMatrix[0][1] * p1.getVelocity().get(1),
                collisionMatrix[1][0] * p1.getVelocity().get(0) + collisionMatrix[1][1] * p1.getVelocity().get(1)
        });
        p1.setVelocity(newVelocity);

        newVelocity = new BasicVector(new double[] {
                collisionMatrix[0][0] * p2.getVelocity().get(0) + collisionMatrix[0][1] * p2.getVelocity().get(1),
                collisionMatrix[1][0] * p2.getVelocity().get(0) + collisionMatrix[1][1] * p2.getVelocity().get(1)
        });
        p2.setVelocity(newVelocity);
    }
}
