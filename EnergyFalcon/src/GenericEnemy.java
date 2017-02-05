import java.awt.Color;
import java.awt.Graphics2D;

import arcadia.Input;

public class GenericEnemy implements Actor{
	private static final int ENEMY_WIDTH=100;
	private static final int ENEMY_HEIGHT=100;
	
	int x=400, y=250;
	
	public GenericEnemy(){
		
	}
	
	@Override
	public void onTick(Input input) {
		// TODO Auto-generated method stub
		
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