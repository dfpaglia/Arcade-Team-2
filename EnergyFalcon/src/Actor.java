import java.awt.Graphics2D;

import arcadia.Input;

public interface Actor{
	void onTick(Input input);
	void draw(Graphics2D g);
	Collider getCollider();
}

