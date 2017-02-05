import java.awt.Color;
import java.awt.Graphics2D;

import arcadia.Input;

public class GenericEnemy implements Actor{
	private static final int ENEMY_WIDTH=100;
	private static final int ENEMY_HEIGHT=100;

	private Player p;
	private double x=400, y=250;
	double speed = 3.0;

	public GenericEnemy(Player p){
		this.p = p;
	}
	
	@Override
	public void onTick(Input input) {
		// TODO Auto-generated method stub
		double moveToX = p.getX();
		double moveToY = p.getY();
		
		double diffX = moveToX - x;
		double diffY = moveToY - y;

		float angle = (float)Math.atan2(diffY, diffX);

		x += speed * Math.cos(angle);
		y += speed * Math.sin(angle);
		
		
	}

	@Override
	public void draw(Graphics2D g) {
		g.setColor(Color.red);
		g.fillOval((int)Math.round(x-(ENEMY_WIDTH/2)), (int)Math.round(y-(ENEMY_HEIGHT/2)), ENEMY_WIDTH, ENEMY_HEIGHT);
	}

	@Override
	public Collider getCollider() {
		// TODO Auto-generated method stub
		return null;
	}

	public double getX() {
		return x;
	}

	public double getY() {
		return y;
	}

}

