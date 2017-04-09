import java.awt.Color;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.Iterator;

import DavidMohrhardt.Animator;
import DavidMohrhardt.Sprite;
import arcadia.Input;

public class PilumThrower extends Enemy{
	private static final int ENEMY_WIDTH=75;
	private static final int ENEMY_HEIGHT=63;
	private static final int ENEMY_HEALTH = 3;
	
	private static final long FIREBALL_WAIT = 1800000000L; // Time after the wizard appears to when it throws its fireball. 1/3 seconds
	
	private boolean isVisible = true;
	private boolean isEnabled = true;
	private long fireTime = 0; 
	private EnemyHealth health = new EnemyHealth(ENEMY_HEALTH);
	private ArrayList<Pilum> pilums;
	
	//Sprite Stuff
	private Animator pilumA;
	private Sprite pilumS;
	private ArrayList<String> actions;
	
	private class PilumThrowerCollider extends BoxCollision{
		public PilumThrowerCollider(double x, double y, double width, double height) {
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
			return new CollisionData(this, health, EnemyType.PILUM_THROWER);
		}
	}
	
	private PilumThrowerCollider c = null;
	
	public PilumThrower(Player p){
		super(p, 0, 0);
		
		pilumA = new Animator("C:\\Users\\Taube\\git\\Arcade-Team-2\\EnergyFalcon\\src\\PilumThrowerSpriteSheet.png", "C:\\Users\\Taube\\git\\Arcade-Team-2\\EnergyFalcon\\src\\PilumThrower.ssc");
		pilumS = new Sprite("C:\\Users\\Taube\\git\\Arcade-Team-2\\EnergyFalcon\\src\\PilumThrowerSpriteSheet.png", "C:\\Users\\Taube\\git\\Arcade-Team-2\\EnergyFalcon\\src\\PilumThrower.ssc");
		actions = pilumS.getActions();
		
		
		long curTime = System.nanoTime();
		fireTime = curTime + FIREBALL_WAIT;
		pilums = new ArrayList<Pilum>();
		c = new PilumThrowerCollider(x, y, ENEMY_WIDTH, ENEMY_HEIGHT);
	}
	
	@Override
	public void onTick(Input input) {
		long curTime = System.nanoTime();
		if(!isEnabled) return;
		
		c.setPos(Math.floor(x - ENEMY_WIDTH/2), Math.floor(y - ENEMY_HEIGHT/2));
		
		if(curTime >= fireTime){
			//Cast fireball
			pilums.add(new Pilum(x, y, new Vector2D(p.getX()  - x, p.getY() - y, 1)));
			fireTime = curTime + FIREBALL_WAIT;
		}
		//Update all fireballs, delete ones marked for deletion.
		Iterator<Pilum> itPilum = pilums.iterator();
		Pilum f;
		while(itPilum.hasNext()){
			f = itPilum.next();
			f.onTick();
			if(f.shouldDelete()){
				itPilum.remove();
			}
		}
	}

	@Override
	public void draw(Graphics2D g) {
		if(isVisible){
			if(p.getX() <= getX())
				g.drawImage(pilumS.getFrame("AttackLeft", 0).getScaledInstance(ENEMY_WIDTH, ENEMY_HEIGHT, 0), (int)Math.floor(x - ENEMY_WIDTH/2), (int)Math.floor(y - ENEMY_HEIGHT/2), null);
			else if (p.getX() > getX())
				g.drawImage(pilumS.getFrame("AttackRight", 0).getScaledInstance(ENEMY_WIDTH, ENEMY_HEIGHT, 0), (int)Math.floor(x - ENEMY_WIDTH/2), (int)Math.floor(y - ENEMY_HEIGHT/2), null);
			//g.setColor(Color.CYAN);
			//g.fillRect((int)Math.floor(x - ENEMY_WIDTH/2), (int)Math.floor(y - ENEMY_HEIGHT/2), (int)(ENEMY_WIDTH), (int)(ENEMY_HEIGHT));
		}
		//dg.setColor(Color.red);
		for(Pilum f : pilums){
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
		Iterator<Pilum> itPilum = pilums.iterator();
		Pilum f;
		while(itPilum.hasNext()){
			f = itPilum.next();
			f.destruct();
			itPilum.remove();
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
	
	public double getX(){
		return x;
	}

	@Override
	public EnemyType getType() {
		return EnemyType.PILUM_THROWER;
	}

}
