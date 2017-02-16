public abstract class Collider {
	CollisionType type;
	public Collider(CollisionType type){
		this.type = type;
		CollisionTracker.addCollider(this, type);
	}
	
	abstract boolean collides(Collider c);
	abstract void setPos(double x, double y);
	abstract double getX();
	abstract double getY();
	abstract void onCollide(CollisionType t, Object extraData);
	
	void destruct(){
		CollisionTracker.removeCollision(this, type);
	}
}

