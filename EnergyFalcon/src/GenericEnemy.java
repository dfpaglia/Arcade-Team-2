import java.awt.Color;
import java.awt.Graphics2D;

import arcadia.Input;

public class GenericEnemy implements Actor{
	private static final int ENEMY_WIDTH=100;
	private static final int ENEMY_HEIGHT=100;
	private Player p;
	int x=400, y=250;
	double speed = 2.0;
	
	public GenericEnemy(Player p){
		this.p = p;
	}
	
	@Override
	public void onTick(Input input) {
		// TODO Auto-generated method stub
		int moveToX = p.getX();
		int moveToY = p.getY();
		
		int diffX = moveToX - x;
		int diffY = moveToY - y;

		float angle = (float)Math.atan2(diffY, diffX);

		x += speed * Math.cos(angle);
		y += speed * Math.sin(angle);
		
		
	}

	@Override
	public void draw(Graphics2D g) {
		g.setColor(Color.red);
		g.fillOval(x-(ENEMY_WIDTH/2), y-(ENEMY_HEIGHT/2), ENEMY_WIDTH, ENEMY_HEIGHT);
	}

	@Override
	public Collider getCollider() {
		// TODO Auto-generated method stub
		return null;
	}

}
