import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

import arcadia.Game;

public class Pilum {
	private static final double DEFAULT_SPEED = 5.0;
	private static int PILUM_HEIGHT = 25;
	private static int PILUM_WIDTH = 100;
	private static final int NUM_COLLIDERS = 15;
	
	private Vector2D direction;
	private boolean shouldDelete = false;
	private boolean shouldDestruct = false;
	private BufferedImage pilumImage;
	private Image pil;
	private double x, y;
	
	private class PilumCollider extends CircularCollision{
		public PilumCollider(double x, double y, double r) {
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
			return new CollisionData(this);
		}
	}
	private PilumCollider[] c = new PilumCollider[NUM_COLLIDERS];
	
	public Pilum(double x, double y, Vector2D direction){
		
		try {
			pil = ImageIO.read(this.getClass().getResource("/Pilum.png")).getScaledInstance(PILUM_WIDTH, PILUM_HEIGHT, 0);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		double angle = Math.atan2(direction.getY(), direction.getX());
		this.direction = Vector2D.scale(Vector2D.unitVector(direction), DEFAULT_SPEED);
		pilumImage = new BufferedImage(2*PILUM_WIDTH, 2*PILUM_WIDTH, BufferedImage.TYPE_4BYTE_ABGR);
		AffineTransform t = new AffineTransform();
		t.rotate(angle, PILUM_WIDTH, PILUM_WIDTH);
		
		Graphics2D g = pilumImage.createGraphics();
		g.transform(t);
		g.drawImage(pil, PILUM_WIDTH, PILUM_WIDTH, null);
		//g.fillRect(PILUM_WIDTH, PILUM_WIDTH, PILUM_WIDTH, PILUM_HEIGHT);
		//g.drawLine(PILUM_WIDTH, PILUM_WIDTH + PILUM_HEIGHT/2, 2*PILUM_WIDTH, PILUM_WIDTH + PILUM_HEIGHT/2);
		
		
		this.x = x;
		this.y = y;
		for(int i = 0;i<NUM_COLLIDERS;i++){
			int dist = i*(PILUM_WIDTH/(NUM_COLLIDERS+1)) + (PILUM_WIDTH/(NUM_COLLIDERS+1));
			Vector2D shiftCurrent = Vector2D.rotate(new Vector2D(dist, PILUM_HEIGHT/2, 1), angle);
			c[i] = new PilumCollider(shiftCurrent.getX()+x, shiftCurrent.getY()+y, PILUM_HEIGHT/2);
		}
	}
	public Pilum(double x, double y, Vector2D direction, double speed){
		
		try {
			pil = ImageIO.read(this.getClass().getResource("Pilum.png")).getScaledInstance(PILUM_WIDTH, PILUM_HEIGHT, 0);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		double angle = Math.atan2(direction.getY(), direction.getX());
		this.direction = Vector2D.scale(Vector2D.unitVector(direction), speed);
		pilumImage = new BufferedImage(2*PILUM_WIDTH, 2*PILUM_WIDTH, BufferedImage.TYPE_4BYTE_ABGR);
		AffineTransform t = new AffineTransform();
		t.rotate(angle, PILUM_WIDTH, PILUM_WIDTH);
		
		Graphics2D g = pilumImage.createGraphics();
		g.transform(t);
		g.drawImage(pil, PILUM_WIDTH, PILUM_WIDTH, null);
		//g.fillRect(PILUM_WIDTH, PILUM_WIDTH, PILUM_WIDTH, PILUM_HEIGHT);
		
		this.x = x;
		this.y = y;
		for(int i = 0;i<NUM_COLLIDERS;i++){
			int dist = i*(PILUM_WIDTH/(NUM_COLLIDERS+2)) + (PILUM_WIDTH/(NUM_COLLIDERS+2));
			Vector2D shiftCurrent = Vector2D.rotate(new Vector2D(dist, PILUM_HEIGHT/2, 1), angle);
			c[i] = new PilumCollider(shiftCurrent.getX()+x, shiftCurrent.getY()+y, PILUM_HEIGHT/2);
		}
	}
	
	public void onTick(){
		if(!shouldDelete){
			for(int i = 0;i<c.length;i++){
				c[i].setPos(c[i].getX() + direction.getX(), c[i].getY() + direction.getY());
			}
			x+=direction.getX();
			y+=direction.getY();
			//Destroy the fireball if it's out of range
			if(c[c.length-1].getX() > Game.WIDTH || c[c.length-1].getX() < 0 || c[c.length-1].getY() > Game.HEIGHT || c[c.length-1].getY() < 0 ){
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
			g.drawImage(pilumImage, (int)(x - PILUM_WIDTH), (int)(y - PILUM_WIDTH), null);
			/*for(int i = 0;i<c.length;i++){
				g.fillOval((int)(c[i].getX() - PILUM_HEIGHT/2), (int)(c[i].getY() - PILUM_HEIGHT/2), PILUM_HEIGHT, PILUM_HEIGHT);
			}*/
		}
	}
	
	public boolean shouldDelete(){
		return shouldDelete;
	}
	
	public void destruct(){
		for(int i = 0;i<c.length;i++){
			c[i].destruct();
		}
	}
}
