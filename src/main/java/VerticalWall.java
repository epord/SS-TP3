/**
 * Created by Mariano on 28/8/2018.
 */
public class VerticalWall extends Wall {


	public VerticalWall(double x, double y1, double y2) {
		super(x, y1, x, y2);
	}

	public double getX() {
		return this.getX1();
	}

	@Override
	public Double getCollisionTime(Particle particle) {
		double tc1 = (getX() - particle.getX() - particle.getRadius()) / particle.getVelocity().get(0);
		double tc2 = (getX() - particle.getX() + particle.getRadius()) / particle.getVelocity().get(0);
		return Math.min(tc1, tc2);
	}
}