import java.awt.Graphics2D;
import java.io.IOException;
import java.util.ArrayList;

import DavidMohrhardt.Animator;
import DavidMohrhardt.Sprite;
import arcadia.Button;
import arcadia.Input;

public class Parry {
	private static final long PARRY_DELTA = 500000000L;
	private static final long PARRY_DELAY_DELTA = 850000000L;

	private static final double PARRY_WIDTH = 10;
	private static final double PARRY_LENGTH = 68;

	private boolean isParrying = false;
	private boolean canParry = true;

	private long endParry = 0;
	private long endDelay = 0;

	private double lastX;
	private double lastY;
	
	private Animator shieldSS;
	private Sprite sS;
	private ArrayList<String> actions;
	
	private Direction direct;
	private Player p2;
	
	public Parry(Player p){
		try {
			shieldSS = new Animator(this.getClass().getResource("Shield.png").openStream(), this.getClass().getResource("Shield.ssc").openStream());
			sS = new Sprite(this.getClass().getResource("Shield.png").openStream(), this.getClass().getResource("Shield.ssc").openStream());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		actions = sS.getActions();
		p2 = p;
		
	}

	private class ParryHitBox extends BoxCollision {

		public ParryHitBox(double x, double y, double width, double height, Player p) {
			super(x, y, width, height, CollisionType.PLAYER_HITBOX_COLLISION);
		}

		@Override
		void onCollide(CollisionType t, CollisionData extraData) {

		}

		@Override
		CollisionData getCollisionData() {
			return new CollisionData(this, Parry.this);
		}
	}

	private ParryHitBox hbox;

	public void onTick(Input i, Player p) {
		long curtime = System.nanoTime();
		if (i.pressed(Button.B) && canParry) {
			isParrying = true;
			canParry = false;
			endParry = curtime + PARRY_DELTA;
			endDelay = endParry + PARRY_DELAY_DELTA;
			switch (p.getDirection()) {
			case DOWN:
				direct = Direction.DOWN;
				hbox = new ParryHitBox(p.getX() - PARRY_LENGTH / 2, p.getY() + Player.getPlayerHeight() / 2,
						PARRY_LENGTH, PARRY_WIDTH, p);
				break;
			case LEFT:
				direct = Direction.LEFT;
				hbox = new ParryHitBox(p.getX() - Player.getPlayerWidth() / 2 - PARRY_WIDTH,
						p.getY() - PARRY_LENGTH / 2, PARRY_WIDTH, PARRY_LENGTH, p);
				break;
			case RIGHT:
				direct = Direction.RIGHT;
				hbox = new ParryHitBox(p.getX() + Player.getPlayerWidth() / 2, p.getY() - PARRY_LENGTH / 2, PARRY_WIDTH,
						PARRY_LENGTH, p);
				break;
			case UP:
				direct = Direction.UP;
				hbox = new ParryHitBox(p.getX() - PARRY_LENGTH / 2,
						p.getY() - Player.getPlayerHeight() / 2 - PARRY_WIDTH, PARRY_LENGTH, PARRY_WIDTH, p);
				break;
			}
			lastY = p.getY();
			lastX = p.getX();
		}
		if (isParrying && curtime > endParry) {
			isParrying = false;
			hbox.destruct();
			hbox = null;
		}

		if (!canParry && curtime > endDelay) {
			canParry = true;
		}

		if (hbox != null) {
			double dx = p.getX() - lastX;
			double dy = p.getY() - lastY;

			hbox.setPos(hbox.getX() + dx, hbox.getY() + dy);
			lastX = p.getX();
			lastY = p.getY();
		}
	}
	
	public boolean isParrying(){
		return isParrying;
	}

	void draw(Graphics2D g) {
		if (hbox != null) {
			switch(direct){ //0 is DOWN, 3 is UP, 1 is LEFT, 2 is RIGHT
			case DOWN:
				g.drawImage(shieldSS.getFrameAtIndex("Down", 0).getScaledInstance(75, 63, 0), (int)p2.getX() - Player.getPlayerWidth()/2, (int)p2.getY(), null);
				break;
			case LEFT:
				g.drawImage(shieldSS.getFrameAtIndex("Side", 0).getScaledInstance(75, 63, 0), (int)(p2.getX() - Player.getPlayerWidth()), (int)p2.getY() - Player.getPlayerHeight()/2, null);
				break;
			case RIGHT: 
				g.drawImage(shieldSS.getFrameAtIndex("Side", 0).getScaledInstance(75, 63, 0), (int)p2.getX(), (int)p2.getY() - Player.getPlayerHeight()/2, null);
				break;
			case UP:
				g.drawImage(shieldSS.getFrameAtIndex("Up", 0).getScaledInstance(75, 63, 0), (int)p2.getX() - Player.getPlayerWidth()/2, (int)(p2.getY() - Player.getPlayerHeight()), null);
				break;
			}
		}
	}
	public Direction getDirection(){
		return direct;
	}
	public void destruct(){
		if(hbox!=null){
			hbox.destruct();
		}
	}

}
