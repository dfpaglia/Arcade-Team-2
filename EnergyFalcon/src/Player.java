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
	private static final double ACCEL = 1;
	private static final double DECCEL= 0.5;
	//This sets the player sprite dimensions. Changing these will change the size
	//of the sprite, without having to screw with image editing programs.
	private static final int PLAYER_WIDTH = 75;
	private static final int PLAYER_HEIGHT = 63;
	//Max Velocity (magnitude)
	private static final double V_MAX = 10;
	// X Y coordinates, relative to the top left of the screen.
	private double x, y;
	private Vector2D vel;
	private boolean accelerated = false;
	private Image playerSprite;
	//TODO instatiate collider
	private Collider collider;
	
	public Player() {
		try {
			playerSprite = ImageIO.read(this.getClass().getResource("FighterTest1.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		vel = new Vector2D(0,0,1);
		playerSprite = playerSprite.getScaledInstance(PLAYER_WIDTH, PLAYER_HEIGHT, 0);
		x = Game.WIDTH / 2;
		y = Game.HEIGHT / 2;
	}

	// Method that should be called every tick.
	public void onTick(Input input) {
		Vector2D projection = new Vector2D(0,0,1);
		accelerated = false;
		
		if (input.pressed(Button.U)) {
			vel = Vector2D.add(vel, new Vector2D(0, -ACCEL, 1));
			accelerated = true;
		}else if (input.pressed(Button.D)) {
			vel = Vector2D.add(vel, new Vector2D(0, ACCEL, 1));
			accelerated = true;
		}else{
			projection = Vector2D.add(projection, Vector2D.project(vel, new Vector2D(0, 1, 1)));
		}
		
		if (input.pressed(Button.L)) {
			vel = Vector2D.add(vel, new Vector2D(-ACCEL, 0, 1));
			accelerated = true;
		}else if (input.pressed(Button.R)) {
			vel = Vector2D.add(vel, new Vector2D(ACCEL, 0, 1));
			accelerated = true;
		}else{
			projection = Vector2D.add(projection, Vector2D.project(vel, new Vector2D(1, 0, 1)));
		}
		projection = Vector2D.scale(Vector2D.unitVector(projection), DECCEL);
		
		if(projection.getX()*projection.getX() > vel.getX()*vel.getX()){
			vel.setX(0);
		}else{
			vel.setX(vel.getX() - projection.getX());
		}
		
		if(projection.getY()*projection.getY() > vel.getY()*vel.getY()){
			vel.setY(0);
		}else{
			vel.setY(vel.getY() - projection.getY());
		}
		
		if(vel.magnitude() > V_MAX){
			vel = Vector2D.scale(Vector2D.unitVector(vel), V_MAX);
		}
		
		x += vel.getX();
		y += vel.getY();
	}
	
	public void draw(Graphics2D g) {
		g.drawImage(playerSprite, (int)Math.round(x - (PLAYER_WIDTH/2)), (int)Math.round(y - (PLAYER_HEIGHT/2)), null);
	}
	
	public Collider getCollider(){
		return collider;
	}
}
