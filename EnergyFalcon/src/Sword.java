import java.awt.Graphics2D;

import arcadia.Button;
import arcadia.Input;

public class Sword {
	private static final long SWORD_SWING_DELTA = 500000000L;
	private static final long SWORD_DELAY_DELTA = 250000000L;
	private static final double SWORD_RADIUS = 40;
	
	private boolean isSwinging = false;
	private boolean canSwing = true;
	
	private long endSwing = 0;
	private long endDelay = 0;
	
	private double lastX;
	private double lastY;
	
	private class SwordHurtbox extends CircularCollision{
		public SwordHurtbox(double x, double y, double r){
			super(x,y,r,CollisionType.PLAYER_HURTBOX_COLLISION);
		}
		
		void onCollide(CollisionType t, Object extraData) {
			switch(t){
			case ENEMY_HITBOX_COLLISION:
				//TODO add enemy hurt code here
				System.out.println("Hit an enemy");
				break;
			default:
				break;
			
			}
		}
	}
	
	private SwordHurtbox hurtbox;
	
	public void onTick(Input i, Player p){
		long curtime = System.nanoTime();
		if(i.pressed(Button.A) && canSwing){
			isSwinging = true;canSwing = false;
			endSwing = curtime + SWORD_SWING_DELTA;
			endDelay = endSwing + SWORD_DELAY_DELTA;
			switch(p.getDirection()){
				case DOWN:
					hurtbox = new SwordHurtbox(p.getX(), p.getY() + Player.getPlayerHeight()/2, SWORD_RADIUS);
					break;
				case LEFT:
					hurtbox = new SwordHurtbox(p.getX() - Player.getPlayerWidth()/2, p.getY(), SWORD_RADIUS);
					break;
				case RIGHT:
					hurtbox = new SwordHurtbox(p.getX() + Player.getPlayerWidth()/2, p.getY(), SWORD_RADIUS);
					break;
				case UP:
					hurtbox = new SwordHurtbox(p.getX(), p.getY() - Player.getPlayerHeight()/2, SWORD_RADIUS);
					break;
			}
			lastY = p.getY();
			lastX = p.getX();
		}
		
		if(isSwinging && curtime > endSwing){
			isSwinging = false;
			hurtbox.destruct();
			hurtbox = null;
		}
		
		if(!canSwing && curtime > endDelay){
			canSwing = true;
		}
		
		if(hurtbox != null){
			double dx = p.getX() - lastX;
			double dy = p.getY() - lastY;
			
			hurtbox.setPos(hurtbox.getX() + dx, hurtbox.getY() + dy);
			lastX = p.getX();
			lastY = p.getY();
		}
	}
	
	void draw( Graphics2D g){
		if(hurtbox!=null){
			hurtbox.drawCollision(g);
		}
	}
}
