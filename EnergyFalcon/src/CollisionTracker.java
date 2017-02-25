import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class CollisionTracker {
	private static ArrayList<Collider> enemyHurtboxes = new ArrayList<Collider>();
	private static ArrayList<Collider> playerHurtboxes = new ArrayList<Collider>();
	private static ArrayList<Collider> enemyHitboxes = new ArrayList<Collider>();
	private static ArrayList<Wall> walls = new ArrayList<Wall>();
	private static ArrayList<Collider> generalHurtboxes = new ArrayList<Collider>();
	private static Collider playerHitbox = null;
	private static Map<Collider, Object> extraData = new HashMap<Collider, Object>(25);
	
	public static void addCollider(Collider c,  CollisionType type){
		switch(type){
			case ENEMY_HITBOX_COLLISION:
				enemyHitboxes.add(c);
				break;
			case ENEMY_HURTBOX_COLLISION:
				enemyHurtboxes.add(c);
				break;
			case HURTBOX_GENERAL:
				generalHurtboxes.add(c);
				break;
			case PLAYER_HITBOX_COLLISION:
				playerHitbox = c;
				break;
			case PLAYER_HURTBOX_COLLISION:
				playerHurtboxes.add(c);
				break;
			case WALL_COLLISION:
				if(c instanceof Wall){
					walls.add((Wall)c);
				}
				break;
			default:
				break;
			
		}
	}
	public static void addData(Collider c, Object data){
		extraData.put(c, data);
	}
	public static void handleCollisions(){
		//For every enemy hurtbox, check if it touches the player
		for(Collider c : enemyHurtboxes){
			if(c.collides(playerHitbox)){
				c.onCollide(CollisionType.PLAYER_HITBOX_COLLISION, extraData.get(playerHitbox));
				playerHitbox.onCollide(CollisionType.ENEMY_HURTBOX_COLLISION, extraData.get(c));
			}
		}
		//For every wall, see if it collides with the player hitbox
		for(Wall w : walls){
			if(w.collides(playerHitbox)){
				playerHitbox.onCollide(CollisionType.WALL_COLLISION, extraData.get(w));
				w.onCollide(CollisionType.PLAYER_HITBOX_COLLISION, extraData.get(playerHitbox));
			}
		}
		//check all player hurtbox with every enemy hitbox
		for(Collider c : playerHurtboxes){
			for(Collider ce : enemyHitboxes){
				if(c.collides(ce)){
					c.onCollide(CollisionType.ENEMY_HITBOX_COLLISION, extraData.get(ce));
					ce.onCollide(CollisionType.PLAYER_HURTBOX_COLLISION, extraData.get(c));
				}
			}
		}
		//TODO check all general hitboxes with both enemies and actors.
	}
	
	public static void removeCollision(Collider c, CollisionType type){
		extraData.remove(c);
		switch(type){
			case ENEMY_HITBOX_COLLISION:
				enemyHitboxes.remove(c);
				break;
			case ENEMY_HURTBOX_COLLISION:
				enemyHurtboxes.remove(c);
				break;
			case HURTBOX_GENERAL:
				generalHurtboxes.remove(c);
				break;
			case PLAYER_HITBOX_COLLISION:
				playerHitbox = null;
				break;
			case PLAYER_HURTBOX_COLLISION:
				playerHurtboxes.remove(c);
				break;
			case WALL_COLLISION:
				if(c instanceof Wall){
					walls.remove(c);
				}
				break;
			default:
				break;
		}
	}
}
