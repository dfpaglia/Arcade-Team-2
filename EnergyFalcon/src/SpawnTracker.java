import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;

import arcadia.Input;

public class SpawnTracker {
	private static final double START_DISTANCE = 400;
	private static final double FALL_SPEED = 10;
	private static final double SHADOW_RADIUS = 20;
	
	ArrayList<Enemy> enemies;
	Map<Enemy, Vector2D> enemyToEndPos = new HashMap<Enemy, Vector2D>();
	int round;
	Random rand;
	Player p;
	
	public SpawnTracker(Player p){
		enemies = new ArrayList<Enemy>();
		round = 1;
		this.p = p;
		rand = new Random();
	}
	
	public void onTick(Input in){
		if(enemies.size()==0){
			round++;
			generateEnemies();
		}
		
		Iterator<Enemy> it = enemies.iterator();
		Enemy e;
		while(it.hasNext()){
			e = it.next();
			e.onTick(in);
			if(e.isDead()){
				e.destruct();
				it.remove();
			}
		}
	}
	
	public void draw(Graphics2D g){
		for(Enemy e : enemies){
			e.draw(g);
		}
	}
	
	private void generateEnemies(){
		int numPoints = generateDifficulty();
		int randomInt;
		EnemyType[] enemArr = EnemyType.values();
		ArrayList<EnemyType> enemies = new ArrayList<EnemyType>();
		
		for(int i = 0;i<enemArr.length;i++){
			enemies.add(enemArr[i]);
		}
		
		while(!enemies.isEmpty() && numPoints > 0){
			randomInt = rand.nextInt(enemies.size());
			if(enemies.get(randomInt).getDifficulty() > numPoints){
				enemies.remove(randomInt);
			}else{
				this.enemies.add(enemies.get(randomInt).createEnemy(p));
				numPoints -= enemies.get(randomInt).getDifficulty();
			}
		}
	}
	
	private int generateDifficulty(){
		return (int)Math.round(Math.sqrt(round)+round);
	}
}
