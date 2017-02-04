import java.awt.Graphics2D;
import java.awt.Image;
import java.io.IOException;

import javax.imageio.ImageIO;

import arcadia.Button;
import arcadia.Game;
import arcadia.Input;

public class Player implements Actor{
	// This sets the amount of pixels you move per tick.
	// Obviously, a higher number is faster, but less precise for movement.
	// If we want better movement(speed+precision), we need some sort of basic
	// acceleration model.
	private static final int DELTA = 4;
	private static final int ACCEL = 4;
	private static final int DECCEL= 4;
	//This sets the player sprite dimensions. Changing these will change the size
	//of the sprite, without having to screw with image editing programs.
	private static final int PLAYER_WIDTH = 75;
	private static final int PLAYER_HEIGHT = 63;
	// X Y coordinates, relative to the top left of the screen.
	private double x, y;
	private Image playerSprite;
	//TODO instatiate collider
	private Collider collider;
	
	public Player() {
		try {
			playerSprite = ImageIO.read(this.getClass().getResource("FighterTest1.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		playerSprite = playerSprite.getScaledInstance(PLAYER_WIDTH, PLAYER_HEIGHT, 0);
		x = Game.WIDTH / 2;
		y = Game.HEIGHT / 2;
	}

	// Method that should be called every tick.
	public void onTick(Input input) {
		if (input.pressed(Button.U)) {
			y -= DELTA;
		}
		if (input.pressed(Button.D)) {
			y += DELTA;
		}
		if (input.pressed(Button.L)) {
			x -= DELTA;
		}
		if (input.pressed(Button.R)) {
			x += DELTA;
		}
	}
	public void draw(Graphics2D g) {
		g.drawImage(playerSprite, (int)Math.round(x - (PLAYER_WIDTH/2)), (int)Math.round(y - (PLAYER_HEIGHT/2)), null);
	}
	
	public Collider getCollider(){
		return collider;
	}
}
