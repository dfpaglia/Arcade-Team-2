import java.awt.Image;
import java.io.IOException;

import javax.imageio.ImageIO;

import arcadia.Game;

public class PlayerHealth {

	private static final long HIT_TIME_DELTA = 1000000000L; //Wait 1 second between hits
	
	private Image[] playerHealthSprite = new Image[4];
	private Image endImage;
	private int scalar = 124;
	public int health = 4;
	public int healthCeiling = 4;
	public long hitTime = 0;
	public long hitTimeCeiling = 0;
	
	
	public PlayerHealth(){
	
		try {
			playerHealthSprite[0] = ImageIO.read(this.getClass().getResource("Banner1.png")).getScaledInstance(scalar, scalar, 0);
			playerHealthSprite[1] = ImageIO.read(this.getClass().getResource("Banner2.png")).getScaledInstance(scalar, scalar, 0);
			playerHealthSprite[2] = ImageIO.read(this.getClass().getResource("Banner3.png")).getScaledInstance(scalar, scalar, 0);
			playerHealthSprite[3] = ImageIO.read(this.getClass().getResource("Banner4.png")).getScaledInstance(scalar, scalar, 0);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void heal() {
		if(health < healthCeiling)
			health = healthCeiling;
	}
	
	public int getPlayerHealth(){
		return health;
	}
	
	public void hurt() {
		hitTime = System.nanoTime();
		if(hitTime >= hitTimeCeiling){
			health--;
			hitTimeCeiling = hitTime + HIT_TIME_DELTA;
		}
	}
	
	public boolean canBeHurt(){
		hitTime = System.nanoTime();
		if(hitTime >= hitTimeCeiling){
			return true;
		}
		return false;
	}
	
	public Image healthDraw(){
		
		switch(health){
		case 4:
			return playerHealthSprite[0];
			
		case 3:
			return playerHealthSprite[1];
			
		case 2:
			return playerHealthSprite[2];
	
		case 1:
			return playerHealthSprite[3];
		case 0:
			return null;
		default:
			return null;
		}
		
		
	}
}
	


