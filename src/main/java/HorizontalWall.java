/**
 * Created by Mariano on 28/8/2018.
 */
public class HorizontalWall extends Wall {


	public HorizontalWall(double x1, double x2, double y) {
		super(x1, y, x2, y);
	}

	public double getY() {
		return this.getY1();
	}

	@Override
	public Double getCollisionTime(Particle particle) {
		double tc1 = (getY() - particle.getY() - particle.getRadius()) / particle.getVelocity().get(1);
		double tc2 = (getY() - particle.getY() + particle.getRadius()) / particle.getVelocity().get(1);
		double min = Math.min(tc1, tc2);
		if (min < 0) return null;
		double collisionX = particle.getX() + particle.getVelocity().get(0) * min;
		if (collisionX < getX1() || collisionX > getX2()) return null;
		return min;
	}

	@Override
	public void addImpulse(Particle p) {
		super.cumulatedImpulse += Math.abs(p.getVelocity().get(0)) * 2;
	}

	@Override
	public boolean isHorizontal() {
		return true;
	}

	@Override
	public boolean isVertical() {
		return false;
	}
}
