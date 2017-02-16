import java.awt.Graphics2D;

public abstract class BoxCollision extends Collider {
	
	double x, y;
	double width, height;
	
	public BoxCollision(double x, double y, double width, double height, CollisionType type){
		super(type);
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
	}
	
	public double getHeight() {
		return height;
	}
	
	public double getWidth() {
		return width;
	}

	@Override
	public boolean collides(Collider c) {
		if(c instanceof BoxCollision){
			BoxCollision b = (BoxCollision)c;
			double x11, x21, x12, x22, y11, y21, y12, y22;
			x11 = x; x21 = b.getX(); x12 = x + width; x22 = b.getX() + b.getWidth();
			y11 = y; y21 = b.getY(); y12 = y + height; y22 = b.getY() + b.getHeight();
			//IS this rectangle in the other?
			if(x11 < x22 && x11 > x21 ){
				if(y11 < y22 && y11 > y21){
					return true;
				} 
				if(y12 < y22 && y11 > y21){
					return true;
				}
			}
			
			if(x12 < x22 && x12 > x21){
				if(y11 < y22 && y11 > y21){
					return true;
				}
				if(y12 < y22 && y12 > y21){
					return true;
				}
			}
			
			//Is the other rectangle in this one?
			if(x21 < x12 && x21 > x11 ){
				if(y21 < y12 && y21 > y11){
					return true;
				}
				if(y22 < y12 && y21 > y11){
					return true;
				}
			}
			
			if(x22 < x12 && x22 > x11 ){
				if(y21 < y12 && y21 > y11){
					return true;
				}
				if(y22 < y12 && y22 > y11){
					return true;
				}
			}
			
		}else if(c instanceof CircularCollision){
			return c.collides(this);
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
	
	public void drawCollision(Graphics2D g){
		g.drawRect((int)Math.round(x), (int)Math.round(y), (int)Math.round(width), (int)Math.round(height));
	}
	
	public abstract void onCollide(CollisionType t, Object extraData);
}
