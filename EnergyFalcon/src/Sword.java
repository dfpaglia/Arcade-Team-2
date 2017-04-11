import java.awt.Graphics2D;
import java.awt.Image;
import java.io.IOException;

import javax.imageio.ImageIO;

import arcadia.Button;
import arcadia.Input;

public class Sword {
	
	private int direct;
	
	private static final long SWORD_SWING_DELTA = 500000000L;
	private static final long SWORD_DELAY_DELTA = 300000000L;
	private static final double SWORD_RADIUS = 50;
	
	private boolean isSwinging = false;
	private boolean canSwing = true;
	
	private long endSwing = 0;
	private long endDelay = 0;
	
	private double lastX;
	private double lastY;
	
	private Image swordSpriteUp, swordSpriteDown, swordSpriteLeft, swordSpriteRight;
	private Player p2;
	
	public boolean getIsSwinging(){
		return isSwinging;
	}
	
	public Sword(Player p){
		
		p2 = p;
		
		try {
			swordSpriteUp = ImageIO.read(this.getClass().getResource("PCSword.png")).getScaledInstance(75, 63, 0);
			swordSpriteDown = ImageIO.read(this.getClass().getResource("PCSword1.png")).getScaledInstance(75, 63, 0);
			swordSpriteLeft = ImageIO.read(this.getClass().getResource("PCSword3.png")).getScaledInstance(75, 63, 0);
			swordSpriteRight = ImageIO.read(this.getClass().getResource("PCSword2.png")).getScaledInstance(75, 63, 0);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private class SwordHurtbox extends CircularCollision{
		Player p;
		public SwordHurtbox(double x, double y, double r, Player p){
			super(x,y,r,CollisionType.PLAYER_HURTBOX_COLLISION);
			this.p = p;
			p2 = p;
		}
		
		void onCollide(CollisionType t, CollisionData extraData) {
			switch(t){
			case ENEMY_HITBOX_COLLISION:
				break;
			default:
				break;
			}
		}

		@Override
		CollisionData getCollisionData() {
			return new CollisionData((BoxCollision)p.getCollider());
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
					hurtbox = new SwordHurtbox(p.getX(), p.getY() + Player.getPlayerHeight()/2, SWORD_RADIUS, p);
					direct = 0;
					break;
				case LEFT:
					hurtbox = new SwordHurtbox(p.getX() - Player.getPlayerWidth()/2, p.getY(), SWORD_RADIUS, p);
					direct = 1;
					break;
				case RIGHT:
					hurtbox = new SwordHurtbox(p.getX() + Player.getPlayerWidth()/2, p.getY(), SWORD_RADIUS, p);
					direct = 2;
					break;
				case UP:
					hurtbox = new SwordHurtbox(p.getX(), p.getY() - Player.getPlayerHeight()/2, SWORD_RADIUS, p);
					direct = 3;
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
	
	public void draw( Graphics2D g){
		if(hurtbox!=null){
			switch(direct){ //0 is DOWN, 3 is UP, 1 is LEFT, 2 is RIGHT
				case 0:
					g.drawImage(swordSpriteDown, (int)p2.getX() - Player.getPlayerWidth()/2, (int)p2.getY() + Player.getPlayerHeight()/2, null);
					break;
				case 1:
					g.drawImage(swordSpriteLeft, (int)(p2.getX() - Player.getPlayerWidth() * 3/2.0), (int)p2.getY() - Player.getPlayerHeight()/2, null);
					break;
				case 2: 
					g.drawImage(swordSpriteRight, (int)p2.getX() + Player.getPlayerWidth()/2, (int)p2.getY() - Player.getPlayerHeight()/2, null);
					break;
				case 3:
					g.drawImage(swordSpriteUp, (int)p2.getX() - Player.getPlayerWidth()/2, (int)(p2.getY() - Player.getPlayerHeight() * 3/2.0), null);
					break;
			}
		}
	}
	public void destruct(){
		if(hurtbox!=null){
			hurtbox.destruct();
		}
	}
}
