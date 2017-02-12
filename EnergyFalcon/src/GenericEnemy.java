import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.io.IOException;

import javax.imageio.ImageIO;

import arcadia.Input;

public class GenericEnemy implements Actor{
	private static final int ENEMY_WIDTH=75;
	private static final int ENEMY_HEIGHT=63;

	private Player p;
	private double x=400, y=250;
	double speed = 3.0;
	private Image enemySprite;

	public GenericEnemy(Player p){
		this.p = p;
		
		try {
			
			enemySprite = ImageIO.read(this.getClass().getResource("EnemyTest1.png"));
		
		} catch (IOException e) {
			e.printStackTrace();
		}
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
		g.drawImage(enemySprite, (int)Math.round(x - (ENEMY_WIDTH/2)), (int)Math.round(y - (ENEMY_HEIGHT/2)), null);
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

