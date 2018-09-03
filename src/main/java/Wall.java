
public abstract class Wall implements Obstacle {

    private Double x1, y1, x2, y2;
    protected double cumulatedImpulse = 0;
    private Boolean doubleSided;

    public Wall(double x1, double y1, double x2, double y2, Boolean doubleSided) {
        this.x1 = x1;
        this.y1 = y1;
        this.x2 = x2;
        this.y2 = y2;
        this.doubleSided = doubleSided;
    }

	public double getCumulatedImpulse() {
		return cumulatedImpulse;
	}

	public abstract void addImpulse(Particle p);

	public double getSurface() {
    	return Math.sqrt((x1-x2)*(x1-x2) + (y1-y2)*(y1-y2))*(doubleSided?2:1);
	}

	public Double getX1() {
		return x1;
	}

	public Double getY1() {
		return y1;
	}

	public Double getX2() {
		return x2;
	}

	public Double getY2() {
		return y2;
	}

	public abstract boolean isHorizontal();
	public abstract boolean isVertical();

	@Override
	public boolean equals(Object obj) {
		if (obj == null) return false;
		if (!(obj instanceof Wall)) return false;

		Wall other = (Wall) obj;

		return getX1().equals(other.getX1()) && getX2().equals(other.getX2()) && getY1().equals(other.getY1()) && getY2().equals(other.getY2());
	}

	@Override
	public int hashCode() {
		return (getX1().hashCode() + getY1().hashCode()) - (getX2().hashCode() + getY2().hashCode());
	}
}
