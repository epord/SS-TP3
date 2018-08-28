import org.la4j.vector.dense.BasicVector;

public class ParticleImpl implements Particle{

    private Integer id;
    private Double radius;
    private Double x;
    private Double y;
    private BasicVector velocity;

    public ParticleImpl(int id, double radius, double x, double y, double speedX, double speedY) {
        this.id = id;
        this.radius = radius;
        this.x = x;
        this.y = y;
        this.velocity = new BasicVector(new double[]{speedX, speedY});
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Double getRadius() {
        return radius;
    }

    public Double getX() {
        return x;
    }

    public Double getY() {
        return y;
    }

    public BasicVector getVelocity() {
        return velocity;
    }

	public void setX(double x) {
		this.x = x;
	}

	public void setY(double y) {
		this.y = y;
	}

	@Override
	public void setVelocity(BasicVector velocity) {
		this.velocity = velocity;
	}

	public boolean isCollidingWith(Particle particle) {
        double distance = Math.sqrt(Math.pow(particle.getX() - getX(), 2) + Math.pow(particle.getY() - getY(), 2));
        return distance < particle.getRadius() + getRadius();
    }

    /**
     *
     * @param particle
     * @return time until next collision (null if never colliding)
     */
    public Double getCollisionTime(Particle particle) {
        BasicVector deltaV = new BasicVector(new double[]{particle.getVelocity().get(0) - getVelocity().get(0), particle.getVelocity().get(1) - getVelocity().get(1)});
        BasicVector deltaR = new BasicVector(new double[]{particle.getX() - getX(), particle.getY() - getY()});
        double VdotR = deltaV.innerProduct(deltaR);
        if (VdotR >= 0) {
            return null;
        }
        double VdotV = deltaV.innerProduct(deltaV);
        double RdotR = deltaR.innerProduct(deltaR);
        double sigma = particle.getRadius() + getRadius();
        double d = Math.pow(VdotR, 2) - VdotV * (RdotR - sigma * sigma);
        if (d < 0) {
            return null;
        }
        return -1 * (VdotR + Math.sqrt(d))/VdotV;
    }

	@Override
	public boolean equals(Object obj) {
		if (obj == null) return false;

		Particle other = (Particle) obj;

		return this.getId().equals(other.getId()) && this.getRadius().equals(other.getRadius()) && this.getX().equals(other.getX())
				&& this.getY().equals(other.getY()) && this.getVelocity().equals(other.getVelocity());
	}

	@Override
	public int hashCode() {
		return getId().hashCode() + getRadius().hashCode() + getX().hashCode() + getY().hashCode() + getVelocity().hashCode();
	}

	@Override
    public String toString() {
        return "<id: " + id + " x: " + x + " y: " + y + " r: " + radius + ">";
    }
}
