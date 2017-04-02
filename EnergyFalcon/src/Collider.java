public abstract class Collider {
	CollisionType[] types;
	public Collider(CollisionType type){
		this.types = new CollisionType[1];
		this.types[0] = type;
		CollisionTracker.addCollider(this, type);
	}
	
	public Collider(CollisionType... types){
		this.types = types;
		for(CollisionType t : types){
			CollisionTracker.addCollider(this, t);
		}
	}
	abstract boolean collides(Collider c);
	abstract void setPos(double x, double y);
	abstract double getX();
	abstract double getY();
	abstract CollisionData getCollisionData();
	abstract void onCollide(CollisionType t, CollisionData extraData);
	
	void destruct(){
		for(int i = 0;i<types.length;i++){
			CollisionTracker.removeCollision(this, types[i]);
		}
	}
}
