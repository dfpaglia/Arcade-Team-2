import java.awt.Graphics2D;
import java.awt.Image;
import java.io.IOException;

import java.util.ArrayList;

import javax.imageio.ImageIO;
import arcadia.Button;
import arcadia.Game;
import arcadia.Input;


import DavidMohrhardt.*;

public class Player implements Actor{

	private static final double ACCEL = 1;
	private static final double DECCEL = 1;
	// This sets the player sprite dimensions. Changing these will change the
	// size
	// of the sprite, without having to screw with image editing programs.
	private static final int PLAYER_WIDTH = 75;
	private static final int PLAYER_HEIGHT = 63;
	// Max Velocity (magnitude)
	private static final double V_MAX = 8.0;
	private static final double KNOCKBACK_VEL = 10.0;
	// X Y coordinates, relative to the top left of the screen.
	private double x, y;
	private Vector2D vel;
	private Vector2D knockbackVel;
	private PlayerHealth health;

	public PlayerHealth getHealth() {
		return health;
	}

	private Direction d;
	private Sword sword;
	
	private Animator playerSS;
	private Sprite pS;
	private ArrayList<String> actions;
	private Parry parry;

	// Nested collision class for player

	private class PlayerCollision extends BoxCollision {

		public PlayerCollision(double x, double y, double width, double height) {
			super(x, y, width, height, CollisionType.PLAYER_HITBOX_COLLISION);
		}

		public void onCollide(CollisionType t, CollisionData extraData) {
			switch (t) {
			case ENEMY_HURTBOX_COLLISION:
				if (health.canBeHurt()) {
					knockbackVel = new Vector2D(Player.this.x - extraData.getX() - (extraData.getWidth() / 2),
							Player.this.y - extraData.getY() - (extraData.getHeight() / 2), 1);
					knockbackVel = Vector2D.scale(Vector2D.unitVector(knockbackVel), KNOCKBACK_VEL);
					health.hurt();
				}
				break;
			case WALL_COLLISION:
				switch (extraData.getWall()) {
				case Wall.TOP_WALL:
					setY(Wall.TOP_WALL_EDGE + PLAYER_HEIGHT / 2);
					vel.setY(0);
					break;
				case Wall.BOTTOM_WALL:
					setY(Wall.BOTTOM_WALL_EDGE - PLAYER_HEIGHT / 2 - 1);
					vel.setY(0);
					break;
				case Wall.LEFT_WALL:
					setX(Wall.LEFT_WALL_EDGE + PLAYER_WIDTH / 2);
					vel.setX(0);
					break;
				case Wall.RIGHT_WALL:
					setX(Wall.RIGHT_WALL_EDGE - PLAYER_WIDTH / 2 - 1);
					vel.setX(0);
					break;
				}
				break;
			default:
				break;
			}
		}

		@Override
		CollisionData getCollisionData() {
			return new CollisionData(this, health);
		}
	}

	private PlayerCollision collision;

	public Player() {
		
		try {
			playerSS = new Animator(this.getClass().getResource("/SpriteSheetPC.png").openStream(), this.getClass().getResource("/Player.ssc").openStream());
			pS = new Sprite(this.getClass().getResource("/SpriteSheetPC.png").openStream(), this.getClass().getResource("/Player.ssc").openStream());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		actions = pS.getActions();

		collision = new PlayerCollision(x - PLAYER_WIDTH / 2, y - PLAYER_HEIGHT / 2, PLAYER_WIDTH, PLAYER_HEIGHT);
		health = new PlayerHealth();

		knockbackVel = new Vector2D(0, 0, 1);
		vel = new Vector2D(0, 0, 1);
		x = Game.WIDTH / 2;
		y = Game.HEIGHT / 2;

		d = Direction.DOWN;
		sword = new Sword(this);
		parry = new Parry(this);
	}

	public static int getPlayerWidth() {
		return PLAYER_WIDTH;
	}

	public static int getPlayerHeight() {
		return PLAYER_HEIGHT;
	}

	public int getPlayerHealth() {
		return health.getPlayerHealth();
	}

	public double getX() {
		return x;
	}

	public double getY() {
		return y;
	}

	public void setX(double x) {
		this.x = x;
	}

	public void setY(double y) {
		this.y = y;
	}

	// Method that should be called every tick.
	public void onTick(Input input) {
		calcNextPos(input);
		collision.setPos(x - PLAYER_WIDTH / 2, y - PLAYER_HEIGHT / 2);
		if(!parry.isParrying()){
			sword.onTick(input, this);
		}
		if(!sword.getIsSwinging()){
			parry.onTick(input, this);
		}
	}

	public Collider getCollider() {

		return collision;
	}

	
	//Let's say that 
	//Create a timer, check when the timer

	public void draw(Graphics2D g) {
		
		 //if the player is not attacking
		switch(getDirection()){
			case UP:
				if(sword.getIsSwinging() || parry.isParrying())
					g.drawImage(playerSS.getFrameAtIndex(actions.get(3), 0).getScaledInstance(PLAYER_WIDTH, PLAYER_HEIGHT, 0), (int)Math.round(x - (PLAYER_WIDTH/2)), (int)Math.round(y - (PLAYER_HEIGHT/2)), null);
				//Movement \/ and Attack /\
				else{
					g.drawImage(playerSS.startActionAnimation("WalkUp").getScaledInstance(PLAYER_WIDTH, PLAYER_HEIGHT, 0), (int)Math.round(x - (PLAYER_WIDTH/2)), (int)Math.round(y - (PLAYER_HEIGHT/2)), null);
					//g.drawImage(playerSS.getFrameAtIndex(actions.get(6), 0).getScaledInstance(PLAYER_WIDTH, PLAYER_HEIGHT, 0), (int)Math.round(x - (PLAYER_WIDTH/2)), (int)Math.round(y - (PLAYER_HEIGHT/2)), null);
				}
				break;
			case DOWN:
				if(sword.getIsSwinging() || parry.isParrying())
					g.drawImage(playerSS.getFrameAtIndex(actions.get(5), 0).getScaledInstance(PLAYER_WIDTH, PLAYER_HEIGHT, 0), (int)Math.round(x - (PLAYER_WIDTH/2)), (int)Math.round(y - (PLAYER_HEIGHT/2)), null);
				//Movement \/ and Attack /\
				else
					g.drawImage(playerSS.startActionAnimation("WalkDown").getScaledInstance(PLAYER_WIDTH, PLAYER_HEIGHT, 0), (int)Math.round(x - (PLAYER_WIDTH/2)), (int)Math.round(y - (PLAYER_HEIGHT/2)), null);
					//g.drawImage(playerSS.getFrameAtIndex(actions.get(4), 0).getScaledInstance(PLAYER_WIDTH, PLAYER_HEIGHT, 0), (int)Math.round(x - (PLAYER_WIDTH/2)), (int)Math.round(y - (PLAYER_HEIGHT/2)), null);
				break;
			case LEFT:
				if(sword.getIsSwinging() || parry.isParrying())
					g.drawImage(playerSS.getFrameAtIndex(actions.get(7), 0).getScaledInstance(PLAYER_WIDTH, PLAYER_HEIGHT, 0), (int)Math.round(x - (PLAYER_WIDTH/2)), (int)Math.round(y - (PLAYER_HEIGHT/2)), null);
				//Movement \/ and Attack /\
				else
					g.drawImage(playerSS.startActionAnimation("WalkLeft").getScaledInstance(PLAYER_WIDTH, PLAYER_HEIGHT, 0), (int)Math.round(x - (PLAYER_WIDTH/2)), (int)Math.round(y - (PLAYER_HEIGHT/2)), null);
					//g.drawImage(playerSS.getFrameAtIndex(actions.get(1), 0).getScaledInstance(PLAYER_WIDTH, PLAYER_HEIGHT, 0), (int)Math.round(x - (PLAYER_WIDTH/2)), (int)Math.round(y - (PLAYER_HEIGHT/2)), null);
				break;
			case RIGHT:
				if(sword.getIsSwinging() || parry.isParrying())
					g.drawImage(playerSS.getFrameAtIndex(actions.get(2), 0).getScaledInstance(PLAYER_WIDTH, PLAYER_HEIGHT, 0), (int)Math.round(x - (PLAYER_WIDTH/2)), (int)Math.round(y - (PLAYER_HEIGHT/2)), null);
				//Movement \/ and Attack /\
				else
					g.drawImage(playerSS.startActionAnimation("WalkRight").getScaledInstance(PLAYER_WIDTH, PLAYER_HEIGHT, 0), (int)Math.round(x - (PLAYER_WIDTH/2)), (int)Math.round(y - (PLAYER_HEIGHT/2)), null);
					//g.drawImage(playerSS.getFrameAtIndex(actions.get(0), 0).getScaledInstance(PLAYER_WIDTH, PLAYER_HEIGHT, 0), (int)Math.round(x - (PLAYER_WIDTH/2)), (int)Math.round(y - (PLAYER_HEIGHT/2)), null);
				break;
		}
		
		g.drawImage(health.healthDraw(), 0, 0, null);
		sword.draw(g);
		parry.draw(g);
	}

	private void calcNextPos(Input input) {
		// This vector will represent the current velocity
		// projected onto whatever axis (singular or multiple)
		// That isn't being pressed right now.
		Vector2D projection = new Vector2D(0, 0, 1);

		if (input.pressed(Button.U)) {
			vel = Vector2D.add(vel, new Vector2D(0, -ACCEL, 1));
			d = Direction.UP;
		} else if (input.pressed(Button.D)) {
			vel = Vector2D.add(vel, new Vector2D(0, ACCEL, 1));
			d = Direction.DOWN;
		} else {
			// if neither up or down is being pressed, then project the current
			// velocity
			// onto the y axis and add it to projection
			projection.setY(vel.getY());
		}

		if (input.pressed(Button.L)) {
			vel = Vector2D.add(vel, new Vector2D(-ACCEL, 0, 1));
			d = Direction.LEFT;
		} else if (input.pressed(Button.R)) {
			vel = Vector2D.add(vel, new Vector2D(ACCEL, 0, 1));
			d = Direction.RIGHT;
		} else {
			// if neither left or right is being pressed, then project the
			// current velocity
			// onto the x axis and add it to projection
			projection.setX(vel.getX());
		}
		// Scale the projection vector by our deceleration.
		projection = Vector2D.scale(Vector2D.unitVector(projection), DECCEL);
		// Take away velocity if projection is smaller in maginatude than vel,
		// otherwise set to 0.
		if (projection.getX() * projection.getX() > vel.getX() * vel.getX()) {
			vel.setX(0);
		} else {
			vel.setX(vel.getX() - projection.getX());
		}
		// Same as above, but for y.
		if (projection.getY() * projection.getY() > vel.getY() * vel.getY()) {
			vel.setY(0);
		} else {
			vel.setY(vel.getY() - projection.getY());
		}
		// Cap the velocity at V_MAX
		if (vel.magnitude() > V_MAX) {
			vel = Vector2D.scale(Vector2D.unitVector(vel), V_MAX);
		}
		// knockback
		if (knockbackVel.magnitude() < DECCEL) {
			knockbackVel.setX(0);
			knockbackVel.setY(0);
		} else {
			Vector2D knockbackDeccel = Vector2D.scale(Vector2D.unitVector(knockbackVel), -DECCEL);
			knockbackVel = Vector2D.add(knockbackDeccel, knockbackVel);
		}
		if(sword.getIsSwinging()){
			d = sword.getDirection();
		}else if(parry.isParrying()){
			d = parry.getDirection();
		}
		// Add velocity to position.
		x += vel.getX() + knockbackVel.getX();
		y += vel.getY() + knockbackVel.getY();
	}

	public Direction getDirection() {
		return d;
	}
	
	public void destruct(){
		collision.destruct();
		sword.destruct();
		parry.destruct();
	}
}
