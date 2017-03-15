import java.awt.Color;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

import arcadia.Game;
import arcadia.Input;

public class Wizard implements Actor {
	private static final int ENEMY_WIDTH=75;
	private static final int ENEMY_HEIGHT=63;
	private static final int ENEMY_HEALTH = 3;
	
	private static final long TELEPORT_COOLDOWN = 5000000000L; // Time between teleports. 5 seconds
	private static final long TELEPORT_TIME = 1000000000L; //Time the wizard is gone between teleports. 1 second
	private static final long FIREBALL_WAIT = 300000000L; // Time after the wizard appears to when it throws its fireball. 1/3 seconds
	
	private double x, y;
	private boolean isVisible = false;
	private long appearTime=0, teleTime = 0, fireTime = 0; 
	private EnemyHealth health = new EnemyHealth(ENEMY_HEALTH);
	private ArrayList<Fireball> fire;
	private Player p;
	
	private class WizardCollider extends BoxCollision{
		public WizardCollider(double x, double y, double width, double height) {
			super(x, y, width, height, CollisionType.ENEMY_HITBOX_COLLISION, CollisionType.ENEMY_HURTBOX_COLLISION);
		}

		void onCollide(CollisionType t, CollisionData extraData) {
			switch(t){
			case ENEMY_HITBOX_COLLISION:
				break;
			case ENEMY_HURTBOX_COLLISION:
				break;
			case HURTBOX_GENERAL:
				break;
			case PLAYER_HITBOX_COLLISION:
				break;
			case PLAYER_HURTBOX_COLLISION:
				if(health.canBeHurt()){
					health.hurt();
				}
				break;
			case WALL_COLLISION:
				break;
			default:
				break;
			
			}
		}

		CollisionData getCollisionData() {
			return new CollisionData(this, health, EnemyType.WIZARD);
		}
	}
	
	private WizardCollider c = null;
	
	public Wizard(Player p){
		long curTime = System.nanoTime();
		this.p = p;
		appearTime = curTime;
		fireTime = curTime + FIREBALL_WAIT;
		teleTime = curTime + TELEPORT_COOLDOWN;
		fire = new ArrayList<Fireball>();
	}
	
	@Override
	public void onTick(Input input) {
		long curTime = System.nanoTime();
		if(curTime >= appearTime){
			WizardCollider c = null;
			//TODO make sure that the seed is selected and not some default value
			Random r = new Random();
			isVisible = true;
			do{
				if(c!=null){
					c.destruct();
					c = null;
				}
				x = r.nextInt((int)(Game.WIDTH - Wall.LEFT_WALL_EDGE - (Game.WIDTH - Wall.RIGHT_WALL_EDGE) - ENEMY_WIDTH)) + Wall.LEFT_WALL_EDGE;
				y = r.nextInt((int)(Game.HEIGHT - Wall.TOP_WALL_EDGE - (Game.HEIGHT - Wall.BOTTOM_WALL_EDGE) - ENEMY_HEIGHT)) + Wall.TOP_WALL_EDGE;
				c = new WizardCollider(x, y, ENEMY_WIDTH, ENEMY_HEIGHT);
			}while(c.collides(p.getCollider()));
			this.c = c;
			x+=(ENEMY_WIDTH/2);
			y+=(ENEMY_HEIGHT/2);
			appearTime = Long.MAX_VALUE;
		}
		if(curTime >= fireTime){
			//Cast fireball
			fire.add(new Fireball(x, y, new Vector2D(p.getX()  - x, p.getY() - y, 1)));
			fireTime = Long.MAX_VALUE;
		}
		
		if(curTime >= teleTime){
			//recalculate times, make invisible, remove the collider.
			appearTime = curTime + TELEPORT_TIME;
			fireTime = appearTime + FIREBALL_WAIT;
			teleTime = appearTime + TELEPORT_COOLDOWN;
			isVisible = false;
			c.destruct();
			c = null;
		}
		//Update all fireballs, delete ones marked for deletion.
		Iterator<Fireball> itFire = fire.iterator();
		Fireball f;
		while(itFire.hasNext()){
			f = itFire.next();
			f.onTick();
			if(f.shouldDelete()){
				itFire.remove();
			}
		}
		if(health.getEnemyHealth() <= 0){
			System.out.println("dead");
		}
	}

	@Override
	public void draw(Graphics2D g) {
		if(isVisible){
			g.setColor(Color.CYAN);
			g.fillRect((int)(x - (ENEMY_WIDTH/2)), (int)(y - (ENEMY_HEIGHT/2)), (int)(ENEMY_WIDTH), (int)(ENEMY_HEIGHT));
		}
		g.setColor(Color.red);
		for(Fireball f : fire){
			f.draw(g);
		}
	}

	@Override
	public Collider getCollider() {
		return c;
	}

}
