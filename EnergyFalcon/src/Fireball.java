import java.awt.Graphics2D;

import arcadia.Game;

public class Fireball {
	
	private static final double DEFAULT_SPEED = 5.0;
	private static final double RADIUS = 15.0;
	
	private Vector2D direction;
	private boolean shouldDelete = false;
	private boolean shouldDestruct = false;
	
	private class FireballCollider extends CircularCollision{
		public FireballCollider(double x, double y, double r) {
			super(x, y, r, CollisionType.ENEMY_HURTBOX_COLLISION);
		}
		@Override
		void onCollide(CollisionType t, CollisionData extraData) {
			switch(t){
			case ENEMY_HITBOX_COLLISION:
				break;
			case ENEMY_HURTBOX_COLLISION:
				break;
			case HURTBOX_GENERAL:
				break;
			case PLAYER_HITBOX_COLLISION:
				shouldDelete = true;
				shouldDestruct = true;
				break;
			case PLAYER_HURTBOX_COLLISION:
				break;
			case WALL_COLLISION:
				break;
			default:
				break;
			
			}
		}
		
		@Override
		CollisionData getCollisionData() {
			return new CollisionData(Fireball.this);
		}
	}
	private FireballCollider c;
	
	public Fireball(double x, double y, Vector2D direction){
		this.direction = Vector2D.scale(Vector2D.unitVector(direction), DEFAULT_SPEED);
		c = new FireballCollider(x, y, RADIUS);
	}
	public Fireball(double x, double y, Vector2D direction, double speed){
		this.direction = Vector2D.scale(Vector2D.unitVector(direction), speed);
		c = new FireballCollider(x, y, RADIUS);
	}
	
	public void onTick(){
		if(!shouldDelete){
			c.setPos(c.getX() + direction.getX(), c.getY() + direction.getY());
			//Destroy the fireball if it's out of range
			if(c.getX() > Game.WIDTH || c.getX() < 0 || c.getY() > Game.HEIGHT || c.getY() < 0 ){
				shouldDelete = true;
				shouldDestruct = true;
			}
		}
		if(shouldDestruct){
			this.destruct();
			shouldDestruct = false;
		}
	}
	
	public void draw(Graphics2D g){
		if(!shouldDelete){
			g.fillOval((int)(c.getX() - RADIUS), (int)(c.getY() - RADIUS), (int)(2*RADIUS), (int)(2*RADIUS));
		}
	}
	
	public boolean shouldDelete(){
		return shouldDelete;
	}
	
	public void destruct(){
		c.destruct();
	}
}
