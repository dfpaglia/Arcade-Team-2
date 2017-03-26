import java.awt.Graphics2D;
import java.awt.Image;
import java.io.IOException;

import javax.imageio.ImageIO;

import arcadia.Input;

public class GenericEnemy extends Enemy{
	private static final int ENEMY_WIDTH=75;
	private static final int ENEMY_HEIGHT=63;
	private static final double ENEMY_KNOCKBACK_VEL = 10.0;
	private static final double ENEMY_MAX_SPEED = 3.0;
	private static final double ENEMY_ACCEL = 3.0;
	private static final double ENEMY_KNOCKBACK_DECCEL = 1.0;
	
	private Vector2D vel;
	private Vector2D knockbackVel;
	private Image enemySprite;
	private EnemyHealth h;
	private boolean isEnabled = true;
	
	private class EnemyCollision extends BoxCollision{

		public EnemyCollision(double x, double y, double width, double height, GenericEnemy e) {
			super(x, y, width, height, CollisionType.ENEMY_HITBOX_COLLISION, CollisionType.ENEMY_HURTBOX_COLLISION);
		}

		public void onCollide(CollisionType t, CollisionData extraData) {
			if(isEnabled){
				switch(t){
					case PLAYER_HITBOX_COLLISION:
						//TODO should something happen to this enemy if it collides with the player?
						break;
					case PLAYER_HURTBOX_COLLISION:
						if(h.canBeHurt()){
							knockbackVel = new Vector2D(GenericEnemy.this.x - extraData.getX() - (extraData.getWidth()/2), GenericEnemy.this.y - extraData.getY() - (extraData.getWidth()/2), 1);
							knockbackVel = Vector2D.scale(Vector2D.unitVector(knockbackVel), ENEMY_KNOCKBACK_VEL); 
							h.hurt();
						}
						break;
					case WALL_COLLISION:
						switch(extraData.getWall()){
							case Wall.TOP_WALL:
								GenericEnemy.this.y = Wall.TOP_WALL_EDGE + ENEMY_HEIGHT/2;
								vel.setY(0);
								break;
							case Wall.BOTTOM_WALL:
								GenericEnemy.this.y = Wall.BOTTOM_WALL_EDGE - ENEMY_HEIGHT/2 - 1;
								vel.setY(0);
								break;
							case Wall.LEFT_WALL:
								GenericEnemy.this.x = Wall.LEFT_WALL_EDGE + ENEMY_WIDTH/2;
								vel.setX(0);
								break;
							case Wall.RIGHT_WALL:
								GenericEnemy.this.x = Wall.RIGHT_WALL_EDGE - ENEMY_WIDTH/2 - 1;
								vel.setX(0);
								break;
						}
						break;
					default:
						break;
					
					}
			}
		}

		@Override
		CollisionData getCollisionData() {
			return new CollisionData(this, h, EnemyType.GENERIC_ENEMY);
		}
		
	};
	
	private EnemyCollision collision;
	
	public GenericEnemy(Player p){
		super(p, 0, 0);
		collision = new EnemyCollision(x-ENEMY_WIDTH/2,y-ENEMY_HEIGHT/2,ENEMY_WIDTH, ENEMY_HEIGHT, this);
		vel = new Vector2D(0, 0, 1);
		knockbackVel = new Vector2D(0, 0, 1);
		h = new EnemyHealth(5);
		try {
			
			enemySprite = ImageIO.read(this.getClass().getResource("EnemyTest1.png"));
		
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void onTick(Input input) {
		if(!isEnabled) return;
			
		Vector2D accel = new Vector2D(p.getX() - x, p.getY() - y, 1);
		accel = Vector2D.unitVector(accel);
		accel = Vector2D.scale(accel, ENEMY_ACCEL);
		vel = Vector2D.add(vel, accel);
		if(vel.magnitude() > ENEMY_MAX_SPEED){
			vel = Vector2D.unitVector(vel);
			vel = Vector2D.scale(vel, ENEMY_MAX_SPEED);
		} 
		//knockback
		if(knockbackVel.magnitude() < ENEMY_KNOCKBACK_DECCEL){
			knockbackVel.setX(0);
			knockbackVel.setY(0);
		}else{
			Vector2D knockBackDeccel = Vector2D.scale(Vector2D.unitVector(knockbackVel), -ENEMY_KNOCKBACK_DECCEL);
			knockbackVel = Vector2D.add(knockbackVel, knockBackDeccel);
		}
		x += vel.getX() + knockbackVel.getX();
		y += vel.getY() + knockbackVel.getY();
		collision.setPos(x-ENEMY_WIDTH/2, y-ENEMY_HEIGHT/2);
	}

	@Override
	public void draw(Graphics2D g) {
			g.drawImage(enemySprite, (int)Math.round(x - (ENEMY_WIDTH/2)), (int)Math.round(y - (ENEMY_HEIGHT/2)), null);
	}

	@Override
	public Collider getCollider() {
		return collision;
	}
	
	public int getEnemyHealth(){
		return h.getEnemyHealth();
	}

	@Override
	public boolean isDead() {
		return h.getEnemyHealth() <= 0;
	}

	@Override
	public void destruct() {
		collision.destruct();
	}

	@Override
	public void disableEnemy() {
		isEnabled = false;
	}

	@Override
	public void enableEnemy() {
		isEnabled = true;
	}

	@Override
	public double getWidth() {
		return ENEMY_WIDTH;
	}

	@Override
	public double getHeight() {
		return ENEMY_HEIGHT;
	}
}
