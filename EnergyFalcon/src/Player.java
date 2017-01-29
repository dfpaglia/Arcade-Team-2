import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.io.IOException;

import javax.imageio.ImageIO;

import arcadia.Button;
import arcadia.Game;
import arcadia.Input;

public class Player {
	//This sets the amount of pixels you move per tick.
	//Obviously, a higher number is faster, but less precise for movement.
	//If we want better movement(speed+precision), we need some sort of basic acceleration model.
	private static final int DELTA = 4;
	//X Y coordinates, relative to the top left of the screen.
	private int x, y;
	private Image playerSprite;
	
	public Player(){
		try {
			playerSprite = ImageIO.read(this.getClass().getResource("FighterTest1.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		x = (Game.WIDTH/2) - 50;
		y = (Game.HEIGHT/2) - 50;
	}
	//Method that should be called every tick.
	public void onTick(Input input){
		if(input.pressed(Button.U)){
			y-=DELTA;
		}
		if(input.pressed(Button.D)){
			y+=DELTA;
		}
		if(input.pressed(Button.L)){
			x-=DELTA;
		}
		if(input.pressed(Button.R)){
			x+=DELTA;
		}
	}
	
	public void draw(Graphics2D g){
		g.drawImage(playerSprite, x, y, null);
	}
}
