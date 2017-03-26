import java.awt.Graphics2D;

import arcadia.Button;
import arcadia.Input;

public class Parry {
	private static final long PARRY_DELTA = 500000000L;
	private static final long PARRY_DELAY_DELTA = 30000000L;

	private static final double PARRY_WIDTH = 10;
	private static final double PARRY_LENGTH = 68;

	private boolean isParrying = false;
	private boolean canParry = true;

	private long endParry = 0;
	private long endDelay = 0;

	private double lastX;
	private double lastY;

	private class ParryHitBox extends BoxCollision { // HitBox is Misnomer

		public ParryHitBox(double x, double y, double width, double height, Player p) {
			super(x, y, width, height, CollisionType.PLAYER_HURTBOX_COLLISION);
		}
		
		@Override
		void onCollide(CollisionType t, CollisionData extraData) {
			// TODO Auto-generated method stub
			
		}

		@Override
		CollisionData getCollisionData() {
			return null;
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
				hbox = new ParryHitBox(p.getX() - PARRY_LENGTH / 2, p.getY() + Player.getPlayerHeight() / 2, PARRY_LENGTH, PARRY_WIDTH, p);
				break;
			case LEFT:
				hbox = new ParryHitBox(p.getX() - Player.getPlayerWidth() / 2 - PARRY_WIDTH, p.getY() - PARRY_LENGTH / 2, PARRY_WIDTH, PARRY_LENGTH, p);
				break;
			case RIGHT:
				hbox = new ParryHitBox(p.getX() + Player.getPlayerWidth() / 2, p.getY() - PARRY_LENGTH / 2, PARRY_WIDTH, PARRY_LENGTH, p);
				break;
			case UP:
				hbox = new ParryHitBox(p.getX() - PARRY_LENGTH / 2, p.getY() - Player.getPlayerHeight() / 2 - PARRY_WIDTH, PARRY_LENGTH, PARRY_WIDTH, p);
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

	void draw(Graphics2D g) {
		if (hbox != null) {
			hbox.drawCollision(g);
		}
	}

}
