/**
 * Created by Mariano on 28/8/2018.
 */
public class Collision {

	private Double collisionTime;
	private PhysicalObject object1;
	private PhysicalObject object2;

	public Collision(double collisionTime, PhysicalObject object1, PhysicalObject object2) {
		this.collisionTime = collisionTime;
		this.object1 = object1;
		this.object2 = object2;
	}

	public Double getCollisionTime() {
		return collisionTime;
	}

	public PhysicalObject getObject1() {
		return object1;
	}

	public PhysicalObject getObject2() {
		return object2;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) return false;

		Collision other = (Collision) obj;

		return this.getCollisionTime().equals(other.getCollisionTime()) &&
				((this.getObject1().equals(other.getObject1()) && this.getObject2().equals(other.getObject2())) ||
						(this.getObject1().equals(other.getObject2()) && this.getObject2().equals(other.getObject1())));
	}

	@Override
	public int hashCode() {
		return this.getCollisionTime().hashCode() - (this.getObject1().hashCode() + this.getObject2().hashCode());
	}

	@Override
	public String toString() {
		return "Collision: { " + getCollisionTime() + ", " + getObject1().toString() + ", " + getObject2().toString() + " }";
	}
}
