import java.awt.Color;
import java.awt.Graphics2D;

import arcadia.Input;

public class GenericEnemy implements Actor{
	private static final int ENEMY_WIDTH=100;
	private static final int ENEMY_HEIGHT=100;
	
	int x=200, y=200;
	
	public GenericEnemy(){
		
	}
	
	@Override
	public void onTick(Input input) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void draw(Graphics2D g) {
		g.setColor(Color.red);
		g.fillRect(x, y, ENEMY_WIDTH, ENEMY_WIDTH);
	}

	@Override
	public Collider getCollider() {
		// TODO Auto-generated method stub
		return null;
	}

}
