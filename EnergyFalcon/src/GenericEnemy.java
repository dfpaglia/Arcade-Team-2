import java.awt.Color;
import java.awt.Graphics2D;

import arcadia.Input;

public class GenericEnemy implements Actor{
	private static final int ENEMY_WIDTH=100;
	private static final int ENEMY_HEIGHT=100;

	private Player p;
	private double x=400, y=250;
	double speed = 3.0;
	private BoxCollision collision;
	
	public GenericEnemy(Player p){
		this.p = p;
		collision = new BoxCollision(x-ENEMY_WIDTH/2,y-ENEMY_HEIGHT/2,ENEMY_WIDTH, ENEMY_HEIGHT);
	}
	
	@Override
	public void onTick(Input input) {
		double moveToX = p.getX();
		double moveToY = p.getY();
		
		double diffX = moveToX - x;
		double diffY = moveToY - y;

		float angle = (float)Math.atan2(diffY, diffX);

		x += speed * Math.cos(angle);
		y += speed * Math.sin(angle);
		
		collision.setPos(x-ENEMY_WIDTH/2, y-ENEMY_HEIGHT/2);
		
	}

	@Override
	public void draw(Graphics2D g) {
		g.setColor(Color.red);
		g.fillOval((int)Math.round(x-(ENEMY_WIDTH/2)), (int)Math.round(y-(ENEMY_HEIGHT/2)), ENEMY_WIDTH, ENEMY_HEIGHT);
	}

	@Override
	public Collider getCollider() {
		return collision;
	}

	public double getX() {
		return x;
	}

	public double getY() {
		return y;
	}
}

