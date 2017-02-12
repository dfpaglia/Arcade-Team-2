import java.awt.Image;
import java.io.IOException;

import javax.imageio.ImageIO;

public class PlayerHealth {

	private Image[] playerHealthSprite = new Image[4];
	private int scalar = 124;
	
	public PlayerHealth(){
	
		try {
			playerHealthSprite[0] = ImageIO.read(this.getClass().getResource("Banner1.png")).getScaledInstance(scalar, scalar, 0);
			playerHealthSprite[1] = ImageIO.read(this.getClass().getResource("Banner2.png")).getScaledInstance(scalar, scalar, 0);
			playerHealthSprite[2] = ImageIO.read(this.getClass().getResource("Banner3.png")).getScaledInstance(scalar, scalar, 0);
			playerHealthSprite[3] = ImageIO.read(this.getClass().getResource("Banner1.png")).getScaledInstance(scalar, scalar, 0);
		
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public Image healthDraw(int health){
		
		switch(health){
		case 4:
			return playerHealthSprite[0];
			
		case 3:
			return playerHealthSprite[1];
			
		case 2:
			return playerHealthSprite[2];
	
		case 1:
			return playerHealthSprite[3];
		default:
			return null;
		}
		
		
	}
}
	


