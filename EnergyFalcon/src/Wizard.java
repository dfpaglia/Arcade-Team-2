import java.awt.Color;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.Iterator;
import arcadia.Input;

public class Wizard extends Enemy {
	private static final int ENEMY_WIDTH=75;
	private static final int ENEMY_HEIGHT=63;
	private static final int ENEMY_HEALTH = 3;
	
	private static final long FIREBALL_WAIT = 1800000000L; // Time after the wizard appears to when it throws its fireball. 1/3 seconds
	
	private boolean isVisible = true;
	private boolean isEnabled = true;
	private long fireTime = 0; 
	private EnemyHealth health = new EnemyHealth(ENEMY_HEALTH);
	private ArrayList<Fireball> fire;
	
	private class WizardCollider extends BoxCollision{
		public WizardCollider(double x, double y, double width, double height) {
			super(x, y, width, height, CollisionType.ENEMY_HITBOX_COLLISION, CollisionType.ENEMY_HURTBOX_COLLISION);
		}

		void onCollide(CollisionType t, CollisionData extraData) {
			if(isEnabled){
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
		}

		CollisionData getCollisionData() {
			return new CollisionData(this, health, EnemyType.WIZARD);
		}
	}
	
	private WizardCollider c = null;
	
	public Wizard(Player p){
		super(p, 0, 0);
		long curTime = System.nanoTime();
		fireTime = curTime + FIREBALL_WAIT;
		fire = new ArrayList<Fireball>();
		c = new WizardCollider(x, y, ENEMY_WIDTH, ENEMY_HEIGHT);
	}
	
	@Override
	public void onTick(Input input) {
		long curTime = System.nanoTime();
		if(!isEnabled) return;
		
		c.setPos(Math.floor(x - ENEMY_WIDTH/2), Math.floor(y - ENEMY_HEIGHT/2));
		
		if(curTime >= fireTime){
			//Cast fireball
			fire.add(new Fireball(x, y, new Vector2D(p.getX()  - x, p.getY() - y, 1)));
			fireTime = curTime + FIREBALL_WAIT;
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
	}

	@Override
	public void draw(Graphics2D g) {
		if(isVisible){
			g.setColor(Color.CYAN);
			g.fillRect((int)Math.floor(x - ENEMY_WIDTH/2), (int)Math.floor(y - ENEMY_HEIGHT/2), (int)(ENEMY_WIDTH), (int)(ENEMY_HEIGHT));
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

	@Override
	public boolean isDead() {
		return health.getEnemyHealth() <= 0;
	}

	@Override
	public void destruct() {
		c.destruct();
		Iterator<Fireball> itFire = fire.iterator();
		Fireball f;
		while(itFire.hasNext()){
			f = itFire.next();
			f.destruct();
			itFire.remove();
		}
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
