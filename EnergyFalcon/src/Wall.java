import arcadia.Game;

public class Wall extends BoxCollision{
	public static final int LEFT_WALL = 0;
	public static final int TOP_WALL = 1;
	public static final int RIGHT_WALL = 2;
	public static final int BOTTOM_WALL = 3;
	
	public static final double LEFT_WALL_EDGE = 80;
	public static final double TOP_WALL_EDGE = 40;
	public static final double RIGHT_WALL_EDGE = Game.WIDTH - 80;
	public static final double BOTTOM_WALL_EDGE = Game.HEIGHT - 80;
	
	private int wall = 0;
	
	public Wall(double x, double y, double width, double height, int wall){
		super(x,y,width,height, CollisionType.WALL_COLLISION);
		this.wall = wall;
	}
	
	public int getWall(){
		return wall;
	}

	public void onCollide(CollisionType t, Object extraData) {
		//TODO add stuff here if you want
	}
}
