import java.awt.Color;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;

import arcadia.Game;
import arcadia.Input;

public class SpawnTracker {
	private static final double START_DISTANCE = 400;
	private static final double FALL_SPEED = 10;
	private static final double SHADOW_DIAMETER = 50;
	private int POINTS = 0;

	ArrayList<Enemy> enemies;
	Map<Enemy, Vector2D> enemyToEndPos = new HashMap<Enemy, Vector2D>();
	int round;
	Random rand;
	Player p;

	public SpawnTracker(Player p) {
		enemies = new ArrayList<Enemy>();
		round = 1;
		this.p = p;
		rand = new Random();
		generateEnemies();
	}

	public void onTick(Input in) {
		if (enemies.size() == 0) {
			round++;
			generateEnemies();
		}

		Iterator<Enemy> it = enemies.iterator();
		Enemy e;
		Vector2D endPos;
		while (it.hasNext()) {
			e = it.next();
			endPos = enemyToEndPos.get(e);
			if (endPos != null) {
				double distance = (e.getX() - endPos.getX()) * (e.getX() - endPos.getX())
						+ (e.getY() - endPos.getY()) * (e.getY() - endPos.getY());
				e.setY(e.getY() + FALL_SPEED);
				if (distance <= FALL_SPEED * FALL_SPEED) {
					e.enableEnemy();
					e.setX(endPos.getX());
					e.setY(endPos.getY());
					enemyToEndPos.remove(e);
					e.onTick(in);
				}
			} else {
				e.onTick(in);
				if (e.isDead()) {
					POINTS += e.getType().getDifficulty();
					e.destruct();
					it.remove();
					if (POINTS >= 5) {
						POINTS = 0;
						p.getHealth().heal();
					}
				}
			}
		}
	}

	public void draw(Graphics2D g) {
		Vector2D fallPos;
		for (Enemy e : enemies) {
			g.setColor(Color.black);
			fallPos = enemyToEndPos.get(e);
			if (fallPos != null) {
				g.fillOval((int) (fallPos.getX() - SHADOW_DIAMETER / 2), (int) (fallPos.getY() - SHADOW_DIAMETER / 2),
						(int) SHADOW_DIAMETER, (int) SHADOW_DIAMETER);
			}
			e.draw(g);
		}
	}

	private void generateEnemies() {
		int numPoints = generateDifficulty();
		int randomInt;
		EnemyType[] enemArr = EnemyType.values();
		ArrayList<EnemyType> enemies = new ArrayList<EnemyType>();

		for (int i = 0; i < enemArr.length; i++) {
			enemies.add(enemArr[i]);
		}

		while (!enemies.isEmpty() && numPoints > 0) {
			randomInt = rand.nextInt(enemies.size());
			if (enemies.get(randomInt).getDifficulty() > numPoints) {
				enemies.remove(randomInt);
			} else {
				double x, y;
				Enemy enem = enemies.get(randomInt).createEnemy(p);
				this.enemies.add(enem);
				numPoints -= enemies.get(randomInt).getDifficulty();
				enem.disableEnemy();

				x = (Game.WIDTH - Wall.LEFT_WALL_EDGE - (Game.WIDTH - Wall.RIGHT_WALL_EDGE) - enem.getWidth())
						* rand.nextDouble() + Wall.LEFT_WALL_EDGE;
				y = (Game.HEIGHT - Wall.TOP_WALL_EDGE - (Game.HEIGHT - Wall.BOTTOM_WALL_EDGE) - enem.getHeight())
						* rand.nextDouble() + Wall.TOP_WALL_EDGE;

				enemyToEndPos.put(enem, new Vector2D(x, y, 1));

				enem.setX(x);
				enem.setY(y - START_DISTANCE);
			}
		}
	}

	private int generateDifficulty() {
		return (int) Math.round(Math.sqrt(round) + round);

	}
	
	public void destruct(){
		Iterator<Enemy> it = enemies.iterator();
		Enemy e;
		while (it.hasNext()) {
			e = it.next();
			e.destruct();
		}
	}
	
	public boolean winner() {

		return round > 5;

	}
}
