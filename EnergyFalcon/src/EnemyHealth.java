
public class EnemyHealth {
	private int health;
	public long hitTime = 0;
	public long hitTimeCeiling = 0;
	private static final long HIT_TIME_DELTA = 1000000000L;
	
	
	public void hurt() {
		hitTime = System.nanoTime();
		if(hitTime >= hitTimeCeiling){
			health--;
			hitTimeCeiling = hitTime + HIT_TIME_DELTA;
		}
	}
	
	public int getEnemyHealth(){
		return health;
	}
	
	public EnemyHealth(int h){
		health = h;
	}
	
	
	
	
}
