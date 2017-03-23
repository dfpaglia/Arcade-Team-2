
public abstract class Enemy implements Actor{
	protected Player p;
	protected double x, y;
	public Enemy(Player p){
		this.p = p;
	}
	public abstract void disableEnemy();
	public abstract void enableEnemy();
	public abstract boolean isDead();
	public abstract void destruct();
}
