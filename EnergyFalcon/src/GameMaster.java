import java.awt.Graphics2D;
import java.awt.Image;
import java.io.IOException;

import javax.imageio.ImageIO;

import arcadia.Game;

//GameMaster controls win/lose and stuff
public class GameMaster {
	
	private Image win, lose;
	private Player p;
	private GenericEnemy en;
	
	public GameMaster(Player p, GenericEnemy en){
		this.p = p;
		this.en = en;
		
		try {
			
			win = ImageIO.read(this.getClass().getResource("Victory Screen.png")).getScaledInstance(Game.WIDTH, Game.HEIGHT, 0);
			lose = ImageIO.read(this.getClass().getResource("Defeat Screen.png")).getScaledInstance(Game.WIDTH, Game.HEIGHT, 0);
		
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void draw(Graphics2D g) {
		if(p.getPlayerHealth() <= 0){
			g.drawImage(lose, 0, 0, null);
		}
		
		if(en.getEnemyHealth() <= 0)
			g.drawImage(win, 0, 0, null);
	}
}
