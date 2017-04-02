public abstract class Enemy implements Actor{
	protected Player p;
	protected double x, y;
	public Enemy(Player p, double x, double y){
		this.p = p;
		this.x = x;
		this.y = y;
	}
	public abstract void disableEnemy();
	public abstract void enableEnemy();
	public abstract boolean isDead();
	public abstract void destruct();
	public abstract double getWidth();
	public abstract double getHeight();
	public double getX(){
		return x;
	}
	public double getY(){
		return y;
	}
	public void setX(double x){
		this.x = x;
	}
	public void setY(double y){
		this.y = y;
	}
}
