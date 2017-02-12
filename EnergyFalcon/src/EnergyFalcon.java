import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.io.IOException;

import javax.imageio.ImageIO;

import arcadia.Arcadia;
import arcadia.Game;
import arcadia.Input;
import arcadia.Sound;


public class EnergyFalcon extends Game {
	Image cover, background;
	private Player p;
	private GenericEnemy e;
	private PlayerHealth h;
	int hCount = 4;
	Wall[] walls;

	public EnergyFalcon() {

		p = new Player();
		e = new GenericEnemy(p);
		h = new PlayerHealth();
		walls = new Wall[4]; 
		
		walls[Wall.LEFT_WALL] = new Wall(0,0, 80, Game.HEIGHT); // LEFT WALL
		walls[Wall.TOP_WALL] = new Wall(0,0,Game.WIDTH, 40); // TOP WALL
		walls[Wall.RIGHT_WALL] = new Wall(Game.WIDTH - 80, 0, 80, Game.HEIGHT); // RIGHT WALL
		walls[Wall.BOTTOM_WALL] = new Wall(0, Game.HEIGHT - 80, Game.WIDTH, 80); //BOTTOM WALL
		
		try {
			cover = ImageIO.read(this.getClass().getResource("cover.jpg"));
			background = ImageIO.read(this.getClass().getResource("ArenaFix.png")).getScaledInstance(Game.WIDTH, Game.HEIGHT, 0);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public static void main(String[] args) {

		Arcadia.display(new Arcadia(new Game[] {new EnergyFalcon()}));

	}

	@Override
	public void tick(Graphics2D graphics, Input input, Sound sound) {
		// TODO Auto-generated method stub
		graphics.setColor(Color.black);
		graphics.fillRect(0, 0, Game.WIDTH, Game.HEIGHT);
		graphics.drawImage(background, 0, 0, null);
		graphics.drawImage(h.healthDraw(hCount), -20, -20, null);
		p.onTick(input);
		e.onTick(input);
		//TODO add a more modular way to check collision
		if(p.getCollider().collides(e.getCollider())){
			//TODO add something to do on collision
		}
		for(int i = 0;i<walls.length;i++){
			if(walls[i].getCollision().collides(p.getCollider())){
				p.collidesWithWall(i);
			}
		}
		p.draw(graphics);
		e.draw(graphics);
	}

	@Override
	public Image cover() {
		return cover;
	}

}

