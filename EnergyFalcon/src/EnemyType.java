
public enum EnemyType {
	GENERIC_ENEMY(2),
	/*WIZARD(4),*/
	PILUM_THROWER(3);
	
	private final int difficulty;
	
	EnemyType(int difficulty){
		this.difficulty = difficulty;
	}
	
	int getDifficulty(){
		return difficulty;
	}
	
	Enemy createEnemy(Player p){
		if(this.compareTo(GENERIC_ENEMY) == 0){
			return new GenericEnemy(p);
		/*}else if(this.compareTo(WIZARD) == 0){
			return new Wizard(p);*/
		}else if(this.compareTo(PILUM_THROWER) == 0){
			return new PilumThrower(p);
		}
		return null;
	}
}
