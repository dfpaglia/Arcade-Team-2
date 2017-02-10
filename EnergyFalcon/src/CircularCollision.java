
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
		if (c instanceof CircularCollision) {
			double xDif = x - c.getX();
			double yDif = y - c.getY();
			double distanceSquared = xDif * xDif + yDif * yDif;
			return distanceSquared < (r + ((CircularCollision) c).r) * (r + ((CircularCollision) c).r);
		}else if(c instanceof BoxCollision){
			//First, we'll check the corners
			double dx;
			double dy;
			double px;
			double py;
			BoxCollision b = (BoxCollision)c;
			//Corner 1:
			px = b.getX();
			py = b.getY();
			dx = x - px;
			dy = y - py;
			if(dx*dx + dy*dy < r*r){
				return true;
			}else{
				//Corner 2:
				px = px+b.getWidth();
				dx = x - px;
				if(dx*dx + dy*dy < r*r){
					return true;
				}else{
					//Corner 3:
					py = py+b.getHeight();
					dy = y - py;
					if(dx*dx + dy*dy < r*r){
						return true;
					}else{
						//Corner 4:
						px = b.getX();
						dx = x - px;
						if(dx*dx + dy*dy < r*r){
							return true;
						}
					}
				}
			}
			//Now check the edges
			//Right & left most edges:
			if(b.getY() < y && (b.getY() + b.getHeight()) > y){
				if((b.getX() - r)<x && (b.getX()+b.getWidth() + r)>x){
					return true;
				}
				//Up and bottom edges
			}else if(b.getX() < x && (b.getX()+b.getWidth()) > x){
				if((b.getY() - r) < y && (b.getY() + b.getHeight() + r) > y){
					return true;
				}
			}
		}
		return false;
	}

	@Override
	public void setPos(double x, double y) {
		this.x = x;
		this.y = y;
	}

	@Override
	public double getX() {
		return x;
	}

	@Override
	public double getY() {
		return y;
	}

	public double getR() {
		return r;
	}

}
