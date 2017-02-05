
public class CircularCollision implements Collider {

double x; 
double y;
double r;

public CircularCollision(double x, double y, double r) {
	this.x = x;
	this.y = y;
	this.r = r;
	
}
	
	@Override
	public boolean collides(Collider c) {
		if( c instanceof CircularCollision) {
		double xDif = x - c.getX();
		double yDif = y - c.getY();
		double distanceSquared = xDif * xDif + yDif * yDif;
		boolean collision = distanceSquared < (r + ((CircularCollision)c).r) * (r + ((CircularCollision)c).r);
		return collision;
		}
		return false;
	}

	@Override
	public void setPos(int x, int y) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public int getX() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getY() {
		// TODO Auto-generated method stub
		return 0;
	}

}