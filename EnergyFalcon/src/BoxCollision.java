
public class BoxCollision implements Collider {
	
	double x;
	double y;
	
	public double getHeight() {
		return 0;
	}
	
	public double getWidth() {
		return 0;
	}

	@Override
	public boolean collides(Collider c) {
		
		return false;
	}

	@Override
	public void setPos(double x, double y) {
	
		
	}

	@Override
	public double getX() {

		return 0;
	}

	@Override
	public double getY() {

		return 0;
	}


}
