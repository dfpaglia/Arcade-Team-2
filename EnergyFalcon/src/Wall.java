
public class Wall {
	private BoxCollision collider;
	public static final int LEFT_WALL = 0;
	public static final int TOP_WALL = 1;
	public static final int RIGHT_WALL = 2;
	public static final int BOTTOM_WALL = 3;
	
	public Wall(double x, double y, double width, double height){
		collider = new BoxCollision(x, y, width, height);
	}
	
	public BoxCollision getCollision(){
		return collider;
	}
}
