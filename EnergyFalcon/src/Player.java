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
		calcNextPos(input);
	}
	
	public void draw(Graphics2D g) {
		g.drawImage(playerSprite, (int)Math.round(x - (PLAYER_WIDTH/2)), (int)Math.round(y - (PLAYER_HEIGHT/2)), null);
	}
	
	public Collider getCollider(){
		return collider;
	}
	
	private void calcNextPos(Input input){
		//This vector will represent the current velocity
		//projected onto whatever axis (singular or multiple)
		//That isn't being pressed right now.
		Vector2D projection = new Vector2D(0,0,1);
		
		if (input.pressed(Button.U)) {
			vel = Vector2D.add(vel, new Vector2D(0, -ACCEL, 1));
		}else if (input.pressed(Button.D)) {
			vel = Vector2D.add(vel, new Vector2D(0, ACCEL, 1));
		}else{
			//if neither up or down is being pressed, then project the current velocity
			// onto the y axis and add it to projection
			projection = Vector2D.add(projection, Vector2D.project(vel, new Vector2D(0, 1, 1)));
		}
		
		if (input.pressed(Button.L)) {
			vel = Vector2D.add(vel, new Vector2D(-ACCEL, 0, 1));
		}else if (input.pressed(Button.R)) {
			vel = Vector2D.add(vel, new Vector2D(ACCEL, 0, 1));
		}else{
			//if neither left or right is being pressed, then project the current velocity
			// onto the x axis and add it to projection
			projection = Vector2D.add(projection, Vector2D.project(vel, new Vector2D(1, 0, 1)));
		}
		//Scale the projection vector by our deceleration.
		projection = Vector2D.scale(Vector2D.unitVector(projection), DECCEL);
		//Take away velocity if projection is smaller in maginatude than vel,
		//otherwise set to 0.
		if(projection.getX()*projection.getX() > vel.getX()*vel.getX()){
			vel.setX(0);
		}else{
			vel.setX(vel.getX() - projection.getX());
		}
		//Same as above, but for y.
		if(projection.getY()*projection.getY() > vel.getY()*vel.getY()){
			vel.setY(0);
		}else{
			vel.setY(vel.getY() - projection.getY());
		}
		//Cap the velocity at V_MAX
		if(vel.magnitude() > V_MAX){
			vel = Vector2D.scale(Vector2D.unitVector(vel), V_MAX);
		}
		//Add velocity to position.
		x += vel.getX();
		y += vel.getY();
	}
}
