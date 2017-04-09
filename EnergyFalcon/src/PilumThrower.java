import java.awt.Color;
import java.awt.Graphics2D;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

import DavidMohrhardt.Animator;
import DavidMohrhardt.Sprite;
import arcadia.Input;

public class PilumThrower extends Enemy{
	private static final int ENEMY_WIDTH=75;
	private static final int ENEMY_HEIGHT=63;
	private static final int ENEMY_HEALTH = 3;
	private static final double ENEMY_KNOCKBACK_VEL = 10.0;
	private static final double ENEMY_KNOCKBACK_DECCEL = 1.0;
	
	private static final long FIREBALL_WAIT = 1800000000L; // Time after the wizard appears to when it throws its fireball. 1/3 seconds
	
	private boolean isVisible = true;
	private boolean isEnabled = true;
	private long fireTime = 0; 
	private EnemyHealth health = new EnemyHealth(ENEMY_HEALTH);
	private ArrayList<Pilum> pilums;
	
	private Vector2D knockbackVel;
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
					if (extraData.isParry()) {
						knockbackVel = new Vector2D(PilumThrower.this.x - extraData.getX() - (ENEMY_WIDTH / 2),
								PilumThrower.this.y - extraData.getY() - (ENEMY_HEIGHT / 2), 1);
						knockbackVel = Vector2D.scale(Vector2D.unitVector(knockbackVel), ENEMY_KNOCKBACK_VEL);
					}
					break;
				case PLAYER_HURTBOX_COLLISION:
					if (health.canBeHurt()) {
						knockbackVel = new Vector2D(PilumThrower.this.x - extraData.getX() - (extraData.getWidth() / 2),
								PilumThrower.this.y - extraData.getY() - (extraData.getHeight() / 2), 1);
						knockbackVel = Vector2D.scale(Vector2D.unitVector(knockbackVel), ENEMY_KNOCKBACK_VEL);
						health.hurt();
					}
					break;
				case WALL_COLLISION:
					switch (extraData.getWall()) {

					case Wall.TOP_WALL:
						PilumThrower.this.y = Wall.TOP_WALL_EDGE + ENEMY_HEIGHT / 2;
						break;
					case Wall.BOTTOM_WALL:
						PilumThrower.this.y = Wall.BOTTOM_WALL_EDGE - ENEMY_HEIGHT / 2 - 1;
						break;
					case Wall.LEFT_WALL:
						PilumThrower.this.x = Wall.LEFT_WALL_EDGE + ENEMY_WIDTH / 2;
						break;
					case Wall.RIGHT_WALL:
						PilumThrower.this.x = Wall.RIGHT_WALL_EDGE - ENEMY_WIDTH / 2 - 1;
						break;
					}
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
		
		try {
			pilumA = new Animator(this.getClass().getResource("PilumThrowerSpriteSheet.png").openStream(), this.getClass().getResource("/PilumThrower.ssc").openStream());
			pilumS = new Sprite(this.getClass().getResource("PilumThrowerSpriteSheet.png").openStream(), this.getClass().getResource("/PilumThrower.ssc").openStream());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		actions = pilumS.getActions();
		
		
		long curTime = System.nanoTime();
		fireTime = curTime + FIREBALL_WAIT;
		pilums = new ArrayList<Pilum>();
		c = new PilumThrowerCollider(x, y, ENEMY_WIDTH, ENEMY_HEIGHT);
		knockbackVel = new Vector2D(0,0,1);
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
		// knockback
		if (knockbackVel.magnitude() < ENEMY_KNOCKBACK_DECCEL) {
			knockbackVel.setX(0);
			knockbackVel.setY(0);
		} else {
			Vector2D knockBackDeccel = Vector2D.scale(Vector2D.unitVector(knockbackVel), -ENEMY_KNOCKBACK_DECCEL);
			knockbackVel = Vector2D.add(knockbackVel, knockBackDeccel);
		}
		x+=knockbackVel.getX();
		y+=knockbackVel.getY();
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
