public interface Collider {
	boolean collides(Collider c);
	void setPos(double x, double y);
	double getX();
	double getY();
}

