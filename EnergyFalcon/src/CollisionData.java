public class CollisionData {
	private double x, y;
	private double width, height;
	private EnemyType enemy = null;
	private EnemyHealth he = null;
	private PlayerHealth hp = null;
	private int wallType = -1;
	private boolean isParry;
	
	private Fireball f;

	public CollisionData(Collider c) {
		x = c.getX();
		y = c.getY();
	}

	public CollisionData(BoxCollision c, EnemyHealth he, EnemyType e) {
		x = c.getX();
		y = c.getY();
		width = c.width;
		height = c.height;
		enemy = e;
		this.he = he;
	}

	public CollisionData(BoxCollision c, PlayerHealth h) {
		x = c.getX();
		y = c.getY();
		width = c.width;
		height = c.height;
		hp = h;
	}

	public CollisionData(BoxCollision c, int wall) {
		x = c.getX();
		y = c.getY();
		width = c.width;
		height = c.height;
		wallType = wall;
	}

	public CollisionData(BoxCollision c, Parry p) {
		x = c.getX();
		y = c.getY();
		width = c.width;
		height = c.height;
		isParry = true;
	}

	public CollisionData(Fireball f) {
		this.f = f;
	}

	public boolean isParry() {
		return isParry;
	}

	public boolean isFireball() {
		return f != null;
	}

	public int getWall() {
		return wallType;
	}

	public Fireball getFireball() {
		return f;
	}

	public EnemyHealth getEnemyHealth() {
		return he;
	}

	public PlayerHealth getPlayerHealth() {
		return hp;
	}

	public EnemyType getEnemy() {
		return enemy;
	}

	public double getX() {
		return x;
	}

	public double getY() {
		return y;
	}

	public double getWidth() {
		return width;
	}

	public double getHeight() {
		return height;
	}
}
