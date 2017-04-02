import java.util.ArrayList;

public class CollisionTracker {
	private static ArrayList<Collider> enemyHurtboxes = new ArrayList<Collider>();
	private static ArrayList<Collider> playerHurtboxes = new ArrayList<Collider>();
	private static ArrayList<Collider> enemyHitboxes = new ArrayList<Collider>();
	private static ArrayList<Wall> walls = new ArrayList<Wall>();
	private static ArrayList<Collider> generalHurtboxes = new ArrayList<Collider>();
	private static ArrayList<Collider> playerHitboxes = new ArrayList<Collider>();

	public static void addCollider(Collider c, CollisionType type) {
		switch (type) {
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
			playerHitboxes.add(c);
			break;
		case PLAYER_HURTBOX_COLLISION:
			playerHurtboxes.add(c);
			break;
		case WALL_COLLISION:
			if (c instanceof Wall) {
				walls.add((Wall) c);
			}
			break;
		default:
			break;

		}
	}

	public static void handleCollisions() {
		// For every enemy hurtbox, check if it touches the player
		for (Collider c : enemyHurtboxes) {
			for (Collider p : playerHitboxes) {
				if (c.collides(p)) {
					c.onCollide(CollisionType.PLAYER_HITBOX_COLLISION, p.getCollisionData());
					p.onCollide(CollisionType.ENEMY_HURTBOX_COLLISION, c.getCollisionData());
				}
			}
		}
		// For every wall, see if it collides with the player hitbox & enemy

		for (Wall w : walls) {
			for (Collider p : playerHitboxes) {
				if (w.collides(p)) {
					p.onCollide(CollisionType.WALL_COLLISION, w.getCollisionData());
					w.onCollide(CollisionType.PLAYER_HITBOX_COLLISION, p.getCollisionData());
				}
			}
			for (Collider e : enemyHitboxes) {
				if (w.collides(e)) {
					e.onCollide(CollisionType.WALL_COLLISION, w.getCollisionData());
					w.onCollide(CollisionType.ENEMY_HITBOX_COLLISION, e.getCollisionData());
				}
			}
		}
		// check all player hurtbox with every enemy hitbox
		for (Collider c : playerHurtboxes) {
			for (Collider ce : enemyHitboxes) {
				if (c.collides(ce)) {
					c.onCollide(CollisionType.ENEMY_HITBOX_COLLISION, ce.getCollisionData());
					ce.onCollide(CollisionType.PLAYER_HURTBOX_COLLISION, c.getCollisionData());
				}
			}
		}
		// TODO check all general hitboxes with both enemies and actors.
	}

	public static void removeCollision(Collider c, CollisionType type) {
		switch (type) {
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
			playerHitboxes.remove(c);
			break;
		case PLAYER_HURTBOX_COLLISION:
			playerHurtboxes.remove(c);
			break;
		case WALL_COLLISION:
			if (c instanceof Wall) {
				walls.remove(c);
			}
			break;
		default:
			break;
		}
	}
}
